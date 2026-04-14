package com.fmt;

/**
* Author: Carlos Bueno, Sowparnika Sandhya

* Date: 2023-03-10
* 
* This class models the invoices of sales. 
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Invoice {

	private int invoiceId = 0;
	private String invoiceCode;
	private Store store;
	private Person customer;
	private Person salesManager;
	private String date;
	private List<Item> items;

	public Invoice(int invoiceId, String invoiceCode, Store store, Person customer, Person salesManager, String date) {
		super();
		this.invoiceId = invoiceId;
		this.invoiceCode = invoiceCode;
		this.store = store;
		this.customer = customer;
		this.salesManager = salesManager;
		this.date = date;
		this.items = new ArrayList<Item>();
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public Invoice(String invoiceCode, Store store, Person customer, Person salesManager, String date) {
		super();
		this.invoiceCode = invoiceCode;
		this.store = store;
		this.customer = customer;
		this.salesManager = salesManager;
		this.date = date;
		this.items = new ArrayList<Item>();
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public Store getStore() {
		return store;
	}

	public Person getCustomer() {
		return customer;
	}

	public Person getSalesManager() {
		return salesManager;
	}

	public String getDate() {
		return date;
	}

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public Invoice(Invoice i, List<Item> listItem) {
		super();
		this.invoiceCode = i.getInvoiceCode();
		this.store = i.getStore();
		this.customer = i.getCustomer();
		this.salesManager = i.getSalesManager();
		this.date = i.getDate();
		this.items = listItem;
	}

	public String toSummaryReportString() {
		return (String.format("%s\t   %s \t%s \t%.2f \t%.2f", this.invoiceCode, this.store.getStoreCode(),
				this.customer.getName(), this.getTaxes(), this.getTotal()));
	}

	public String toSalesSummaryReportString() {
		return (String.format("%s\t   %s \t", this.store.getStoreCode(), this.salesManager.getName()));
	}

	public String toDBString() {
		return (String.format("%-9s %-10s %-22s %-22s $%9.2f\n", this.invoiceCode, this.store.getStoreCode(),
				this.customer.getName(), this.salesManager.getName(), this.getGrandTotal()));
	}

	public double getTaxes() {
		double totalTax = 0;
		for (int i = 0; i < items.size(); i++) {
			totalTax += items.get(i).getTaxes();
		}
		return totalTax;
	}

	public double getTotal() {
		double totalPrice = 0;
		for (int i = 0; i < items.size(); i++) {
			totalPrice += items.get(i).getTotal();
		}

		return totalPrice;
	}

	public double getGrandTotal() {
		return getTaxes() + getTotal();
	}

	// Compares Totals and sorts by totals
	public static <T> HashMap<T, Invoice> sortByTotal(HashMap<T, Invoice> hm) {

		// Create a list from elements of HashMap
		List<Map.Entry<T, Invoice>> list = new LinkedList<Map.Entry<T, Invoice>>(hm.entrySet());

		// Sorts the list
		Collections.sort(list, new Comparator<Map.Entry<T, Invoice>>() {
			public int compare(Map.Entry<T, Invoice> o1, Map.Entry<T, Invoice> o2) {
				if (o1.getValue().getGrandTotal() > o2.getValue().getGrandTotal()) {
					return -1;
				} else if (o1.getValue().getGrandTotal() > o2.getValue().getGrandTotal()) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		HashMap<T, Invoice> temp = new LinkedHashMap<T, Invoice>();
		for (Map.Entry<T, Invoice> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	@Override
	public String toString() {
		StringBuilder totalReport = new StringBuilder();

		totalReport.append(String.format(
				"\n\nInvoice   %s\n" + "Store     %s\n" + "Date      %s\n" + "Customer:   \n" + "%s" + "\n\n"
						+ "\nSales Person:" + "\n%s",
				getInvoiceCode(), getStore().getStoreCode(), getDate(), getCustomer().personInfoToString(),
				getSalesManager().personInfoToString()));

		totalReport.append(String.format("""
				\nItem                                                             Total
				+---------------------------------------------------------------------------+
				"""));

		double subTotal = getTotal();
		double totalTax = getTaxes();
		double grandTotal = getGrandTotal();

		for (Item item : getItems()) {
			totalReport.append(String.format("%s", item.itemInfoToString()));
		}

		totalReport.append(String.format("""
				\n+---------------------------------------------------------------------------+
				\n"""));
		totalReport.append(String.format(
				"                                                    SubTotal: $  %.2f\n "
						+ "                                                        Tax: $    %.2f\n "
						+ "                                                 GrandTotal: $  %.2f\n ",
				subTotal, totalTax, grandTotal));

		return totalReport.toString();
	}

	// Sort by grand total
	public static Comparator<Invoice> compareByGrandTotal = new Comparator<Invoice>() {

		public int compare(Invoice inv1, Invoice inv2) {
			double invTotalA = inv1.getGrandTotal();
			double invTotalB = inv2.getGrandTotal();

			// adds bigger to the start
			if (invTotalA < invTotalB) {
				return 1;
			} else if (invTotalA > invTotalB) {
				return -1;
			} else {
				return 0;
			}

		}

	};

	// Sort by name alphabetically
	public static Comparator<Invoice> compareByName = new Comparator<Invoice>() {

		@Override
		public int compare(Invoice p1, Invoice p2) {
			String invPerson1 = p1.getCustomer().getName();
			String invPerson2 = p2.getCustomer().getName();

			return invPerson1.compareTo(invPerson2);
		}

	};

	// Sort store by store code, then by salesperson name
	public static Comparator<Invoice> compareByStore = new Comparator<Invoice>() {

		public int compare(Invoice s1, Invoice s2) {
			String invStore1 = s1.getStore().getStoreCode();
			String invStore2 = s2.getStore().getStoreCode();
			int storeResult = invStore1.compareTo(invStore2);

			if (storeResult == 0) {
				return s1.getSalesManager().getName().compareTo(s2.getSalesManager().getName());
			}
			return storeResult;

		}

	};
}

package com.fmt;

import java.util.Comparator;

/**
 * Author: Carlos Bueno, Sowparnika Ssandhya
 *
 * Date: 2023-03-10
 * 
 * This class give summary reports on sales and sale totals. 
 */

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

public class InvoiceReport {

	/**
	 * Prints the total Summary Report
	 * 
	 * @param <T>
	 * @param invoices
	 */
	public static <T> void summaryReport(HashMap<T, Invoice> invoices) {
		StringBuilder totalReport = new StringBuilder();

		totalReport.append(String.format("""
				+----------------------------------------------------------------------------------------------+
				| Summary Report - By Total                                                                    |
				+----------------------------------------------------------------------------------------------+
				Invoice #       Store        Customer                 Num Items          Tax           Total
				\n"""));

		int numOfItem = 0;
		double totalTax = 0;
		double grandTotal = 0;

		for (T list : invoices.keySet()) {
			numOfItem = numOfItem + invoices.get(list).getItems().size();
			totalTax = totalTax + invoices.get(list).getTaxes();
			grandTotal = grandTotal + invoices.get(list).getGrandTotal();

			totalReport.append(String.format("%s       %9s       %-20s %9d           $%8.2f    $%11.2f\n",
					invoices.get(list).getInvoiceCode(), invoices.get(list).getStore().getStoreCode(),
					invoices.get(list).getCustomer().getName(), invoices.get(list).getItems().size(),
					invoices.get(list).getTaxes(), invoices.get(list).getGrandTotal()));
		}
		totalReport.append(String.format("""
				+----------------------------------------------------------------------------------------------+
				\n"""));

		totalReport.append(String.format("%60d          $ %.2f    $  %.2f", numOfItem, totalTax, grandTotal));
		totalReport.append(String.format("\n"));

		System.out.println(totalReport);
	}

	/**
	 * Prints the Sales Summary report of the store
	 * 
	 * @param <T>
	 * @param stores
	 */
	public static <T> void salesSummaryReport(HashMap<T, Store> stores) {
		StringBuilder totalReport = new StringBuilder();

		totalReport.append(("""
				+---------------------------------------------------------------------------+
				| Store Sales Summary Report                                                |
				+---------------------------------------------------------------------------+
				Store      Manager                    #Sales          Grand Total
				\n"""));

		int numOfSales = 0;
		double grandTotal = 0;
		for (T list : stores.keySet()) {
			numOfSales = numOfSales + stores.get(list).invoices.size();
			grandTotal = grandTotal + stores.get(list).getGrandTotal();

			totalReport.append(String.format("%s     %s   \t\t%d    \t\t$ %.2f         \n",
					stores.get(list).getStoreCode(), stores.get(list).getManager().getName(),
					stores.get(list).invoices.size(), stores.get(list).getGrandTotal()));
		}

		totalReport.append(String.format("""
				+---------------------------------------------------------------------------+
				\n"""));

		totalReport.append(String.format("%41d          \t$ %.2f", numOfSales, grandTotal));
		totalReport.append(String.format("\n"));

		System.out.println(totalReport);

	}

	/**
	 * prints Summary of Invoice Items.
	 * 
	 * @param <T>
	 * @param invoices
	 */
	public static <T> void invoiceItemsSummary(HashMap<T, Invoice> invoices) {
		for (Invoice i : invoices.values()) {
			System.out.println(i);
		}
	}

	public static <T> void databaseReports(HashMap<Integer, Invoice> invoiceDB, Comparator<Invoice> comp, String type) {
		MyLinkedList<Invoice> invoiceList = new MyLinkedList<Invoice>(comp);

		System.out.println("+---------------------------------------------------------------------------+");
		System.out.println("| Sales by " + type + "                                                      ");
		System.out.println("+---------------------------------------------------------------------------+");
		System.out.println("Sale      Store      Customer               Salesperson            Total");

		for (Invoice invoice : invoiceDB.values()) {
			invoiceList.addElement(invoice);
		}

		for (int i = 0; i < invoiceList.getSize(); i++) {
			System.out.print(invoiceList.get(i).toDBString());
		}
	}

	/**
	 * Prints out various invoice reports using data from CSV files.
	 */
	public static void csvSalesReport() {

		HashMap<String, Person> persons = LoadData.mapPersonFile();
		HashMap<String, Store> stores = LoadData.parseStoreFile(persons);
		HashMap<String, Invoice> invoices = LoadData.parseInvoiceDataFile(stores, persons);
		List<Item> updatedInvoices = LoadData.parseInvoiceItemFile(invoices);

		summaryReport(Invoice.sortByTotal(invoices));
		salesSummaryReport(Store.sortByNames(stores));
		invoiceItemsSummary(invoices);
	}

	/**
	 * Prints out various invoice reports using data from the database.
	 */
	public static void databaseSalesReport() {

		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);

		HashMap<Integer, Store> storesDB = DatabaseLoader.loadStore();
		HashMap<Integer, Invoice> invoiceDB = DatabaseLoader.getInvoices(storesDB);
		DatabaseLoader.getItemList(invoiceDB);

		summaryReport(Invoice.sortByTotal(invoiceDB));
		salesSummaryReport(Store.sortByNames(storesDB));
		invoiceItemsSummary(invoiceDB);

	}

	public static void databaseSortedSalesReport() {

		HashMap<Integer, Store> storesDB = DatabaseLoader.loadStore();
		HashMap<Integer, Invoice> invoiceDB = DatabaseLoader.getInvoices(storesDB);
		DatabaseLoader.getItemList(invoiceDB);

		databaseReports(invoiceDB, Invoice.compareByName, "Customer");
		databaseReports(invoiceDB, Invoice.compareByGrandTotal, "Total");
		databaseReports(invoiceDB, Invoice.compareByStore, "Store");
	}

	public static void main(String[] args) {

		InvoiceReport.csvSalesReport();

	}

}
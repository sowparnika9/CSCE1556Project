package com.fmt;

/**
 * 
 * Author: Carlos Bueno & Sowparnika Sandhya Date: 2023-02-24
 *
 * This class represents Product.
 *
 */
public class Product extends Item {
	private String unit;
	private double unitPrice;
	private double quantity;

	public Product(String code, String name, String unit, double unitPrice) {
		super(code, name);
		this.unit = unit;
		this.unitPrice = unitPrice;
	}

	public Product(String code, String name, String unit, double unitPrice, double quantity) {
		super(code, name);
		this.unit = unit;
		this.unitPrice = unitPrice;
		this.quantity = quantity;

	}

	public String getUnit() {
		return unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}
	
	public double getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return this.getCode() + " " + " " + this.getName() + " " + unit + " " + unitPrice + " ";
	}

	@Override
	//Calculates the taxes of the product
	public double getTaxes() {
		return Math.round(getTotal() * 0.0715 * 100)/ 100.0;
	}

	//Calculates the total with respect to the quantity purchased along with the unit price.
	@Override
	public double getTotal() {
		return Math.round(unitPrice * quantity * 100)/100.0;
	}

	//Calculates the grand total which includes total along with taxes
	public double getGrandTotal() {
		return getTotal() + getTaxes();

	}

	@Override
	public String itemInfoToString() {
		return (String.format("\n%s    (Product)  %s\n 		%.2f @ $%.2f / %s		\n\t\t\t\t\t\t\t\t$ %.2f", 
				this.getCode(), 
				this.getName(),
				this.getQuantity(),
				this.getUnitPrice(),
				this.getUnit(),
				this.getTotal()));
	}
}
package com.fmt;

/**
 * 
 * Author: Carlos Bueno & Sowparnika Sandhya Date: 2023-02-24
 *
 * This class models an Item.
 *
 */
public abstract class Item {
	private String code;
	private String name;	
	
	public Item(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public abstract double getTaxes();

	public abstract double getTotal();

	public double getGrandTotal() {
		return getTaxes() + getTotal();
	};
	
	public abstract String itemInfoToString();
	
}
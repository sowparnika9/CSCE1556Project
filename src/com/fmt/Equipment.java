package com.fmt;

/**
 * **
 * 
 * Author: Carlos Bueno & Sowparnika Sandhya Date: 2023-02-24
 *
 * This class represents Equipment-Item
 *
 */
public class Equipment extends Item {
	private String model;

	public Equipment(String code, String name, String model) {
		super(code, name);
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	@Override
	public double getTaxes() {
		return 0.0;
	}

	@Override
	public double getTotal() {
		return 0.0;
	}

	@Override
	public String toString() {
		return this.getCode() + " " + " " + this.getName() + " " + this.model;
	}

	@Override
	public String itemInfoToString() {
		return null;
	}

}
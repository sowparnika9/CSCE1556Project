package com.fmt;

/**
 * 
 *Author: Carlos Bueno & Sowparnika Sandhya
 *Date: 2023-02-24
 *
 *This class models an Address
 *
 */
public class Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;
	public Address(String street, String city, String state, String zip, String country) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}
	
	public String dateToString() {
		return (String.format("\t%s\n \t%s %s %s %s", this.getStreet(), this.getCity(), this.getState(), this.getZip(), this.getCountry()));
	}
	
}
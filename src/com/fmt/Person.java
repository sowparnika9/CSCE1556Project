package com.fmt;

import java.util.List;

/**
* 
*Author: Carlos Bueno & Sowparnika Sandhya
*Date: 2023-02-24
*
*This class models a Person
*
*/
public class Person {
	private String personCode;
	private String lastName;
	private String firstName;
	private Address address;
	private List<String> emails;
	
	public Person(String personCode, String lastName, String firstName, Address addressP, List<String> email) {
		super();
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = addressP;
		this.emails = email;
	}	
	public String getPersonCode() {
		return personCode;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getName() {
		return lastName + "," + firstName;
	}
	
	public String personInfoToString() {
		return (String.format("%s  (%s : %s)\n  %s", this.getName(), this.getPersonCode(), this.emails, this.address.dateToString() ));
	}
}	

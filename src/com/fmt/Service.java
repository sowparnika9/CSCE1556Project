package com.fmt;

/**
 * 
 * Author: Carlos Bueno & Sowparnika Sandhya Date: 2023-02-24
 *
 * This class represents Services.
 *
 */
public class Service extends Item {

	private double hourlyRate;
	private double hoursBilled;

	public Service(String code, String name, double hourlyRate) {
		super(code, name);
		this.hourlyRate = hourlyRate;
	}

	public Service(String code, String name, double hourlyRate, double hoursBilled) {
		super(code, name);
		this.hourlyRate = hourlyRate;
		this.hoursBilled = hoursBilled;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public double getHoursBilled() {
		return hoursBilled;
	}

	public String toString() {
		return this.getCode() + " " + " " + this.getName() + " " + hourlyRate;
	}

	//Calculates the taxes with respect to the service used per hour
	public double getTaxes() {
		return Math.round(getTotal() * .0345 * 100) / 100.0;
	}

	//Calculates then total with respect to hours utilized and rate per hour
	public double getTotal() {
		return Math.round(hoursBilled * hourlyRate * 100) / 100.0;
	}

	public String itemInfoToString() {
		return (String.format("\n%s    (Service)    %s     \n 	   %.2f hours @ %.2f/hr			\n\t\t\t\t\t\t\t         $ %.2f", 
				this.getCode(), 
				this.getName(), 
				this.getHoursBilled(),
				this.getHourlyRate(),
				this.getTotal()));
	}

}
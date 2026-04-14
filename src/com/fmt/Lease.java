package com.fmt;
/**
* Author: Carlos Bueno, Sowparnika Ssandhya

* Date: 2023-03-10
* 
* This class models a lease for equipment. 
*/

import java.time.LocalDate;
//import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Lease extends Equipment {
	private double leaseRate;
	private String startDate;
	private String endDate;

	
	public Lease(String code, String name, String model, double leaseRate, String startDate,
			String endDate) {
		super(code, name, model);
		this.leaseRate = leaseRate;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public double getLeaseRate() {
		return leaseRate;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public int getDayOfLease() {
		return (int) ChronoUnit.DAYS.between(LocalDate.parse(this.startDate), LocalDate.parse(this.endDate)) + 1;
	}


	//Calculating the taxes

	public double getTaxes() {
		double taxes = 0;
		if (getTotal() < 10000) {
			taxes = 0;

		} else if (getTotal() >= 10000 && getTotal() < 100000) {
			taxes = 500;

		} else if (getTotal() >= 100000) {
			taxes = 1500;
		}
		return taxes;
	}
	
	//Calculating the total amount for equipment lease
	public double getTotal() {
		double total = Math.round((leaseRate * this.getDayOfLease()) /30.0 * 100) / 100.0;
		return total;
	}
	
	public String itemInfoToString() {
		return (String.format("\n%s     (Lease)     %s-%s      \n  %d days	(%s --> %s) @ $%.2f / 30 days				\n\t\t\t\t\t\t\t       $ %.2f", 
				this.getCode(), 
				this.getName(),
				this.getModel(),
				this.getDayOfLease(),
				this.getStartDate(),
				this.getEndDate(),
				this.getLeaseRate(),
				this.getTotal()));
	}

}
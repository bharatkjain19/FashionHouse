package com.fashion.mart.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
	private String userName;
	private int age;
	private int height;
	private String gender;
	private long salesAmount;
	private Date lastPurchaseDate;
	
	@Override
	public String toString() {
		return "Customer [userName=" + userName + ", age=" + age + ", height=" + height + ", gender= " + gender + ", salesAmount=" + salesAmount + ", lastPurchasingDate="
				+ lastPurchaseDate.toLocaleString() + "]";
	}
}

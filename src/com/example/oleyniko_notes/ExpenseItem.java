/*
Expense Tracker: Expense tracking application
Copyright (C) 2015  Oleg Oleynikov oleyniko@ualberta.ca

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.example.oleyniko_notes;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ExpenseItem implements Serializable{

	
	private static final long serialVersionUID = 250137177617934952L;
	private Date date;
	private String category;
	private String description;
	private String currency;
	private Double amount;
	
	public ExpenseItem() {
		// TODO Auto-generated constructor stub
	}
	
	public ExpenseItem(Date date, String category, String description, String currency, Double amount) {
		this.date = date;
		this.category = category;
		this.description = description;
		this.currency = currency;
		this.amount = amount;
	}

	public ExpenseItem(Parcel src) {
		this.date = new Date(src.readLong());
		this.category = src.readString();
		this.description = src.readString();
		this.currency = src.readString();
		this.amount = src.readDouble();
	}

	// declare how to convert the item to String, giving a summary of the Item
	public String toString()
	{
		String date = DateFormat.getDateInstance().format(this.date);
		
		return  this.description + ", a " + this.category + " item;\nOn " + date + "\nCost: " + this.amount.toString() + " " + this.currency;
	}
	
	
	// getters and setters
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}	

}

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;

public class Claim implements Serializable{

	// private fields
	private static final long serialVersionUID = -485618097047181921L;
	private ArrayList<ExpenseItem> expenseItems;
	private Date dateStarted;
	private Date dateEnded;
	private String description;
	private boolean inProgress = true;
	private boolean isSubmitted = false;
	private boolean isReturned = false;
	private boolean isApproved = false;
	// store amount of each currency for the Claim
	private Map<String, Double> claimAmount;
	
	
	
	public Claim(Date dateStarted, Date dateEnded, String description) {
		this.setDateStarted(dateStarted);
		this.setDateEnded(dateEnded);
		this.setDescription(description);
		this.expenseItems = new ArrayList<ExpenseItem>();
		initializeClaimAmount(new Double(0), new Double(0), new Double(0), new Double(0));
	}
	
	@SuppressWarnings("unchecked")
	private Claim(Parcel src)
	{
		this.expenseItems = new ArrayList<ExpenseItem>();
		this.expenseItems = src.readArrayList(ExpenseItem.class.getClassLoader());
		this.dateStarted = new Date(src.readLong());
		this.dateEnded = new Date(src.readLong());
		this.description = src.readString();
		this.inProgress = src.readByte() != 0;
		this.isSubmitted = src.readByte() != 0;
		this.isReturned = src.readByte() != 0;
		this.isApproved = src.readByte() != 0;
		
		
		// awful, Awful, AWFUL hack. but good enough for this assignment because the Map is of constant size
		Double cad = src.readDouble();
		Double usd = src.readDouble();
		Double eur = src.readDouble();
		Double gbp = src.readDouble();
		
		initializeClaimAmount(cad, usd, eur, gbp);
	}

	
	public void addExpenseItem(ExpenseItem item)
	{
		// add an expense item to the Claim's list of expense items
		// update claim cost listings as well
		this.expenseItems.add(item);
		claimAmount.put(item.getCurrency(), claimAmount.get(item.getCurrency()) + item.getAmount());
	}
	
	public void removeExpenseItem(ExpenseItem item)
	{
		// remove an expense item from the Claim's list of expense items by reference
		// update claim cost listings as well
		claimAmount.put(item.getCurrency(), claimAmount.get(item.getCurrency()) - item.getAmount());
		this.expenseItems.remove(item);
	}
	
	public void removeExpenseItem(int id)
	{
		// remove an expense item from the Claim's list of expense items by id
		// update claim cost listings as well
		ExpenseItem item = expenseItems.get(id);
		claimAmount.put(item.getCurrency(), claimAmount.get(item.getCurrency()) - item.getAmount());
		this.expenseItems.remove(item);
	}
	
	public void initializeClaimAmount(Double cad, Double usd, Double eur, Double gbp)
	{
		// method to initialize claim cost listings 
		claimAmount = new HashMap<String, Double>();
		claimAmount.put("CAD", cad);
		claimAmount.put("USD", usd);
		claimAmount.put("EUR", eur);
		claimAmount.put("GBP", gbp);
	}

	// declare how to convert the claim to String, giving a summary of the Claim
	// expense items are converted to String separately
	public String toString()
	{
		String status = new String();
		if (this.inProgress) status = "Status: In Progress";
		else if (this.isSubmitted) status = "Status: Submitted";
		else if (this.isReturned) status = "Status: Returned";
		else if (this.isApproved) status = "Status: Approved";
		String dateStarted = DateFormat.getDateInstance().format(this.dateStarted);
		String dateEnded = DateFormat.getDateInstance().format(this.dateEnded);
		String cost = "CAD: " + claimAmount.get("CAD").toString() + 
						" USD: " + claimAmount.get("USD").toString() +
						" EUR: " + claimAmount.get("EUR").toString() +
						" GBP: " + claimAmount.get("GBP").toString();
		return this.description + " from " + dateStarted + " to " + dateEnded + "\n" + cost + "\n" + status;
	}
	
	// a bunch of getters and setters
	
	public ArrayList<ExpenseItem> getExpenseItems() {
		return expenseItems;
	}

	public void setExpenseItems(ArrayList<ExpenseItem> expenseItems) {
		this.expenseItems = expenseItems;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateEnded() {
		return dateEnded;
	}

	public void setDateEnded(Date dateEnded) {
		this.dateEnded = dateEnded;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}
	
	public boolean getInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	
	public boolean getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}
	
	public boolean getIsReturned() {
		return isReturned;
	}

	public void setIsReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}
	
	public boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	
	
}

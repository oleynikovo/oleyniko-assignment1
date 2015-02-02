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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemActivity extends Activity {

	// Activity to add items. Triples as an Activity to view Items, and an Activity to Edit Items
	// the functioning is defined by the itemPos value sent from View Claim Activity
	// negative value means add an item, positive means view ad edit 
	private ArrayList<Claim> claims = ClaimListController.getClaimList();
	private Integer claimPos;
	private Integer itemPos;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);
		
		// retrieve sent Claim position and Item position from previous Activity
		// adapted on January 30, 2015 from
		// https://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if (bundle != null)
		{
			claimPos = (Integer) bundle.get("Claim Position");
			itemPos = (Integer) bundle.get("Item Position");
			if (!itemPos.equals(new Integer(-1)))
			{
				// if editing an item, load stuff from Item into EditText and Spinner boxes
				ExpenseItem item = claims.get(claimPos).getExpenseItems().get(itemPos);
			
				EditText etDescription = (EditText) findViewById(R.id.itemDescription);
				etDescription.setText(item.getDescription(), TextView.BufferType.EDITABLE);
			
				EditText etCategory = (EditText) findViewById(R.id.itemCategory);
				etCategory.setText(item.getCategory(), TextView.BufferType.EDITABLE);
			
				EditText etDate = (EditText) findViewById(R.id.itemDate);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(item.getDate());
				String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
				String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
				String year = Integer.toString(calendar.get(Calendar.YEAR));
				String dateRepr = day + "/" + month + "/" + year;
				etDate.setText(dateRepr, TextView.BufferType.EDITABLE);
			
				Spinner spCurrency = (Spinner) findViewById(R.id.itemCurrency);
				int spinPos = 0;
				if (item.getCurrency().equals("CAD")) spinPos = 0;
				else if (item.getCurrency().equals("USD")) spinPos = 1;
				else if (item.getCurrency().equals("EUR")) spinPos = 2;
				else if (item.getCurrency().equals("GBP")) spinPos = 3;
				spCurrency.setSelection(spinPos);
			
				EditText etAmount = (EditText) findViewById(R.id.itemAmount);
				etAmount.setText(item.getAmount().toString(), TextView.BufferType.EDITABLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void cancelItem(View v)
	{
		// go back to View Claim Activity without save and put claim position back
		Intent intent = new Intent(AddItemActivity.this, ViewClaimActivity.class);
		intent.putExtra("Position", claimPos);
		startActivity(intent);
	}
	
	public void saveItem(View v)
	{
		// save or update item and go back to View Claim Activity
		
		// regex to check if input follows format DD/MM/YYYY
		// don't worry about putting anything like 99/99/9999, Calendar class will handle it
		String regEx = "[0-9]{2}/[0-9]{2}/[0-9]{4}";
		if (!itemPos.equals(new Integer(-1)))
		{
			claims.get(claimPos).removeExpenseItem(itemPos);
		}
		
		EditText dateView = (EditText) findViewById(R.id.itemDate);
		EditText categoryView = (EditText) findViewById(R.id.itemCategory);
		EditText descriptionView = (EditText) findViewById(R.id.itemDescription);
		EditText amountView = (EditText) findViewById(R.id.itemAmount);
		Spinner currencyView = (Spinner) findViewById(R.id.itemCurrency);
		
		// warning that date format is wrong
		if (!dateView.getText().toString().matches(regEx))
		{
			Toast.makeText(this, "Date format is not matched! Should be DD/MM/YYYY", Toast.LENGTH_LONG).show();
		}
		else
		{
			String day = dateView.getText().toString().substring(0, 2);
			String month = dateView.getText().toString().substring(3, 5);
			String year = dateView.getText().toString().substring(6, 10);
		
			Calendar dateCal = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
		
			int currencyId = currencyView.getSelectedItemPosition();
		
			String currencyVal = new String();
		
			if (currencyId == 0) currencyVal = "CAD";
			else if (currencyId == 1) currencyVal = "USD";
			else if (currencyId == 2) currencyVal = "EUR";
			else if (currencyId == 3) currencyVal = "GBP";
		
			Double amountVal = Double.parseDouble(amountView.getText().toString()); 
		
			ExpenseItem item = new ExpenseItem(dateCal.getTime(), categoryView.getText().toString(), descriptionView.getText().toString(), currencyVal, amountVal);
		
			claims.get(claimPos).addExpenseItem(item);
		
			Intent intent = new Intent(AddItemActivity.this, ViewClaimActivity.class);
			intent.putExtra("Position", claimPos);
			startActivity(intent);
		}
	}
	
	public void removeItem(MenuItem menu)
	{
		if (itemPos > -1)
		{
			ExpenseItem i = claims.get(claimPos).getExpenseItems().get(itemPos);
			claims.get(claimPos).removeExpenseItem(i);
			Intent intent = new Intent(AddItemActivity.this, ViewClaimActivity.class);
			intent.putExtra("Position", claimPos);
			startActivity(intent);
		}
	}
}

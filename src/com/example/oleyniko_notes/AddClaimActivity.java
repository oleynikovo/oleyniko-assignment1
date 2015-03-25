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
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddClaimActivity extends Activity {

	// Activity where we add a new Claim to the global list of claims
	// doubles as Edit Claim
	// the functioning is defined by the claimPos value sent from View Claim Activity (non-negative)
	// and Main Activity (receive -1)
	private ArrayList<Claim> claims = ClaimListController.getClaimList();
	private Integer claimPos;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_claim);
		
		// retrieve sent Claim Position from previous Activity
		// adapted on January 30, 2015 from the answer by William Tate at
		// https://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if (bundle != null)
		{
			claimPos = (Integer) bundle.get("Claim Position");
			if (!claimPos.equals(new Integer(-1)))
			{
				// if claimPos is not negative, means we are editing the claim
				// load info from the claim into EditText boxes
				Claim claim = claims.get(claimPos);
				
				EditText dateStartedET = (EditText) findViewById(R.id.claimStartDate);
				EditText dateEndedET = (EditText) findViewById(R.id.claimEndDate);
				EditText descriptionET = (EditText) findViewById(R.id.claimDescription);
				
				descriptionET.setText(claim.getDescription(), TextView.BufferType.EDITABLE);
				
				Calendar calendarStart = Calendar.getInstance();
				calendarStart.setTime(claim.getDateStarted());
				String dayStart = Integer.toString(calendarStart.get(Calendar.DAY_OF_MONTH));
				String monthStart = Integer.toString(calendarStart.get(Calendar.MONTH) + 1);
				String yearStart = Integer.toString(calendarStart.get(Calendar.YEAR));
				String dateStartRepr = dayStart + "/" + monthStart + "/" + yearStart;
				dateStartedET.setText(dateStartRepr, TextView.BufferType.EDITABLE);
				
				Calendar calendarEnded = Calendar.getInstance();
				calendarEnded.setTime(claim.getDateEnded());
				String dayEnded = Integer.toString(calendarEnded.get(Calendar.DAY_OF_MONTH));
				String monthEnded = Integer.toString(calendarEnded.get(Calendar.MONTH) + 1);
				String yearEnded = Integer.toString(calendarEnded.get(Calendar.YEAR));
				String dateEndedRepr = dayEnded + "/" + monthEnded + "/" + yearEnded;
				dateEndedET.setText(dateEndedRepr, TextView.BufferType.EDITABLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_claim, menu);
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
	
	public void cancelClaim(View v)
	{
		// return to main activity without saving
		Intent intent = new Intent(AddClaimActivity.this, MainActivity.class);
    	startActivity(intent);
	}
	
	public void saveClaim(View v) throws java.text.ParseException
	{
		// save claim to the global list of claims
		// or update it, if we are editing the claim
		
		// regex to check if input follows format DD/MM/YYYY
		// don't worry about putting anything like 99/99/9999, Calendar class will handle it
		String regEx = "[0-9]{2}/[0-9]{2}/[0-9]{4}";
		
		// read from inputs on the screen 
		EditText desc = (EditText) findViewById(R.id.claimDescription);
		EditText start = (EditText) findViewById(R.id.claimStartDate);
		EditText end = (EditText) findViewById(R.id.claimEndDate);

		
		// warning that date format is wrong
		if (!start.getText().toString().matches(regEx) || !end.getText().toString().matches(regEx))
		{
			Toast.makeText(this, "Date format is not matched! Should be DD/MM/YYYY", Toast.LENGTH_LONG).show();
		}
		
		else
		{
			String startDay = start.getText().toString().substring(0, 2);
			String startMonth = start.getText().toString().substring(3, 5);
			String startYear = start.getText().toString().substring(6, 10);
		
			String endDay = end.getText().toString().substring(0, 2);
			String endMonth = end.getText().toString().substring(3, 5);
			String endYear = end.getText().toString().substring(6, 10);
		
			Calendar startDate = new GregorianCalendar(Integer.parseInt(startYear), Integer.parseInt(startMonth) - 1, Integer.parseInt(startDay));
			Calendar endDate = new GregorianCalendar(Integer.parseInt(endYear), Integer.parseInt(endMonth) - 1, Integer.parseInt(endDay));
		
			// if the Claim is being edited, just update it. Otherwise, save info from inputs and add a new Claim
			// then order the global list of claims by DateStarted
			if (claimPos > -1)
			{
				claims.get(claimPos).setDateStarted(startDate.getTime());
				claims.get(claimPos).setDateEnded(endDate.getTime());
				claims.get(claimPos).setDescription(desc.getText().toString());
			}
		
			else
			{
				Claim claim = new Claim(startDate.getTime(), endDate.getTime(), desc.getText().toString());
				claims.add(claim);
			}
			Collections.sort(claims, new Comparator<Claim>()
				{
					public int compare(Claim c1, Claim c2)
					{
						return c1.getDateStarted().compareTo(c2.getDateStarted());
					}
				});
		
			Intent intent = new Intent(AddClaimActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}
}

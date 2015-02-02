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
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewClaimActivity extends Activity {
	// Activity to view the selected claim from Main Activity
	
	// instantiate the global claims list
	private ArrayList<Claim> claims = ClaimListController.getClaimList();
	// claim position in the global claims list
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_claim);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		if (bundle != null)
		{
			final Integer pos = (Integer) bundle.get("Position");
			id = pos;
			Claim c = claims.get(pos);
			TextView tv = (TextView) findViewById(R.id.claimViewTextView);
			tv.setText(c.toString());
			
			// find the list view to load stuff into it
			final ListView lv = (ListView) findViewById(R.id.itemViewListView);
	        
			// set an array adapter for ArrayList of ExpenseItems, so that Android knows how to put its items in ListView
	        // custom list item copied on January 31, 2015 from
	        // https://stackoverflow.com/questions/2464056/change-font-size-in-listview-android-eclipse
	        ArrayAdapter<ExpenseItem> claimAdapter = new ArrayAdapter<ExpenseItem>(this, R.layout.custom_list_item, c.getExpenseItems());
	        lv.setAdapter(claimAdapter);
	      
	        // wait for an item to be clicked in ListView and send list position 
	        // to Add Item Activity, which triples as View Item Activity and Edit Item Activity  
	        // onItemClickListener copied on February 1, 2015 from
	        // https://stackoverflow.com/questions/9097723/adding-a-onclicklistener-to-listview-android
	        lv.setOnItemClickListener( new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	        	{
	        		Intent intent = new Intent(ViewClaimActivity.this, AddItemActivity.class);
	        		intent.putExtra("Item Position", new Integer(position));
	        		intent.putExtra("Claim Position", pos);
	            
	        		startActivity(intent);
	        	}
	        });
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_claim, menu);
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
	
	public void backToMainActivity(View v)
	{
		// go back to main activity
		// It's like pressing the back button, except another Activity is generated!
		Intent intent = new Intent(ViewClaimActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void addItem(View v)
	{
		// move to activity to add an item to Expense Items list
		// send -1 for item position, so that the next Activity knows it shoul dbe a new one
		Intent intent = new Intent(ViewClaimActivity.this, AddItemActivity.class);
		intent.putExtra("Claim Position", id);
		intent.putExtra("Item Position", -1);
		startActivity(intent);
	}
	
	 public void editClaim(MenuItem menu)
	 {
		 // edit claim
		 //the Claim Position is non-negative, so the next Activity knows to load stuff from this Claim
		 Intent intent = new Intent(ViewClaimActivity.this, AddClaimActivity.class);
		 intent.putExtra("Claim Position", id);
		 startActivity(intent);
	 }
	    
	 public void removeClaim(MenuItem menu)
	 {
		 // remove this claim from the global list of claims and return to main activity
		 claims.remove(id);
		 Intent intent = new Intent(ViewClaimActivity.this, MainActivity.class);
		 startActivity(intent);
	 }
	    
	 public void emailClaim(MenuItem menu)
	 {
		 // try and send an e-mail from within the application
		 // Copied on February 2, 2015 from
		 // https://stackoverflow.com/questions/8284706/send-email-via-gmail
		 String emailBody = claims.get(id).toString() + "\nItems:\n";
		 for (ExpenseItem ei : claims.get(id).getExpenseItems())
		 {
			 emailBody = emailBody + "\n\n" + ei.toString();
		 }
		 Intent send = new Intent(Intent.ACTION_SENDTO);
		 String uriText = "mailto:" + Uri.encode("oleyniko@ualberta.ca") + 
		           "?subject=" + Uri.encode("Claim from oleyniko-notes") + 
		           "&body=" + Uri.encode(emailBody);
		 Uri uri = Uri.parse(uriText);

		 send.setData(uri);
		 startActivity(Intent.createChooser(send, "Send mail..."));
	 }
	 
	 
	 // change status of the claim. other than the field value, nothing changes 
	 public void denoteSubmitted(MenuItem menu)
	 {
		 claims.get(id).setInProgress(false);
		 claims.get(id).setIsReturned(false);
		 claims.get(id).setIsApproved(false);
		 claims.get(id).setIsSubmitted(true);
		 
		 Intent intent = new Intent(ViewClaimActivity.this, MainActivity.class);
		 startActivity(intent);
	 }
	 
	 public void denoteReturned(MenuItem menu)
	 {
		 if (claims.get(id).getIsSubmitted())
		 {
			 claims.get(id).setInProgress(false);
			 claims.get(id).setIsReturned(true);
			 claims.get(id).setIsApproved(false);
			 claims.get(id).setIsSubmitted(false);
		 }
		 else
		 {
			 Toast.makeText(this, "Cannot edit a non-submitted claim!", Toast.LENGTH_LONG).show();
		 }
	 }
	 
	 public void denoteApproved(MenuItem menu)
	 {
		 if (claims.get(id).getIsSubmitted())
		 {
			 claims.get(id).setInProgress(false);
			 claims.get(id).setIsReturned(false);
			 claims.get(id).setIsApproved(true);
			 claims.get(id).setIsSubmitted(false);
		 }
		 else
		 {
			 Toast.makeText(this, "Cannot edit a non-submitted claim!", Toast.LENGTH_LONG).show();
		 }
	 }
}

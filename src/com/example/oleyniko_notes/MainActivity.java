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

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

	// instantiate the global claims list
	private ArrayList<Claim> claims = ClaimListController.getClaimList();
	
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // find the list view to load stuff into it
        final ListView lv = (ListView) findViewById(R.id.claimListView);
        
        // set an array adapter for ArrayList of Claims, so that Android knows how to put its items in ListView
        // custom "list item" format copied on January 31, 2015 from the answer by Shereef Marzouk at
        // https://stackoverflow.com/questions/2464056/change-font-size-in-listview-android-eclipse
        ArrayAdapter<Claim> claimAdapter = new ArrayAdapter<Claim>(this, R.layout.custom_list_item, this.claims);
        lv.setAdapter(claimAdapter);
        
        // wait for an item to be clicked in ListView and send list position to View Claim Activity
        // onItemClickListener copied on February 1, 2015 from the answer by Maulik J at
        // https://stackoverflow.com/questions/9097723/adding-a-onclicklistener-to-listview-android
        lv.setOnItemClickListener( new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        	{
        		Object o = lv.getItemAtPosition(position);
        	       Claim c=(Claim)o;
        	       Intent intent = new Intent(MainActivity.this, ViewClaimActivity.class);
        	       intent.putExtra("Position", new Integer(position));
        	       startActivity(intent);
        	}
		});
    }
    
    
    // onStop and onRestart are supposed to serialize the claims list and save/restore to/from a file
    // it probably does not work like that at all
    @Override
    protected void onRestart()
    {
    	super.onRestart();
    	try {
			ClaimListController.loadFromFile(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    protected void onStop()
    {
    	super.onStop();
    	try {
			ClaimListController.saveToFile(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
   
    
    public void addClaim(View v)
    {
    	// move to Add Claim Activity
        // send code -1 to mark it as a new claim, rather than an existing one
    	Intent intent = new Intent(MainActivity.this, AddClaimActivity.class);
    	intent.putExtra("Claim Position", -1);
    	startActivity(intent);
    }
}

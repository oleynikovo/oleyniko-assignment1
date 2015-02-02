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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;

public class ClaimListController {
	// a singleton class, used entirely to refer to the global claims list
	// it also tries to do I/O operations with files, but apparently fails
	
	private static ArrayList<Claim> claimList = null;
	private static String fileName = "claimsList"; 
	
	static public ArrayList<Claim> getClaimList()
	{
		// get the global claims list
		if (claimList == null)
		{
			claimList = new ArrayList<Claim>();
		}
		return claimList;
	}
	
	// write/read the list to/from file
	// pretty sure they do not work
	// saving and loading copied on February 1, 2015 from
	// https://stackoverflow.com/questions/4118751/how-do-i-serialize-an-object-and-save-it-to-a-file-in-android
	public static void saveToFile(Context context) throws IOException
	{
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(claimList);
		os.close();
		fos.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void loadFromFile(Context context) throws IOException, ClassNotFoundException
	{
		FileInputStream fis = context.openFileInput(fileName);
		ObjectInputStream is = new ObjectInputStream(fis);
		claimList = (ArrayList<Claim>) is.readObject();
		is.close();
		fis.close();
	}
	
}

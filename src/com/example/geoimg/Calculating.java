package com.example.geoimg;

import java.util.List;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Calculating extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculating);
		Intent j = getIntent(); 
		System.out.println(j);
		double finalLongitude = Double.parseDouble(j.getStringExtra("finalLongitude"));
		double finalLatitude = Double.parseDouble(j.getStringExtra("finalLatitude"));
		String filePath = j.getStringExtra("filePath");
		DataStorage dataStorage = new DataStorage(this);
        List<DatabaseStorage> book2 = dataStorage.getAllBooks();
        Log.d("Size of data", book2.size()+"");
       
        for(int i=0;i<book2.size();i++){
        	double f = book2.get(i).getLatitude();
        	double g = book2.get(i).getLongitude();
        	if(Math.abs(g-finalLongitude)< 2 && Math.abs(f-finalLatitude)< 2){
        		String loc = book2.get(i).getAuthor();
        		//Toast.makeText(this, "The match gps data is" +
	            //	book2.get(i).getLatitude()+" ANd the image is in:"+book2.get(i).getAuthor(), Toast.LENGTH_LONG).show();
        		try {
        			float flag = ObjectFinder.main2(filePath, book2.get(i).getAuthor());
        			if(flag>0.03){
        				Toast.makeText(this, "The picture was a match:"+flag, Toast.LENGTH_LONG).show();
        				break;
        			}
        			else{
        				Toast.makeText(this, "The picture was NOT a match:"+flag + "+"+ i, Toast.LENGTH_LONG).show();
        				break;
        			}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	else{
        		Toast.makeText(this, "The gpa didnt match: database data=" + f+","+ g  
    	         	+"current data is"+finalLatitude+","+finalLongitude,
    	         	Toast.LENGTH_LONG).show();
        		break;
        		
        	}
        
        	
        	
        }
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculating, menu);
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
}

package com.example.geoimg;

import java.io.File;
import java.io.ObjectOutputStream.PutField;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class UnlockingCheck extends Activity {
	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	
	protected LocationManager locationManager;
	 public static final int MEDIA_TYPE_IMAGE = 1;
	protected Button retriveLocationButton;
	protected Button checkUnlock;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	//private static final String IMAGE_DIRECTORY_NAME = "OpenCV Demo";
	static String filePath=null;
	String locationInString=null;
	private static DataStorage dataStorage;
	double finalLongitude;
	double finalLatitude;
	
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d("kutt",filePath);
		
		
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            //Book book2 = dataStorage.getBook(filePath);
	        	//Toast.makeText(this, "The picture was a match nai thaaa", Toast.LENGTH_LONG).show();	
//	        	finish();
//	        	Intent j = new Intent(this, Calculating.class);
//				j.putExtra("finalLongitude",finalLongitude+"");
//				j.putExtra("finalLatitude",finalLatitude+"");
//				j.putExtra("filePath",filePath);
//				startActivity(j);
//}}
	  
	        	dataStorage = new DataStorage(this);
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
	            			float flag = (float)ObjectFinder.main2(filePath, book2.get(i).getAuthor());
	            			if(flag>0.04){
	            				Toast.makeText(this, "The picture was a match:"+flag, Toast.LENGTH_LONG).show();
	            				//window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	            			}
	            			else{
	            				Toast.makeText(this, "The picture was NOT a match:"+flag, Toast.LENGTH_LONG).show();		
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
	            		
	            	}
	            }
	            //Toast.makeText(this, "The gps data is" +
	            		//book2.get(0).getTitle()+" ANd the image is in:"+book2.get(0).getAuthor(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	        	
	            // User cancelled the image capture
	        //	Toast.makeText(this, "Image saved345345 to:\n",Toast.LENGTH_LONG).show();
	        } else {
	        	//Toast.makeText(this, "Image saved to:\n",Toast.LENGTH_LONG).show();
	            // Image capture failed, advise user
	        }
	        }
	
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unlocking_check);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    showCurrentLocation();
	    retriveLocationButton = (Button) findViewById(R.id.button1);
	   // retriveLocationButton.setOnClickListener(new OnClickListener() {
			//@Override
			//public void onClick(View v){
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
			    Log.d("file intent",fileUri.toString());
				showCurrentLocation();
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			//}	
		//});
	
	}
	
	private Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
}

private static File getOutputMediaFile(int type){
    // To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.

    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
              Environment.DIRECTORY_PICTURES), "MyCameraApp");
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.
    Log.d("Created dir", "create directory");
    // Create the storage directory if it does not exist
    if (! mediaStorageDir.exists()){
        if (! mediaStorageDir.mkdirs()){
            Log.d("MyCameraApp", "failed to create directory");
            
            return null;
        }
        else{
        	Log.d("inside mkdir", "did create directory");
        }
    }else{
    	Log.d("inside mkdir", "did ecxist directory");
    }

    // Create a media file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    File mediaFile;
    if (type == MEDIA_TYPE_IMAGE){
        Log.d("path to img",mediaStorageDir.getPath());
        filePath = (mediaStorageDir.getPath() + File.separator +
		        "referenceImg"+ timeStamp + ".jpg");
        mediaFile = new File(filePath);
        Log.d("Created img file", "create file");
    } else {
        return null;
    }

    return mediaFile;
}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unlocking_check, menu);
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
	
	protected String showCurrentLocation(){
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(location !=null){
			finalLongitude = location.getLongitude();
			finalLatitude = location.getLatitude();
			locationInString = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(),location.getLatitude());
			Toast.makeText(UnlockingCheck.this,locationInString,Toast.LENGTH_LONG).show();	
		}
		return locationInString;
	}

	private class MyLocationListner implements LocationListener {
		
		public void onLocationChanged(Location location){
			String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(),location.getLatitude());
			Toast.makeText(UnlockingCheck.this,message,Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			//Toast.makeText(MainActivity.this, "Provider status changed",Toast.LENGTH_LONG).show();
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		//	Toast.makeText(MainActivity.this, "Provider enabled by the user. GPS turned on",Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		//	Toast.makeText(MainActivity.this, "Provider disabled by the user. GPS turned off",Toast.LENGTH_LONG).show();
			
		}
		
	}
	
	
}

//The code to receive the GPS data has been adapted from http://www.javacodegeeks.com/2010/09/android-location-based-services.html


package com.example.geoimg;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	ToggleButton btnToggleLock;
	Button btnMisc;
	protected LocationManager locationManager;
	 public static final int MEDIA_TYPE_IMAGE = 1;
	protected Button retriveLocationButton;
	protected Button unlockButton;
	Toast toast;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	//private static final String IMAGE_DIRECTORY_NAME = "OpenCV Demo";
	static String filePath=null;
	String locationInString=null;
	double finalLongitude;
	double finalLatitude;
	private static DataStorage dataStorage;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.d("kutt",filePath);
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
//	            Toast.makeText(this, "Image saved to:\n" +
//	                    filePath, Toast.LENGTH_LONG).show();
	            DatabaseStorage book = new DatabaseStorage(finalLongitude,finalLatitude,filePath);
	            dataStorage = new DataStorage(this);
	            dataStorage.addBook(book);
	            //Book book2 = dataStorage.getBook(filePath);
	            List<DatabaseStorage> book2 = dataStorage.getAllBooks();
	            Toast.makeText(this, "The gps data is" +
	            		book2.get(0).getLatitude()+" ANd the image is in:"+book2.get(0).getAuthor(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	        	
	            // User cancelled the image capture
	        	Toast.makeText(this, "Image saved345345 to:\n",Toast.LENGTH_LONG).show();
	        } else {
	        	Toast.makeText(this, "Image saveddsvsv to22:\n",Toast.LENGTH_LONG).show();
	            // Image capture failed, advise user
	        }
	    }
	
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("chalu","aaaaa");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		retriveLocationButton = (Button) findViewById(R.id.retrive_location_button);
		unlockButton = (Button) findViewById(R.id.button1);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	//	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MINIMUM_TIME_BETWEEN_UPDATES,MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,new MyLocationListner());
		    
		unlockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				Intent i = new Intent(MainActivity.this,UnlockingCheck.class);
				startActivity(i);
			}	
		});
		

		retriveLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
			    Log.d("file intent",fileUri.toString());
				showCurrentLocation();
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}	
		});
		
		toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		btnToggleLock = (ToggleButton) findViewById(R.id.toggleButton1);
	    btnToggleLock.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {

	            if (btnToggleLock.isChecked()) {

	                toast.cancel();
	                toast.setText("Unlocked");
	                toast.show();

	                Log.i("Unlocked", "If");

	                Context context = getApplicationContext();
	                KeyguardManager _guard = (KeyguardManager) context
	                        .getSystemService(Context.KEYGUARD_SERVICE);
	                
					KeyguardLock _keyguardLock = _guard.newKeyguardLock("KeyguardLockWrapper");
	                _keyguardLock.disableKeyguard();

	                MainActivity.this.startService(new Intent(MainActivity.this, UpdateService.class));
	                _keyguardLock.reenableKeyguard();
	                

	            } else {

	                toast.cancel();
	                toast.setText("Locked");
	                toast.show();

	                Context context = getApplicationContext();
	                KeyguardManager _guard = (KeyguardManager) context
	                        .getSystemService(Context.KEYGUARD_SERVICE);
	                KeyguardLock _keyguardLock = _guard
	                        .newKeyguardLock("KeyguardLockWrapper");
	                _keyguardLock.reenableKeyguard();

	                Log.i("Locked", "else");

	                MainActivity.this.stopService(new Intent(MainActivity.this,
	                        UpdateService.class));

	            }

	        }
	    });
		
	
		
		
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
				        "IMG_"+ timeStamp + ".jpg");
		        mediaFile = new File(filePath);
		        Log.d("Created img file", "create file");
		    } else {
		        return null;
		    }

		    return mediaFile;
		}


		protected String showCurrentLocation(){
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if(location !=null){
				finalLatitude = location.getLatitude();
				finalLongitude = location.getLongitude();
				locationInString = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(),location.getLatitude());
				Toast.makeText(MainActivity.this,locationInString,Toast.LENGTH_LONG).show();	
			}
			return locationInString;
		}

		private class MyLocationListner implements LocationListener {
			
			public void onLocationChanged(Location location){
				String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(),location.getLatitude());
				Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
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
			
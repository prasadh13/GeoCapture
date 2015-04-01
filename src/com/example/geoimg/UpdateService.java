package com.example.geoimg;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UpdateService extends Service {

    BroadcastReceiver mReceiver;

@Override
public void onCreate() {
    super.onCreate();
    // register receiver that handles screen on and screen off logic
    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
    filter.addAction(Intent.ACTION_SCREEN_OFF);
    mReceiver = new MyReceiver();
    registerReceiver(mReceiver, filter);
}

@Override
public void onDestroy() {

    unregisterReceiver(mReceiver);
    Log.i("onDestroy Reciever", "Called");

    super.onDestroy();
}
Button unlockButton;
@Override
public void onStart(Intent intent, int startId) {
    boolean screenOn = intent.getBooleanExtra("screen_state", false);
    if (!screenOn) {
        Log.i("screenON", "Called");
       // Toast.makeText(getApplicationContext(), "Awake", Toast.LENGTH_LONG)
             //   .show();
        Intent dialogIntent = new Intent(getBaseContext(), UnlockingCheck.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
        
      //  Intent i = new Intent(getBaseContext(),UnlockingCheck.class);
			//	startActivity(i);
    } else {
        Log.i("screenOFF", "Called");
        // Toast.makeText(getApplicationContext(), "Sleep",
        // Toast.LENGTH_LONG)
        // .show();
    }
}

@Override
public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
}
}
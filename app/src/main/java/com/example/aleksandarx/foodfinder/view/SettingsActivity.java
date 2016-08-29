package com.example.aleksandarx.foodfinder.view;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.socket.SocketService;
import com.example.aleksandarx.foodfinder.sync.AlarmReceiver;
import com.example.aleksandarx.foodfinder.sync.BackgroundService;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Intent service;
    private HashMap<Boolean, String> serviceState;
    private Switch socketServiceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_more_people);

        socketServiceSwitch = (Switch) findViewById(R.id.socket_service_switch);

        serviceState = new HashMap<>();
        serviceState.put(true, "Service is turned on.");
        serviceState.put(false, "Service is turned off.");

        service = new Intent(SettingsActivity.this, SocketService.class);

        socketServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                socketServiceSwitch.setText(serviceState.get(isChecked));
                if(isChecked)
                    startService(service);
                else
                    stopService(service);
            }
        });

        boolean isChecked = false;
        if(isMyServiceRunning(SocketService.class))
            isChecked = true;

        socketServiceSwitch.setChecked(isChecked);
        socketServiceSwitch.setText(serviceState.get(isChecked));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //bindService(mIntent, mServerConn, BIND_AUTO_CREATE);
    }


    /*private ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("SERVICE", "onServiceConnected");
            mBounded = true;
            BackgroundService.LocalBinder mLocalBinder = (BackgroundService.LocalBinder) binder;
            mService = mLocalBinder.getServerInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("SERVICE", "onServiceDisconnected");
            mBounded = false;
            mService = null;
        }
    };*/

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

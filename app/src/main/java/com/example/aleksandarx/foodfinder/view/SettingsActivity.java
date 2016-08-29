package com.example.aleksandarx.foodfinder.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.socket.SocketService;
import com.example.aleksandarx.foodfinder.sync.AlarmReceiver;

public class SettingsActivity extends AppCompatActivity {


    private Context context;
    private Switch serviceSwitch;
    private TextView serviceStatus;
    private Switch socketServiceSwitch;
    private TextView socketServiceStatus;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_more_people);
        this.context = this;
        serviceSwitch = (Switch) findViewById(R.id.service_switch);

        serviceStatus = (TextView) findViewById(R.id.service_status);

        socketServiceSwitch = (Switch) findViewById(R.id.socket_service_switch);

        socketServiceStatus = (TextView) findViewById(R.id.socket_service_status);


        Intent alarm = new Intent(this.context, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this.context,0,alarm,PendingIntent.FLAG_NO_CREATE) != null);

        serviceSwitch.setChecked(alarmRunning);
        socketServiceSwitch.setChecked(SocketService.isRunning);

        pendingIntent = PendingIntent.getBroadcast(this.context,0,alarm,0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //


        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    serviceStatus.setText("Sync Service is turned on.");
                    alarmManager.setRepeating(alarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),15000,pendingIntent);
                }
                else
                {
                    serviceStatus.setText("Sync Service is turned off.");
                    alarmManager.cancel(pendingIntent);
                }
            }
        });

        socketServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    Intent intent = new Intent(SettingsActivity.this,
                            SocketService.class);
                    startService(intent);
                    serviceStatus.setText("Socket Service is turned on.");
                }
                else
                {
                    Intent intent = new Intent(SettingsActivity.this,
                            SocketService.class);
                    stopService(intent);
                    serviceStatus.setText("Socket Service is turned off.");
                }
            }
        });


    }
}

package com.example.aleksandarx.foodfinder.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Darko on 03.07.2016.
 */
public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }
    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            // do something here
            System.out.println("BACKGROUND SERVICE RUNNING");
            stopSelf();
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning)
        {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }
}

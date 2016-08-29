package com.example.aleksandarx.foodfinder.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.aleksandarx.foodfinder.network.HttpHelper;
import com.example.aleksandarx.foodfinder.share.UserPreferences;

/**
 * Created by Darko on 03.07.2016.
 */
public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private LocationController locationController;
    private IBinder mBinder = new LocalBinder();



    public class LocalBinder extends Binder {
        public BackgroundService getServerInstance() {
            return BackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        locationController = LocationController.getLocationController(context);
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            HttpHelper.getMyFood(UserPreferences.getPreference(context, UserPreferences.USER_ID), context);
            System.out.println("BACKGROUND SERVICE RUNNING");
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("BACKGROUND SERVICE IS DESTROY");
        this.isRunning = false;
        /*if(locationController != null)
            locationController.stopLocationController();*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationController.startLocationController();
        if(!isRunning)
        {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    public void startBackgroundService(){

    }

    public void setBackgroundService(){

    }
}

/*

protected ServiceConnection mServerConn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Log.d(LOG_TAG, "onServiceConnected");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(LOG_TAG, "onServiceDisconnected");
    }
}

 */

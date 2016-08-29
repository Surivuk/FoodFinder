package com.example.aleksandarx.foodfinder.sync;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aleksandarx on 8/28/16.
 */
public class LocationController implements LocationListener {

    private final Context context;
    private LocationManager locationManager;
    private boolean isRunning;

    private static LocationController INSTANCE_HOLDER = null;

    public static LocationController getLocationController(Context context){
        if(INSTANCE_HOLDER == null)
            INSTANCE_HOLDER = new LocationController(context);
        return INSTANCE_HOLDER;
    }

    private LocationController(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
    }

    public void startLocationController(){
        isRunning = true;
        System.out.println("startLocationController() isRunning: " + isRunning);
    }

    public void stopLocationController(){
        isRunning = false;
        System.out.println("stopLocationController() isRunning: " + isRunning);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isRunning)
            sendMessage(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void sendMessage(double latitude, double longitude) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("location-change");
        intent.putExtra("lat", latitude);
        intent.putExtra("lng", longitude);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}

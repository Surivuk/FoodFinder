package com.example.aleksandarx.foodfinder.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.network.HttpHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by aleksandarx on 6/17/16.
 */
public class MapClass implements OnMapReadyCallback {

    public boolean mapReady;
    private MainActivity activity;
    private GoogleMap map;
    private static final int GPS_TIME_INTERVAL = 10000; // get gps location every 1 min
    private static final int GPS_DISTANCE = 100; // set the distance value in meter
    private Handler guiThread;
    private MarkerOptions personsMarker = null;


    public MapClass(int mapId, MainActivity act){
        mapReady = false;
        activity = act;
        SupportMapFragment mapFragment = (SupportMapFragment) act.getSupportFragmentManager()
                .findFragmentById(mapId);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        map = googleMap;
        guiThread = new Handler();

        /*final LatLng sydney = new LatLng(43.322810, 21.895282);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Nis"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));*/
        map.getUiSettings().setCompassEnabled(true);





        /*if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(final Location location) {
                    LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

                    ExecutorService thread = Executors.newSingleThreadExecutor();
                    thread.submit(new Runnable() {
                        @Override
                        public void run() {
                            updateMapPlaces(HttpHelper.findPlacesAroundYou(location.getLatitude(),location.getLongitude()));
                        }
                    });

                    Toast.makeText(activity, "UPDATE", Toast.LENGTH_SHORT).show();
                    MarkerOptions m = new MarkerOptions().position(pos);
                    map.addMarker(m);

                }

                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                public void onProviderEnabled(String provider) {
                    Toast.makeText(activity, "START", Toast.LENGTH_SHORT).show();
                }

                public void onProviderDisabled(String provider) {
                    Toast.makeText(activity, "STOP", Toast.LENGTH_SHORT).show();
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, locationListener);


            if(personsMarker != null)
            {
                map.addMarker(personsMarker);
            }
        }*/

    }
    private void updateMapPlaces(final List<MarkerOptions> markers)
    {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,"Map updating,marker count: "+String.valueOf(markers.size()),Toast.LENGTH_LONG);
                for(int i=0; i < markers.size(); i++)
                {
                    map.addMarker(markers.get(i));
                }
            }
        });
    }
    public boolean addPersonMarker(String title,double lat,double lng)
    {
            personsMarker = new MarkerOptions();
            personsMarker.title(title);
            personsMarker.position(new LatLng(lat,lng));
            personsMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_custom_pin));
            return mapReady;
    }

    public void changeMyPin(LatLng latLng){
        map.addMarker(new MarkerOptions().position(latLng));
    }
}



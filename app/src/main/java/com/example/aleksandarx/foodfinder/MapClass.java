package com.example.aleksandarx.foodfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;

/**
 * Created by aleksandarx on 6/17/16.
 */
public class MapClass implements OnMapReadyCallback {

    private MainActivity activity;
    private GoogleMap map;

    public MapClass(int mapId, MainActivity act){
        activity = act;
        SupportMapFragment mapFragment = (SupportMapFragment) act.getSupportFragmentManager()
                .findFragmentById(mapId);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        /*final LatLng sydney = new LatLng(43.322810, 21.895282);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Nis"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));*/
        map.getUiSettings().setCompassEnabled(true);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            Toast.makeText(activity, "NIJE DOZVOLJENO", Toast.LENGTH_SHORT).show();
        }
    }

}

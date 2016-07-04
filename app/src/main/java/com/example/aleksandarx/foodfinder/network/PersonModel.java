package com.example.aleksandarx.foodfinder.network;

/**
 * Created by Darko on 03.07.2016.
 */
public class PersonModel {
    public String connectionID;
    public String foodType;
    public double latitude;
    public double longitude;
    public PersonModel(String cid,String ft,double lat,double lng)
    {
        connectionID = cid;
        foodType = ft;
        latitude = lat;
        longitude = lng;
    }

    @Override
    public String toString() {
        String json = "{";
        json += "\"connectionID\":\""+connectionID+"\"";
        json += ",\"foodType\":\""+foodType+"\"";
        json += ",\"latitude\":\""+String.valueOf(latitude)+"\"";
        json += ",\"longitude\":\""+String.valueOf(longitude)+"\"";
        json += "}";
        return json;
    }
}

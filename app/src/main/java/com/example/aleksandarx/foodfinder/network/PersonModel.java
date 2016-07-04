package com.example.aleksandarx.foodfinder.network;

/**
 * Created by Darko on 03.07.2016.
 */
public class PersonModel {
    public String connectionID;
    public String foodType;
    public PersonModel(String cid,String ft)
    {
        connectionID = cid;
        foodType = ft;
    }

    @Override
    public String toString() {
        String json = "{";
        json += "\"connectionID\":\""+connectionID+"\"";
        json += ",\"foodType\":\""+foodType+"\"";
        json += "}";
        return json;
    }
}

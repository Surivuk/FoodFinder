package com.example.aleksandarx.foodfinder.data.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.aleksandarx.foodfinder.data.model.FoodModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandarx on 7/2/16.
 */
public class FoodTable {

    private static final String TABLE_NAME = "FOOD_ARTICLE";
    private static final String ID = "ARTICLE_ID";
    private static final String ARTICLE_USER = "ARTICLE_USER";
    private static final String ARTICLE_LOCATION = "ARTICLE_LOCATION";
    private static final String ARTICLE_LOCATION_ID = "ARTICLE_LOCATION_ID";
    private static final String ARTICLE_LOCATION_NAME = "ARTICLE_LOCATION_NAME";
    private static final String ARTICLE_NAME = "ARTICLE_NAME";
    private static final String ARTICLE_DESCRIPTION = "ARTICLE_DESCRIPTION";
    private static final String MEAL_TYPE = "MEAL_TYPE";
    private static final String ARTICLE_ORIGIN = "ARTICLE_ORIGIN";
    private static final String ARTICLE_IMAGE = "ARTICLE_IMAGE";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FoodTable.TABLE_NAME + " (" +
                    FoodTable.ID + " INTEGER PRIMARY KEY," +
                    FoodTable.ARTICLE_USER + " INTEGER," +
                    FoodTable.ARTICLE_LOCATION_ID + " INTEGER," +
                    FoodTable.ARTICLE_LOCATION + " TEXT," +
                    FoodTable.ARTICLE_LOCATION_NAME + " TEXT," +
                    FoodTable.ARTICLE_NAME + " TEXT," +
                    FoodTable.ARTICLE_DESCRIPTION + " TEXT," +
                    FoodTable.MEAL_TYPE + " TEXT," +
                    FoodTable.ARTICLE_ORIGIN + " TEXT," +
                    FoodTable.ARTICLE_IMAGE + " TEXT" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FoodTable.TABLE_NAME;

    public FoodTable(){}

    public static String getTableName() { return TABLE_NAME; }

    public String sqlCreateEntrise(){ return SQL_CREATE_ENTRIES; }

    public String sqlDeleteEntrise(){ return SQL_DELETE_ENTRIES; }

    public ContentValues insertValue(FoodModel input){

        return null;
    }

    public String deleteValue(long id) {
        return this.ID + "=" + id;
    }


    public ContentValues updateValue(FoodModel value) {
        ContentValues val = new ContentValues();
        /*val.put(DRAWING_TYPE, "C");
        val.put(DRAWING_COORDINATES, AES256.encByte(circleToBson(model.getCircle())));
        if(model.getAccessFlag() != null)
            val.put(DRAWING_ACCESS_FLAG, model.getAccessFlag());
        if(model.getSyncFlag() != null)
            val.put(DRAWING_SYNC_FLAG, model.getSyncFlag());*/
        return val;
    }


    public String updateClaus(long id) {
        return this.ID + "=" + id;
    }


    public Object readOne(Cursor cursor, long id) {
        Object ret = null;
        if(cursor != null){
            while (cursor.moveToNext()){

            }
        }
        return ret;
    }

    public List<Object> readAll(Cursor cursor) {
        List<Object> ret = new ArrayList<>();
        if(cursor != null){
            while (cursor.moveToNext()) {

            }

        }
        return ret;
    }

    public String deleteAll(){
        return "DELETE FROM " + getTableName();
    }

}

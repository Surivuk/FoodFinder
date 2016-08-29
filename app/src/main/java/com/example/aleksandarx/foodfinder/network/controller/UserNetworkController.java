package com.example.aleksandarx.foodfinder.network.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.aleksandarx.foodfinder.view.logger.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by aleksandarx on 8/23/16.
 */
public class UserNetworkController {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private static OkHttpClient client = new OkHttpClient();


    public static JSONObject getProfileInfo(String url, String json) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        //System.out.println(response.body().string());

        return new JSONArray(response.body().string()).getJSONObject(0);
    }




    public static boolean signUpUser(JSONObject json, Uri imgUri, Context context) throws Exception {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", json.getString("email"))
                .addFormDataPart("password", json.getString("password"))
                .addFormDataPart("firstName", json.getString("firstName"))
                .addFormDataPart("lastName", json.getString("lastName"))
                .addFormDataPart("phone", json.getString("phone"))
                .addFormDataPart("imageFile", "bitmap.png", RequestBody.create(MEDIA_TYPE_PNG, new File(getRealPathFromURI(context, imgUri))))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("http://192.168.1.7:8081/signUp")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return true;
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);

            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}

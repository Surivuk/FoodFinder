package com.example.aleksandarx.foodfinder.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.network.controller.UserNetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        


        final TextView email = (TextView) findViewById(R.id.profile_view_username_view_text);
        final TextView password = (TextView) findViewById(R.id.profile_view_password_view_text);
        final TextView firstName = (TextView) findViewById(R.id.profile_view_first_name_view_text);
        final TextView lastName = (TextView) findViewById(R.id.profile_view_last_name_view_text);
        final TextView phone = (TextView) findViewById(R.id.profile_view_phone_number_view_text);
    }


    private class MyThread extends AsyncTask<Void, Void, Void>{

        boolean isOk = false;

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}

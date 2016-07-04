package com.example.aleksandarx.foodfinder.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.bluetooth.BluetoothMainActivity;
import com.example.aleksandarx.foodfinder.bluetooth.BluetoothModel;
import com.example.aleksandarx.foodfinder.share.UserPreferences;

public class MainActivity extends AppCompatActivity {

    private MapClass myMap;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        myMap = new MapClass(R.id.map, MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlacePickerActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.about_us_dialog, null))
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            case R.id.menu_places:
                Intent i = new Intent(MainActivity.this, FoodArticlesActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_sign_out:
                UserPreferences.removePreference(MainActivity.this, UserPreferences.USER_USERNAME);
                UserPreferences.removePreference(MainActivity.this, UserPreferences.USER_ID);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            case R.id.menu_bluetooth:
                Intent intent = new Intent(MainActivity.this,BluetoothMainActivity.class);
                intent.putExtra("food",new BluetoothModel(43.2,23.1,"Na cose","ItalianFood"));
                startActivity(intent);
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.example.aleksandarx.foodfinder.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.network.PersonModel;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketActivity extends AppCompatActivity {

    private static final int GPS_TIME_INTERVAL = 500; // get gps location every 1 min
    private static final int GPS_DISTANCE = 10; // set the distance value in meter
    private LatLng currentPosition;
    public boolean socketConnected;
    private Socket mSocket;
    private Spinner foodOrigin;
    private ListView peopleList;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<String> peopleAdapter;
    private Button joinGroupButton;
    private Button leaveGroupButton;
    private String connectionID;
    private String foodType;
    private ArrayList<String> foodTypes;
    private String liveServer = "http://food-finder-app.herokuapp.com/";
    //private String darkoServer = "http://192.168.1.15:8081";
    private HashMap<String,LatLng> positions;

    {
        try {
            mSocket = IO.socket(liveServer);
            socketConnected = true;
        } catch (URISyntaxException e)
        {
            mSocket = null;
            socketConnected = false;
        }
    }
    private Handler guiThread;
    private Context context;


    private Emitter.Listener onPlace = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONArray people = (JSONArray) args[0];

            ArrayList<String> peoplesName = new ArrayList<>();

            try {
                for(int i = 0 ; i < people.length(); i ++)
                {
                    JSONObject person = people.getJSONObject(i);
                    if(foodTypes.contains(person.getString("foodType")) && !connectionID.equals(person.get("connectionID")) && !peoplesName.contains(person.get("connectionID")))
                    {
                        peoplesName.add(person.getString("connectionID"));
                        LatLng position = new LatLng(person.getDouble("latitude"),person.getDouble("longitude"));

                        positions.put(person.getString("connectionID"),position);

                    }

                }

            } catch (JSONException e) {
                return;
            }
            guiNotifyUser(peoplesName);
        }
    };
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            connectionID = (String) args[0];
            guiToastNotify("Recieved id: "+connectionID);

        }
    };
    private void guiToastNotify(final String message)
    {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void guiNotifyUser(final ArrayList<String> peoplesName)
    {
        guiThread.post(new Runnable(){
            public void run(){
                Toast.makeText(context,"Showing interested people.",Toast.LENGTH_SHORT).show();
                peopleAdapter.clear();

                peopleAdapter.addAll(peoplesName);
                peopleAdapter.notifyDataSetInvalidated();

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_socket_test);
        guiThread = new Handler();
        foodTypes = new ArrayList<>();

        positions = new HashMap<>();

        currentPosition = new LatLng(0,0);

        if (ContextCompat.checkSelfPermission(SocketActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) SocketActivity.this.getSystemService(SocketActivity.this.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(final Location location) {
                    currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                    Toast.makeText(context,"Location changed.",Toast.LENGTH_SHORT).show();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                public void onProviderEnabled(String provider) {
                    Toast.makeText(SocketActivity.this, "START", Toast.LENGTH_SHORT).show();
                }

                public void onProviderDisabled(String provider) {
                    Toast.makeText(SocketActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, locationListener);
        }



        mSocket.connect();


        mSocket.on("message",onMessage);
        mSocket.on("foodFriends",onPlace);

        foodOrigin = (Spinner) findViewById(R.id.food_types);
        adapter = ArrayAdapter.createFromResource(this, R.array.food_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodOrigin.setAdapter(adapter);

        peopleList = (ListView) findViewById(R.id.people_listview);
        peopleAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        peopleList.setAdapter(peopleAdapter);
        peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String personsID = (peopleList.getItemAtPosition(position)).toString();

                //String personsID = (String) peopleList.getItemIdAtPosition(position);

                Intent i = new Intent(SocketActivity.this,MainActivity.class);
                i.putExtra("personsID",personsID);
                LatLng selectedPersonPosition = positions.get(personsID);
                i.putExtra("personsLat",selectedPersonPosition.latitude);
                i.putExtra("personsLng",selectedPersonPosition.longitude);

                startActivity(i);
                finish();
            }
        });

        leaveGroupButton = (Button) findViewById(R.id.leaveButton);
        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("leaveGroup",new PersonModel(connectionID,foodOrigin.getSelectedItem().toString(),currentPosition.latitude,currentPosition.longitude));

                foodTypes.remove(foodOrigin.getSelectedItem().toString());

                foodType = "";
                peopleAdapter.clear();
                peopleAdapter.notifyDataSetInvalidated();
            }
        });

        joinGroupButton = (Button) findViewById(R.id.joinGroup);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType = foodOrigin.getSelectedItem().toString();
                foodTypes.add(foodOrigin.getSelectedItem().toString());

                if(currentPosition == null)
                {
                    Toast.makeText(context,"Location unknown.Turn GPS on and wait for position lock.",Toast.LENGTH_SHORT).show();
                    return;
                }
                PersonModel data = new PersonModel(connectionID,foodType,currentPosition.latitude,currentPosition.longitude);
                System.out.println(data.toString());



                mSocket.emit("joinGroup", data , new Ack(){

                    @Override
                    public void call(Object... args) {
                        guiToastNotify("Joined group.");
                    }
                });


            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        //mSocket.off(foodType, onPlace);
        mSocket.off("foodFriends",onPlace);
        mSocket.off("message", onMessage);
    }
}

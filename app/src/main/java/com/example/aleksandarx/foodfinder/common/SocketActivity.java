package com.example.aleksandarx.foodfinder.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class SocketActivity extends AppCompatActivity {

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
    {
        try {
            mSocket = IO.socket("http://192.168.1.15:8081");
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
                    if(foodType.equals(person.getString("foodType")) && !connectionID.equals(person.get("connectionID")))
                    {
                        peoplesName.add(person.getString("connectionID"));
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

        leaveGroupButton = (Button) findViewById(R.id.leaveButton);
        leaveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("leaveGroup",new PersonModel(connectionID,foodType));
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
                PersonModel data = new PersonModel(connectionID,foodType);
                System.out.println(data.toString());

                /*mSocket.emit("joinGroup",data.toString(), new Ack(){

                    @Override
                    public void call(Object... args) {
                        guiToastNotify("Joined group.");
                    }
                });*/
                //mSocket.on(foodType,onPlace);

                mSocket.emit("joinGroup",data , new Ack(){

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
        mSocket.off("message", onMessage);
    }
}

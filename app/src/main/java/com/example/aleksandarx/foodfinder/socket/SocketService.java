package com.example.aleksandarx.foodfinder.socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import com.example.aleksandarx.foodfinder.network.PersonModel;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class SocketService extends Service {


    public static final String MY_ACTION = "MY_ACTION";
    public static final String MY_COMMAND = "test";
    private String liveServer = "http://192.168.1.15:8081/";//"https://food-finder-app.herokuapp.com/";

    public boolean socketConnected;
    private Socket mSocket;

    private MyThread worker_thread;
    public static boolean isRunning = false;
    private SocketReceiver receiver;
    public static String connectionID = "";


    private Emitter.Listener onPlace = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONArray people = (JSONArray) args[0];

            ArrayList<String> peoplesName = new ArrayList<>();

            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("type",0);
            intent.putExtra("people",people.toString());

            sendBroadcast(intent);
        }
    };
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            connectionID = (String) args[0];
//            guiToastNotify("Recieved id: "+connectionID);

            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("type",1);
            connectionID = (String) args[0];
            intent.putExtra("ID",(String) args[0]);

            sendBroadcast(intent);
        }
    };




    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if(!isRunning)
        {
            isRunning = true;

            receiver = new SocketReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getExtras();
                    int code = bundle.getInt("type");
                    PersonModel data = bundle.getParcelable("person");
                    // rewriting it because activity can get disposed and lose this.
                    data.connectionID = connectionID;
                    switch(code)
                    {
                        case 0:
                            //got person, should emit group leave.
                            mSocket.emit("leaveGroup",data);
                            break;
                        case 1:
                            //got person should emit group join
                            mSocket.emit("joinGroup",data);
                            break;

                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SocketService.MY_COMMAND);
            registerReceiver(receiver, intentFilter);

            //worker_thread = new MyThread();
            //worker_thread.start();

            try {
                mSocket = IO.socket(liveServer);
                socketConnected = true;
            } catch (URISyntaxException e)
            {
                mSocket = null;
                socketConnected = false;
            }


            mSocket.connect();


            mSocket.on("message",onMessage);
            mSocket.on("foodFriends",onPlace);

        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy()
    {

        unregisterReceiver(receiver);
        isRunning = false;
        mSocket.disconnect();

        mSocket.off("foodFriends",onPlace);
        mSocket.off("message", onMessage);


    }
    public class MyThread extends Thread{

        @Override
        public void run() {
            int i = 0;
            // TODO Auto-generated method stub
            while(isRunning){
                try {
                    Thread.sleep(5000);
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);

                    intent.putExtra("DATAPASSED", i);

                    sendBroadcast(intent);
                    i++;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //stopSelf();
        }

    }

}

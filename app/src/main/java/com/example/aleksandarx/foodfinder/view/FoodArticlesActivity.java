package com.example.aleksandarx.foodfinder.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.data.model.FoodModel;
import com.example.aleksandarx.foodfinder.data.sqlite.DBAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodArticlesActivity extends AppCompatActivity {

    private List<FoodModel> listValues;
    //private TextView text;
    private ListView listV;
    private Handler guiThread;
    private DBAdapter db;
    private TextView defaultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getOverflowMenu();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_articles);
        guiThread = new Handler();
        listV = (ListView) findViewById(R.id.myList);
        defaultText = (TextView) findViewById(R.id.empty_list_placeholder);
        db = DBAdapter.createAdapter(FoodArticlesActivity.this);

        ExecutorService thread = Executors.newSingleThreadExecutor();
        thread.submit(new Runnable() {
            @Override
            public void run() {
                db.open();
                updateGUI(db.readAll());
                db.close();
            }
        });
    }

    private void updateGUI(final List<Object> list){
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                listValues = new ArrayList<>();
                for(int i = 0; i < list.size(); i++){
                    FoodModel model = (FoodModel) list.get(i);
                    listValues.add(model);
                }
                ArrayAdapter<FoodModel> myAdapter = new ArrayAdapter <>(FoodArticlesActivity.this, R.layout.row_layout, R.id.listText, listValues);
                listV.setAdapter(myAdapter);
                if(listV.getAdapter().getCount() > 0)
                    defaultText.setVisibility(View.INVISIBLE);

                listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(FoodArticlesActivity.this, listValues.get(position).toString(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FoodArticlesActivity.this, FoodViewActivity.class);
                        FoodModel model = (FoodModel) parent.getAdapter().getItem(position);
                        i.putExtra("id", model.getArticle_id());
                        startActivity(i);
                    }
                });
            }
        });
    }

    private void guiNotifyUser(final String message)
    {
        guiThread.post(new Runnable(){
            public void run(){
                Toast.makeText(FoodArticlesActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_food:
                Intent intent = new Intent(FoodArticlesActivity.this, CreateFoodActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.example.aleksandarx.foodfinder.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.share.UserPreferences;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FoodArticlesActivity extends AppCompatActivity {

    private List<String> listValues;
    //private TextView text;
    private ListView listV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getOverflowMenu();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_articles);

        listV = (ListView) findViewById(R.id.myList);
        TextView defaultText = (TextView) findViewById(R.id.empty_list_placeholder);

        /*listV.setA*/

        listValues = new ArrayList<>();
        /*listValues.add("Item1");
        listValues.add("Item2");
        listValues.add("Item3");
        listValues.add("Item4");
        listValues.add("Item5");
        listValues.add("Item6");
        listValues.add("Item7");*/

        //text = (TextView) findViewById(R.id.mainText);

        ArrayAdapter<String> myAdapter = new ArrayAdapter <>(this, R.layout.row_layout, R.id.listText, listValues);

        // Bind to our new adapter.
        listV.setAdapter(myAdapter);

        if(listV.getAdapter().getCount() > 0)
            defaultText.setVisibility(View.INVISIBLE);

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FoodArticlesActivity.this, listValues.get(position), Toast.LENGTH_SHORT).show();
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

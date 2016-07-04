package com.example.aleksandarx.foodfinder.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.aleksandarx.foodfinder.R;

public class FoodViewActivity extends AppCompatActivity {


    private TextView articleName;
    private TextView articleFoodType;
    private TextView articleOrigin;
    private TextView articleMealType;
    private TextView articleLocation;
    private TextView articleLocationAddress;
    private TextView articleDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);


        articleName = (TextView) findViewById(R.id.article_name_holder);
        articleFoodType = (TextView) findViewById(R.id.article_food_type);
        articleOrigin = (TextView) findViewById(R.id.article_origin);
        articleMealType = (TextView) findViewById(R.id.article_meal_type);
        articleLocation = (TextView) findViewById(R.id.article_location);
        articleLocationAddress = (TextView) findViewById(R.id.article_location_address);
        articleDescription = (TextView) findViewById(R.id.article_description);




    }
}

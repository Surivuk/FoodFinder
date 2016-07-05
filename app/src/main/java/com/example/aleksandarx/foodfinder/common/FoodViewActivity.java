package com.example.aleksandarx.foodfinder.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.aleksandarx.foodfinder.R;
import com.example.aleksandarx.foodfinder.data.model.FoodModel;
import com.example.aleksandarx.foodfinder.data.sqlite.DBAdapter;

public class FoodViewActivity extends AppCompatActivity {


    private TextView articleName;
    private TextView articleFoodType;
    private TextView articleOrigin;
    private TextView articleMealType;
    private TextView articleLocation;
    private TextView articleLocationAddress;
    private TextView articleDescription;
    private WebView img;
    private DBAdapter dbAdapter;


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
        img = (WebView) findViewById(R.id.webView);

        dbAdapter = DBAdapter.createAdapter(FoodViewActivity.this);


        Intent invokingIntent = getIntent();
        Bundle personBundle = invokingIntent.getExtras();
        long id = 0;
        if(personBundle != null){
            id = personBundle.getLong("id");
        }

        dbAdapter.open();
        FoodModel model = (FoodModel) dbAdapter.read(id);
        dbAdapter.close();

        //WebView web = new WebView(this);
        img.setPadding(50, 0, 0, 0);
        img.setInitialScale(40);
        img.setBackgroundColor(Color.BLACK);

        img.loadUrl(model.getArticle_image());
        articleName.setText(model.getItem("articleName"));
        articleDescription.setText(model.getItem("articleDescription"));
        articleOrigin.setText(model.getItem("origin"));
        articleFoodType.setText(model.getItem("foodType"));
        articleMealType.setText(model.getItem("mealType"));
        articleLocation.setText(model.getItem("locationName"));
        articleLocationAddress.setText(model.getItem("locationAddress"));

    }
}

package com.app.wegatyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DateSpotsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_spots);

        ImageButton Map = findViewById(R.id.map_button);
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickmap();
            }
        });

        ImageButton Message = findViewById(R.id.message_button);
        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickmessage();
            }
        });

        ImageButton Settings = findViewById(R.id.settings_button);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicksettings();
            }
        });

        ImageButton Home = findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickhome();
            }
        });
    }

    public void clickhome(){
        Intent intent = new Intent(this, SwipeActivity.class);
        startActivity(intent);
    }

    public void clickmap(){
        Intent intent = new Intent(this, DateSpotsActivity.class);
        startActivity(intent);
    }

    public void clickmessage(){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    public void clicksettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
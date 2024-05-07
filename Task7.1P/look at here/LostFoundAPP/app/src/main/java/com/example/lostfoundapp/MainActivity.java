package com.example.lostfoundapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAdvert, btnListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnListItems = findViewById(R.id.btnListItems);

        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAdvertActivity.class));
            }
        });

        btnListItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListItemsActivity.class));
            }
        });
    }
}
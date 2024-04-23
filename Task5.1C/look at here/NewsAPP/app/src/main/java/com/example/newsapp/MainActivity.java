package com.example.newsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load NewsListFragment in MainActivity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new NewsListFragment())
                .commit();
    }
}

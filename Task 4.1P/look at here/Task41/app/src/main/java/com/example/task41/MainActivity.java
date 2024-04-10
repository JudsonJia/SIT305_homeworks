package com.example.task41;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 设置底部导航栏的点击事件监听器
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.action_create_task) {
                startActivity(new Intent(MainActivity.this, CreateTaskActivity.class));
                return true;
            } else if (id == R.id.action_task_list) {
                startActivity(new Intent(MainActivity.this, TaskListActivity.class));
                return true;
            }
            return false;
        });
    }

}




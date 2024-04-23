package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private EditText editTextURL;
    private Button buttonPlay, buttonAddToPlaylist, buttonMyPlaylist;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Get the username that was passed through
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
        }

        editTextURL = findViewById(R.id.editTextURL);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonAddToPlaylist = findViewById(R.id.buttonAddToPlaylist);
        buttonMyPlaylist = findViewById(R.id.buttonMyPlaylist);

        // Add the logic for the button click event here
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理播放按钮点击事件
                String url = editTextURL.getText().toString().trim();
                if (!url.isEmpty()) {
                    // 实现播放逻辑
                    Intent intent = new Intent(PlayerActivity.this, YouTubePlayer.class);
                    intent.putExtra("videoUrl", url);
                    startActivity(intent);
                } else {
                    Toast.makeText(PlayerActivity.this, "Please enter the video URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理添加到播放列表按钮点击事件
                String url = editTextURL.getText().toString().trim();
                if (!url.isEmpty()) {
                    // 实现添加到播放列表逻辑
                    DatabaseHelper dbHelper = new DatabaseHelper(PlayerActivity.this);
                    dbHelper.addUrlToPlaylist(username, url);
                    Toast.makeText(PlayerActivity.this, "The video URL has been added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlayerActivity.this, "Please enter the video URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonMyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里处理查询我的播放列表按钮点击事件
                // 实现查询播放列表逻辑
                Intent intent = new Intent(PlayerActivity.this, PlaylistActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
}

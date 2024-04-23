package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    private ListView listViewPlaylist;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> playlist;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // Get the username that was passed through
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
        }

        // Initialize the list view and adapter
        listViewPlaylist = findViewById(R.id.listViewPlaylist);
        playlist = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playlist);
        listViewPlaylist.setAdapter(adapter);

        // Retrieve playlist data from the database
        retrievePlaylistFromDatabase();

        listViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the URL of the video that the user clicked on
                String videoUrl = playlist.get(position);

                // Create an Intent to launch the next view and pass the video URL as Extra data
                Intent intent = new Intent(PlaylistActivity.this, YouTubePlayer.class);
                intent.putExtra("videoUrl", videoUrl);
                startActivity(intent);
            }
        });
    }

    private void retrievePlaylistFromDatabase() {
        // Create an instance of the database helper class
        DatabaseHelper dbHelper = new DatabaseHelper(PlaylistActivity.this);

        // Get playlist data from the database
        playlist = dbHelper.retrievePlaylist(username);

        // Update the adapter to reflect the new playlist data
        adapter.clear();
        adapter.addAll(playlist);
        adapter.notifyDataSetChanged();
    }
}

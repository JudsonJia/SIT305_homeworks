package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Quiz_initial extends AppCompatActivity {
    private String Username;
    private String description;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_initial);

        Intent intent = getIntent();
        if (intent != null) {
            Username = intent.getStringExtra("username");
        }

        TextView username = findViewById(R.id.username_required);
        ImageButton start = findViewById(R.id.Continue);
        TextView Description = findViewById(R.id.taskDescription);

        DBHelper dbHelper = new DBHelper(this);
        List<String> Interests = dbHelper.getUserInterests(Username);
        int index = (int) (Math.random() * Interests.size());
        description = Interests.get(index);

        username.setText(Username);
        Description.setText(String.format("Click the following button to get into the quiz of %s", description));
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Quiz_initial.this, Quiz.class);
                intent.putExtra("description", description);
                startActivity(intent);
            }

        });
    }
}

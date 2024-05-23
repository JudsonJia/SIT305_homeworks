package com.example.languagelearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LearningSelection extends AppCompatActivity {

    // Declare UI elements
    private TextView welcome;
    private EditText languageInput;
    private EditText topicInput;
    private Button buttonQuiz;
    private Button buttonVocabulary;
    private Button buttonReading;
    private Button buttonWriting;
    private Button buttonTranslation;
    private Button buttonChat;

    // Store the username
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_selection);

        // Initialize UI elements
        languageInput = findViewById(R.id.language_input);
        topicInput = findViewById(R.id.topic_input);
        buttonQuiz = findViewById(R.id.buttonQuiz);
        buttonVocabulary = findViewById(R.id.buttonVocabulary);
        buttonReading = findViewById(R.id.buttonReading);
        buttonWriting = findViewById(R.id.buttonWriting);
        buttonTranslation = findViewById(R.id.buttonTranslation);
        buttonChat = findViewById(R.id.buttonChat);
        welcome = findViewById(R.id.welcome_sentence);

        // Get the username from the intent
        username = (String) getIntent().getExtras().get("username");

        // Set the welcome message with the username
        welcome.setText(String.format("Hi, %s", username));

        // Set click listeners for the buttons
        buttonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageInput.getText().toString();
                String topic = topicInput.getText().toString();
                if (validateInputs(language, topic)) {
                    Intent intent = new Intent(LearningSelection.this, QuizActivity.class);
                    intent.putExtra("language", language);
                    intent.putExtra("topic", topic);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

        buttonVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageInput.getText().toString();
                String topic = topicInput.getText().toString();
                if (validateInputs(language, topic)) {
                    Intent intent = new Intent(LearningSelection.this, VocabularyActivity.class);
                    intent.putExtra("language", language);
                    intent.putExtra("topic", topic);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

        buttonReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageInput.getText().toString();
                String topic = topicInput.getText().toString();
                if (validateInputs(language, topic)) {
                    Intent intent = new Intent(LearningSelection.this, ReadingActivity.class);
                    intent.putExtra("language", language);
                    intent.putExtra("topic", topic);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

        buttonWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageInput.getText().toString();
                String topic = topicInput.getText().toString();
                if (validateInputs(language, topic)) {
                    Intent intent = new Intent(LearningSelection.this, WritingActivity.class);
                    intent.putExtra("language", language);
                    intent.putExtra("topic", topic);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

        buttonTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearningSelection.this, TranslationActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageInput.getText().toString();
                if (!language.isEmpty()) {
                    Intent intent = new Intent(LearningSelection.this, ChatActivity.class);
                    intent.putExtra("language", language);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(LearningSelection.this, "Please enter the language you want to learn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validate the language and topic inputs
    private boolean validateInputs(String language, String topic) {
        if (language.isEmpty()) {
            Toast.makeText(this, "Please enter the language you want to learn", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (topic.isEmpty()) {
            Toast.makeText(this, "Please enter the topic you want to learn", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
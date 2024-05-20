package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InterestSelectionActivity extends AppCompatActivity {

    private CheckBox checkBoxTechnology, checkBoxSports, checkBoxMusic, checkBoxArt, checkBoxAlgorithm, checkBoxDataStructure, checkBoxWebDevelopment, checkBoxAI, checkBoxTesting;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selection);

        checkBoxTechnology = findViewById(R.id.checkBoxTechnology);
        checkBoxSports = findViewById(R.id.checkBoxSports);
        checkBoxMusic = findViewById(R.id.checkBoxMusic);
        checkBoxArt = findViewById(R.id.checkBoxArt);
        checkBoxAlgorithm = findViewById(R.id.checkBoxAlgorithms);
        checkBoxWebDevelopment = findViewById(R.id.checkBoxWebDevelopment);
        checkBoxAI = findViewById(R.id.checkBoxAI);
        checkBoxDataStructure = findViewById(R.id.checkBoxDataStructure);
        checkBoxTesting = findViewById(R.id.checkBoxTesting);
        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedInterests = new ArrayList<>();

                if (checkBoxTechnology.isChecked()) {
                    selectedInterests.add("Technology");

                }
                if (checkBoxSports.isChecked()) {
                    selectedInterests.add("Sports");
                }
                if (checkBoxMusic.isChecked()) {
                    selectedInterests.add("Music");
                }
                if (checkBoxArt.isChecked()) {
                    selectedInterests.add("Art");
                }
                if (checkBoxAlgorithm.isChecked()) {
                    selectedInterests.add("Algorithm");
                }
                if (checkBoxAI.isChecked()) {
                    selectedInterests.add("AI");
                }
                if (checkBoxTesting.isChecked()) {
                    selectedInterests.add("Testing");
                }
                if (checkBoxDataStructure.isChecked()) {
                    selectedInterests.add("Data Structure");
                }
                if (checkBoxWebDevelopment.isChecked()){
                    selectedInterests.add("Web Development");
                }

                String username = (String) Objects.requireNonNull(getIntent().getExtras()).get("username");

                DBHelper dbHelper = new DBHelper(InterestSelectionActivity.this);

                // Insert user interests into the database
                for (String interest : selectedInterests) {
                    dbHelper.insertUserInterest(username, interest);
                }

                // Close the database helper
                dbHelper.close();

                Intent intent = new Intent(InterestSelectionActivity.this, Quiz_initial.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();


            }
        });
    }
}

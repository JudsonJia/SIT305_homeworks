package com.example.lostfoundapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CreateAdvertActivity extends AppCompatActivity {

    private EditText etName, etDescription, etDate, etLocation;

    private RadioButton lostButton, foundButton;
    private RadioGroup radioGroup;
    private Button btnSubmit;

    private Item item;

    private String itemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        lostButton = findViewById(R.id.Lost);
        foundButton = findViewById(R.id.Found);
        radioGroup = findViewById(R.id.selection_type);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        btnSubmit = findViewById(R.id.btnSubmit);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.Lost){
                    itemType = "Lost";
                }else if(checkedId == R.id.Found){
                    itemType = "Found";
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAdvert(itemType);
            }
        });
    }

    private void submitAdvert(String itemType) {

        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        item = new Item(itemType, name, description, date, location);

        ItemDbHelper dbHelper = new ItemDbHelper(this);
        boolean isSuccessful = dbHelper.insertAdvert(item, dbHelper);
        if (isSuccessful) {
            Toast.makeText(CreateAdvertActivity.this, "Advert submitted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CreateAdvertActivity.this, "Failed to submit advert.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
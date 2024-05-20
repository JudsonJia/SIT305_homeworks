package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.userName);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jump to the registration screen
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Invoke the methods in the database to verify the username and password
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        boolean loginSuccessful = dbHelper.checkUser(username, password);

        if (loginSuccessful) {
            // Successful login redirects to another page
            Intent intent = new Intent(MainActivity.this, Quiz_initial.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish(); // Close the current page
        } else {
            // Login fails with an error message
            Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

}
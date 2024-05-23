package com.example.languagelearning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private EditText confirmPasswordEditText;
    private EditText confirmEmailEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.buttonRegister);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        confirmEmailEditText = findViewById(R.id.confirmEmail);

        registerButton.setOnClickListener(view -> registerUser());
    }

    // Method to handle user registration
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String confirmEmail = confirmEmailEditText.getText().toString().trim();

        // Check if any field is empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmEmail.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }

        // Check if password and confirmation password, and email and confirmation email match
        if (password.equals(confirmPassword) && email.equals(confirmEmail)) {
            // Invoke a method in the database to insert new user information
            boolean registrationSuccessful;
            try (userDBHelper dbHelper = new userDBHelper(RegisterActivity.this)) {
                registrationSuccessful = dbHelper.addUser(username, email, password);
            }
            if (registrationSuccessful) {
                // The registration is successful, a success message is displayed and the registration page is closed
                Toast.makeText(RegisterActivity.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LearningSelection.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else {
                // Registration failed, display error message
                Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "The password and confirmation password or email and confirmation email do not match", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.itube;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextUsername, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Here, the registration logic is implemented, such as verifying that the input is valid, that the password matches, etc
        // Check if the input is empty
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // If any of the input fields are empty, an error message is displayed
            Toast.makeText(RegisterActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return; // Termination of the registration process
        }
        // If the password and confirmation password match, the registration is successful
        if (password.equals(confirmPassword)) {
            // Invoke a method in the database to insert new user information
            DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);
            boolean registrationSuccessful = dbHelper.addUser(fullName, username, password);

            if (registrationSuccessful) {
                // The registration is successful, a success message is displayed and the registration page is closed
                Toast.makeText(RegisterActivity.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                finish(); // 关闭当前页面
            } else {
                // 注册失败，显示错误消息
                Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "The password and confirmation password do not match", Toast.LENGTH_SHORT).show();
        }

    }
}
package com.example.parking_ticket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class testsharedpreference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testsharedpreference);


        // Retrieve username and password from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("username", "");
        String storedPassword = sharedPreferences.getString("password", "");

        // Set the retrieved values to the EditText fields
        EditText usernameEdit = findViewById(R.id.editTextText);
        usernameEdit.setText(storedUsername);

        EditText passwordEdit = findViewById(R.id.editTextText2);
        passwordEdit.setText(storedPassword);

        // Rest of your onCreate code...
    }
}
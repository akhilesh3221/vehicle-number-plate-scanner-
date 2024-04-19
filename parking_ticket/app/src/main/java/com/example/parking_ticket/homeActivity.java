package com.example.parking_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class homeActivity extends AppCompatActivity {
    String put="hello";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_ocr = new Intent(homeActivity.this, ocrActivity.class);

                startActivity(intent_ocr);
            }

        });
    }

}

package com.example.parking_ticket;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class displayActivity extends AppCompatActivity {

    TextView textView, textView2; // Declare the TextView
    String inputtext2;
    String inputtext;
    Integer userid;
    Integer branchid;
    String voucherserial;
    String vehicletype;
    private TextView responseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        responseTV = findViewById(R.id.idTVResponse);

        // Initialize the TextView inside the onCreate method
        textView = findViewById(R.id.vehiclenum);

        Intent intent = getIntent();
        inputtext = intent.getStringExtra("vehicleNumber");
        textView.setText(inputtext);

        textView2 = findViewById(R.id.vehiclerate);

        RadioGroup radioGroup = findViewById(R.id.radiogroup1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonbike) {
                    inputtext2 = "10";
                    vehicletype = "Bike";
                } else if (checkedId == R.id.radioButtoncar) {
                    inputtext2 = "20";
                    vehicletype = "Car";
                } else if (checkedId == R.id.radioButtonauto) {
                    inputtext2 = "30";
                    vehicletype = "Auto";
                } else if (checkedId == R.id.radioButtoncommercial) {
                    inputtext2 = "40";
                    vehicletype = "Commercial";
                } else if (checkedId == R.id.radioButtontruck) {
                    inputtext2 = "50";
                    vehicletype = "Truck";
                } else {
                    inputtext2 = "0";
                    vehicletype = "None";
                }
                textView2.setText(inputtext2);
            }

        });

        findViewById(R.id.buttonSaveRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line checking if the vehicle type is empty or not.
                if (vehicletype == null || vehicletype.isEmpty()) {
                    Toast.makeText(displayActivity.this, "Please select a vehicle type..", Toast.LENGTH_SHORT).show();
                } else {
                    // on below line calling method to post the data.
                    postDataUsingVolley(inputtext, vehicletype);
                }
            }
        });
    }

    private void postDataUsingVolley(String inputtext, String vehicletype) {
        // on below line specifying the URL at which we have to make a post request
        String url = "https://dayas.in/webparking/";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(displayActivity.this);
        // Accessing SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_NAME", Context.MODE_PRIVATE);

        // Retrieving data from SharedPreferences
        userid = sharedPreferences.getInt("userid",0);
        branchid = sharedPreferences.getInt("branchid",0);
        voucherserial = sharedPreferences.getString("voucherserial", "");

        // creating a JSONObject for the POST request
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("VehicleNo", inputtext);
            jsonParams.put("VehicleType", vehicletype);
            jsonParams.put("Price", inputtext2);
            jsonParams.put("userid", userid);
            jsonParams.put("branchid", branchid);
            jsonParams.put("voucherserial", voucherserial);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // making a JSON object request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // setting response to text view.
                responseTV.setText("Response from the API is: " + response.toString());
                // displaying toast message.
                Toast.makeText(displayActivity.this, "Data posted successfully..", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handling error on below line.
                responseTV.setText(error.getMessage());
                Toast.makeText(displayActivity.this, "Fail to get response..", Toast.LENGTH_SHORT).show();
            }
        });

        // adding request to the queue to post the data.
        queue.add(request);
    }


}

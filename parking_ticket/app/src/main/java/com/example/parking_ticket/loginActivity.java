package com.example.parking_ticket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class loginActivity extends AppCompatActivity {

    private TextView responseTV;
    private ProgressBar progressBar;
    private String responseFromPhp = "";
    private String username = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        responseTV = findViewById(R.id.idTVResponse);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText uname = findViewById(R.id.username);
                username = uname.getText().toString();
                EditText pass = findViewById(R.id.password);
                password = pass.getText().toString();

                // on below line checking if the vehicle type is empty or not.
                if (username == null || password.isEmpty()) {
                    Toast.makeText(loginActivity.this, "Please enter both username and password..", Toast.LENGTH_SHORT).show();
                } else {
                    // on below line calling method to post the data.
                    postDataUsingVolley(username, password);
                    // Removed AsyncTask as it's not needed.
                }
            }
        });

        findViewById(R.id.buttontologin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(loginActivity.this, homeActivity.class);
                startActivity(intent_login);
            }
        });
    }

    private void postDataUsingVolley(String username, String password) {
        // on below line specifying the URL at which we have to make a post request
        String url = "https://dayas.in/webparking/loginpro.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(loginActivity.this);

        // creating a JSONObject for the POST request
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", username);
            jsonParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // making a JSON object request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extracting values from the JSON response
                    String status = response.getString("status");
                    int userlevelid = response.getInt("userlevelid");
                    int userid = response.getInt("userid");
                    String userlevel = response.getString("userlevel");
                    String username = response.getString("username");
                    String fullname = response.getString("fullname");
                    String email = response.getString("email");
                    int branchid = response.getInt("branchid");
                    String branch = response.getString("branch");
                    String branchname = response.getString("branchname");
                    String address = response.getString("address");
                    String phone = response.getString("phone");
                    String tin = response.getString("tin");
                    String taxtype = response.getString("taxtype");
                    String billpaper = response.getString("billpaper");
                    String userdate = response.getString("userdate");
                    int accountid = response.getInt("accountid");
                    String voucherserial = response.getString("voucherserial");

                    // Save extracted values to SharedPreferences
                    saveDataToSharedPreferences(
                            userlevelid, userid, userlevel, username,
                            fullname, email, branchid, branch,
                            branchname, address, phone, tin,
                            taxtype, billpaper, userdate,
                            accountid, voucherserial
                    );

                    // Displaying toast message.
                    Toast.makeText(loginActivity.this, "Data posted successfully..", Toast.LENGTH_SHORT).show();
                    Intent intent_login = new Intent(loginActivity.this, homeActivity.class);
                    startActivity(intent_login);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    Toast.makeText(loginActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handling error on below line.
                responseTV.setText(error.getMessage());
                Toast.makeText(loginActivity.this, "Fail to get response..", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    // Try to parse the response as JSON
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    // If it's not a JSON object, assume it's a string and return it as such
                    return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
                }
            }
        };

// adding request to the queue to post the data.
        queue.add(request);
    }

    private void saveDataToSharedPreferences(
            int userlevelid, int userid, String userlevel, String username,
            String fullname, String email, int branchid, String branch,
            String branchname, String address, String phone, String tin,
            String taxtype, String billpaper, String userdate,
            int accountid, String voucherserial) {

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("userlevelid", userlevelid);
        editor.putInt("userid", userid);
        editor.putString("userlevel", userlevel);
        editor.putString("username", username);
        editor.putString("fullname", fullname);
        editor.putString("email", email);
        editor.putInt("branchid", branchid);
        editor.putString("branch", branch);
        editor.putString("branchname", branchname);
        editor.putString("address", address);
        editor.putString("phone", phone);
        editor.putString("tin", tin);
        editor.putString("taxtype", taxtype);
        editor.putString("billpaper", billpaper);
        editor.putString("userdate", userdate);
        editor.putInt("accountid", accountid);
        editor.putString("voucherserial", voucherserial);

        editor.apply();
    }

}

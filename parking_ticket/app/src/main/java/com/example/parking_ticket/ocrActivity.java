package com.example.parking_ticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ocrActivity extends AppCompatActivity {

    static EditText et1;


    TextView tvname, tvnameshow, tvphone, tvphoneShow;
    ProgressDialog progressDialog;
    String myChildphone;
    String myChildname;
    String number;
    int flag = 0;
    String dataToSend = "Hello from SourceActivity!";
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, 1);
        }

        SurfaceView cameraPreview = findViewById(R.id.cameraPreview);
        startCameraSource(cameraPreview);
    }

    private void startCameraSource(SurfaceView cameraPreview) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

        if (!textRecognizer.isOperational()) {
            // Handle the error
        } else {
            cameraSource = new CameraSource.Builder(this, textRecognizer)
                    .setAutoFocusEnabled(true)
                    .setRequestedPreviewSize(1600, 1024)
                    .build();

            cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(ocrActivity.this,
                                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            cameraSource.start(holder);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                    // Implement release logic if needed
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    String input7="";
                    SparseArray<TextBlock> items = detections.getDetectedItems();
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue()).append("\n");
                    }

                    // Process the recognized text as needed
                    String recognizedText = stringBuilder.toString().trim();
                    // Check if the recognized text matches a vehicle number pattern
                    // Create an AlertDialog.Builder
                    input7=isVehicleNumberPlate(recognizedText);




//                    if (isVehicleNumberPlate(recognizedText)) {
//                       System.out.println("recognizedText");
//                    }
                }
            });
        }
    }

    private String isVehicleNumberPlate(String text) {
        String final1;
        // Implement logic to determine if the recognized text is a vehicle number plate
        // You might use regular expressions, machine learning models, or other techniques

        String cleanedString = removeSpecialCharsAndExtraSpaces(text);
        final1 = matchPattern(cleanedString);

        return final1;
    }

    private String matchPattern(String input) {
        // Define the regular expression pattern
        String regex = "[A-Za-z]{2}\\d{2}[A-Za-z]{1,2}\\d{4}";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);
        String matchedSubstring = "";

        // Find and print all matches
        while (matcher.find()) {
            matchedSubstring = matcher.group();
        }

        if (!matchedSubstring.isEmpty()) {
            // passing result via intent
            Intent intent1 = new Intent(this, displayActivity.class);
            intent1.putExtra("vehicleNumber", matchedSubstring); // Use a meaningful key
            startActivity(intent1);
            return matchedSubstring;
        } else {
            Toast.makeText(ocrActivity.this, "no match found", Toast.LENGTH_SHORT).show();
            return "false";
        }
    }


    public static String removeSpecialCharsAndExtraSpaces(String input) {
        // Remove special characters (keep only alphanumeric)
        String cleanString = input.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Remove extra white spaces
        cleanString = cleanString.replaceAll("\\s+", "").trim();

        return cleanString;
    }
}

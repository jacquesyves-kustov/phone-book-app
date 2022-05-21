package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        // UI variables
        TextView debugInfo = findViewById(R.id.debugView);
        Button btBack = findViewById(R.id.backButton);


        // Define system info in string
        String s = "Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";

        // Display the info
        debugInfo.setText(s);


        /*
         * "BACK" button
         */

        btBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DebugActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
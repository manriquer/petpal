package com.example.petpal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar progress;
    private static String TAG = "MainActivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         progress = (ProgressBar) findViewById(R.id.barra);






            // progress.setIndeterminate(true);
            progress.setProgress(0);


            final int totalProgressTime = 100;
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;

                    while(jumpTime < totalProgressTime) {
                        try {
                            jumpTime += 2;
                            progress.setProgress(jumpTime);
                            sleep(50);
                        }
                        catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    Intent intent = new Intent(MainActivity.this, Principal.class);
                    startActivity(intent);

                }
            };
            t.start();


    }










}
package com.example.petpal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {
    ProgressBar progress;
    private static String TAG = "SplashActivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
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
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            };
            t.start();


    }










}
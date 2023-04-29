package com.example.petpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         progressBar = (ProgressBar) findViewById(R.id.determinate_linear_indicator);
        int maxValue=progressBar.getMax(); // get maximum value of the progress bar
    }




}
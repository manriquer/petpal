package com.example.petpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DistanceActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastLocation;
    private boolean tracking = false;
    private float distance = 0;

    private TextView distanceTextView;
    private FloatingActionButton startButton;
    private FloatingActionButton finishButton;

    private Handler handler = new Handler();
    private Runnable updateDistanceRunnable = new Runnable() {
        @Override
        public void run() {
            updateDistance();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        distanceTextView = findViewById(R.id.distance_text_view);
        startButton = findViewById(R.id.start_button);
        finishButton = findViewById(R.id.finish_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTracking();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (lastLocation != null && tracking) {
                    distance += lastLocation.distanceTo(location);
                }
                lastLocation = location;
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    private void startTracking() {
        tracking = true;
        distance = 0;
        handler.post(updateDistanceRunnable);
    }

    private void stopTracking() {
        tracking = false;
        handler.removeCallbacks(updateDistanceRunnable);
    }

    private void updateDistance() {
        distanceTextView.setText(String.format("%.2f m", distance));
    }
}

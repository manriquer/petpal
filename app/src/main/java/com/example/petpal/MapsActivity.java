package com.example.petpal;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.petpal.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseReference userLocationRef;
    private Map<String, Marker> userMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // TOP APP BAR:
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String displayName = currentUser.getDisplayName();

            // Actualización: Utiliza "users" en lugar de "ubicaciones" como colección base
            userLocationRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .child("ubicacion");

            userMarkers = new HashMap<>();

            // Agregar un ChildEventListener para escuchar cambios en la ubicación de otros usuarios
            userLocationRef.getParent().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    if (dataSnapshot.exists() && dataSnapshot.getKey() != null && !dataSnapshot.getKey().equals(displayName)) {
                        String latitudeString = dataSnapshot.child("latitude").getValue(String.class);
                        String longitudeString = dataSnapshot.child("longitude").getValue(String.class);
                        if (latitudeString != null && longitudeString != null) {
                            double latitude = Double.parseDouble(latitudeString);
                            double longitude = Double.parseDouble(longitudeString);
                            LatLng userLocation = new LatLng(latitude, longitude);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(userLocation).title(dataSnapshot.getKey()));
                            userMarkers.put(dataSnapshot.getKey(), marker);
                        }
                    }
                }



                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    // Actualizar la ubicación del usuario en el mapa si cambia
                    if (dataSnapshot.exists() && dataSnapshot.getKey() != null && !dataSnapshot.getKey().equals(displayName)) {
                        double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                        LatLng userLocation = new LatLng(latitude, longitude);
                        Marker marker = userMarkers.get(dataSnapshot.getKey());
                        if (marker != null) {
                            marker.setPosition(userLocation);
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(userLocation).title(dataSnapshot.getKey()));
                            userMarkers.put(dataSnapshot.getKey(), marker);
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    // Eliminar el marcador del usuario si se elimina su ubicación
                    if (dataSnapshot.getKey() != null) {
                        Marker marker = userMarkers.get(dataSnapshot.getKey());
                        if (marker != null) {
                            marker.remove();
                            userMarkers.remove(dataSnapshot.getKey());
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    // No es necesario realizar ninguna acción en este caso
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // No es necesario realizar ninguna acción en este caso
                }
            });
        }

        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                    saveUserLocation();
                }
            }
        });
    }

    private void saveUserLocation() {
        if (userLocationRef != null && currentLocation != null) {
            String latitude = String.valueOf(currentLocation.getLatitude());
            String longitude = String.valueOf(currentLocation.getLongitude());

            // Actualización: Guarda latitud y longitud como campos en lugar de un objeto LatLng
            userLocationRef.child("latitude").setValue(latitude);
            userLocationRef.child("longitude").setValue(longitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (currentLocation != null) {
            LatLng loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc).title("Your Current Location"));
            float zoomLevel = 18.0f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));

            // Llama a saveUserLocation() después de asegurarte de que mMap esté inicializado
            saveUserLocation();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Tiene que aceptar los permisos de ubicación para acceder", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

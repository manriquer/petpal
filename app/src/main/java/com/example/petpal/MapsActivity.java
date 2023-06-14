package com.example.petpal;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private FirebaseUser currentUser;

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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Actualización: Utiliza "users" en lugar de "ubicaciones" como colección base
            userLocationRef = FirebaseDatabase.getInstance().getReference("users");

            userMarkers = new HashMap<>();

            // Obtener todas las ubicaciones de usuarios en la base de datos
            userLocationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                            String username = userSnapshot.child("username").getValue(String.class);

                            if (userId != null && username != null) {
                                String latitudeString = userSnapshot.child("ubicacion").child("latitude").getValue(String.class);
                                String longitudeString = userSnapshot.child("ubicacion").child("longitude").getValue(String.class);

                                if (latitudeString != null && longitudeString != null) {
                                    double latitude = Double.parseDouble(latitudeString);
                                    double longitude = Double.parseDouble(longitudeString);
                                    LatLng userLocation = new LatLng(latitude, longitude);
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(userLocation).title(username));
                                    userMarkers.put(userId, marker);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores, si es necesario
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
        if (userLocationRef != null && currentUser != null && currentLocation != null) {
            String userId = currentUser.getUid();
            String latitude = String.valueOf(currentLocation.getLatitude());
            String longitude = String.valueOf(currentLocation.getLongitude());

            // Actualización: Guarda latitud y longitud como campos en lugar de un objeto LatLng
            userLocationRef.child(userId).child("ubicacion").child("latitude").setValue(latitude);
            userLocationRef.child(userId).child("ubicacion").child("longitude").setValue(longitude);
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
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String otherUserId = null;

                // Obtén el ID de usuario de la otra persona
                for (Map.Entry<String, Marker> entry : userMarkers.entrySet()) {
                    if (entry.getValue().equals(marker)) {
                        otherUserId = entry.getKey();
                        break;
                    }
                }

                if (otherUserId != null) {
                    // Genera el ID de la sala de chat único y compartido
                    String chatId = generateChatId(currentUser.getUid(), otherUserId);

                    Intent intent = new Intent(MapsActivity.this, ChatActivity.class);
                    intent.putExtra("chatId", chatId);
                    startActivity(intent);

                }

                return false;
            }
        });
    }

    private String generateChatId(String userId1, String userId2) {
        // Concatena los IDs de usuario en un orden específico
        String concatenatedIds;
        if (userId1.compareTo(userId2) < 0) {
            concatenatedIds = userId1 + userId2;
        } else {
            concatenatedIds = userId2 + userId1;
        }

        // Genera un hash único a partir de la concatenación
        return Integer.toString(concatenatedIds.hashCode());
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

package com.example.petpal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petpal.Pet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.util.Base64;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity implements AddPetDialog.OnAgregarAnimalListener {

    RecyclerView recyclerViewAnimales;
    List<Pet> animales;
    FloatingActionButton fab;
    PetsAdapter adaptador;
    LinearLayoutManager recyclerLayoutManager;
    FirebaseAuth auth;
    FirebaseUser user;

    int selectedNavItem; // Variable para almacenar la opción de navegación seleccionada



    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedNavItem = R.id.navigation_home;


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Acción cuando se selecciona la opción de casa

                        selectedNavItem = R.id.navigation_home;

                        return true;
                    case R.id.navigation_messages:
                        // Acción cuando se selecciona la opción de mensajes
                        selectedNavItem = R.id.navigation_messages;


                        animales.clear();
                        adaptador.notifyDataSetChanged();

                        // Mostrar las salas de chat
                        showChatRooms();

                        return true;
                    case R.id.navigation_profile:
                        // Acción cuando se selecciona la opción de perfil

                        selectedNavItem = R.id.navigation_profile;

                        return true;
                }
                return false;
            }
        });


        // FIREBASE LOGOUT:
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
            startActivity(intent);
            finish();
        }

        // TOP APP BAR:
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()== R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });

        // FAB BOTTON:
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPetDialog dialogo = new AddPetDialog();
                dialogo.show(getSupportFragmentManager(), "Add pet dialog");

                animales.clear();
            }
        });

        // LISTA:
        animales = new ArrayList<>();

        // Obtén la referencia de la base de datos para el usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // El usuario no ha iniciado sesión, maneja este caso según tus necesidades
            return;
        }
        String userId = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference mascotasRef = userRef.child("mascotas");

        mascotasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String animal = childSnapshot.child("animal").getValue(String.class);
                    String nombre = childSnapshot.child("nombre").getValue(String.class);
                    String raza = childSnapshot.child("raza").getValue(String.class);
                    String peso = childSnapshot.child("peso").getValue(String.class);
                    String fechaNacimiento = childSnapshot.child("fechaNacimiento").getValue(String.class);

                    String imagenBase64 = childSnapshot.child("imagenBase64").getValue(String.class);
                    byte[] imageBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
                    Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Crea un nuevo objeto Pet con los datos obtenidos
                    Pet mascota = new Pet(animal, nombre, raza, peso, fechaNacimiento, imagenBitmap);

                    // Agrega la mascota a la lista de animales
                    animales.add(mascota);
                }

                // Actualiza el adaptador del RecyclerView si es necesario
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Maneja el error de lectura de la base de datos
            }
        });

        recyclerViewAnimales = findViewById(R.id.recyclerViewAnimales);
        adaptador = new PetsAdapter(animales, this);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewAnimales.setLayoutManager(recyclerLayoutManager);
        recyclerViewAnimales.setAdapter(adaptador);
    }

    @Override
    public void onAgregarAnimal(String animal, String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen) {
        // Obtén la referencia de la base de datos para el usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // El usuario no ha iniciado sesión, maneja este caso según tus necesidades
            return;
        }
        String userId = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference mascotasRef = userRef.child("mascotas");

        Pet pet = new Pet(animal, nombre, raza, peso, fechaNacimiento,imagen);
        // Convierte la imagen a un arreglo de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // Codifica los bytes en Base64
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        pet.setImagenBase64(base64Image);

        // Guarda la mascota en la base de datos usando la clave generada
        mascotasRef.child(nombre).setValue(pet);

        // Agrega la mascota a la lista animales
        animales.add(pet);
        adaptador.notifyDataSetChanged();
    }

    private void showChatRooms() {
        // Lógica para mostrar las salas de chat


    }
}

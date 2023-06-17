package com.example.petpal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.util.Base64;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DialogAddPet.OnAgregarAnimalListener {

    RecyclerView recyclerViewAnimales;
    List<Pet> animales;
    FloatingActionButton fab;
    PetsAdapter adaptador;
    LinearLayoutManager recyclerLayoutManager;
    FirebaseAuth auth;
    FirebaseUser user;
    List<ChatRoom> chatRooms;

    ChatRoomsAdapter adapter;

    ChatRoomsAdapter.OnItemClickListener chatItemClickListener; // Nueva instancia de la interfaz

    private RecyclerView recyclerViewChatRooms;
    private ChatRoomsAdapter chatRoomsAdapter;

    int selectedNavItem; // Variable para almacenar la opción de navegación seleccionada

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FrameLayout ln;
    TextView nombre, email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatRooms = new ArrayList<>();
        selectedNavItem = R.id.navigation_home;
        ln = findViewById(R.id.Perfil);
        nombre = findViewById(R.id.nombreUser);
        email = findViewById(R.id.emailUser);
        recyclerViewChatRooms = findViewById(R.id.recyclerViewChatRooms);
        chatRoomsAdapter = new ChatRoomsAdapter(chatRooms, this);
        recyclerViewChatRooms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChatRooms.setAdapter(chatRoomsAdapter);
        showChatRooms();






        // Inicializa la instancia de la interfaz
        chatItemClickListener = new ChatRoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ChatRoom chatRoom = chatRooms.get(position); // Obtén la sala de chat seleccionada
                String chatId = chatRoom.getRoomId();

                // Abre ChatActivity y envía el chatId como extra
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chatId", chatId);
                startActivity(intent);
            }
        };

// Configura la instancia de la interfaz en el adaptador
        chatRoomsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Llama al método onItemClick de nuestra instancia chatItemClickListener
                chatItemClickListener.onItemClick(position);
            }
        });


        // Obtén la referencia de la base de datos para el usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            DatabaseReference mascotasRef = userRef.child("mascotas");

            mascotasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (selectedNavItem == R.id.navigation_home) {
                        animales.clear(); // Reinicia la lista solo cuando se selecciona la opción de "home"

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            // Resto del código para obtener y agregar las mascotas a la lista
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

                        adaptador.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Maneja el error de lectura de la base de datos
                }
            });

        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Acción cuando se selecciona la opción de casa

                        selectedNavItem = R.id.navigation_home;

                        fab.setVisibility(View.VISIBLE);
                        recyclerViewAnimales.setVisibility(View.VISIBLE);

                        recyclerViewChatRooms.setVisibility(View.GONE);
                        ln.setVisibility(View.GONE);


                        // Obtén la referencia de la base de datos para el usuario actual
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser == null) {
                            // El usuario no ha iniciado sesión, maneja este caso según tus necesidades
                            break;
                        }
                        String userId = currentUser.getUid();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                        DatabaseReference mascotasRef = userRef.child("mascotas");

                        mascotasRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                animales.clear(); // Reinicia la lista antes de agregar los nuevos elementos

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

                        return true;
                    case R.id.navigation_messages:
                        selectedNavItem = R.id.navigation_messages;

                        fab.setVisibility(View.GONE);
                        recyclerViewAnimales.setVisibility(View.GONE);
                        recyclerViewChatRooms.setVisibility(View.VISIBLE);
                        ln.setVisibility(View.GONE);

                        showChatRooms();
                        return true;


                    case R.id.navigation_profile:
                        // Acción cuando se selecciona la opción de perfil
                        selectedNavItem = R.id.navigation_profile;
                        fab.setVisibility(View.GONE);
                        recyclerViewAnimales.setVisibility(View.GONE);

                        recyclerViewChatRooms.setVisibility(View.GONE);

                        ln.setVisibility(View.VISIBLE);
                        if (user != null) {
                            // Obtén la URL de la foto de perfil del usuario
                            String photoUrl = user.getPhotoUrl().toString();

                            // Carga la imagen en un ImageView utilizando Picasso
                            ImageView imageView = findViewById(R.id.imageView);
                            Picasso.get().load(photoUrl).into(imageView);
                            String username = user.getDisplayName();


                            nombre.setText(username);
                            email.setText(user.getEmail());

                        } else {
                            // El usuario no ha iniciado sesión
                            System.out.println("El usuario no ha iniciado sesión.");
                        }

                        Spinner languageSpinner = findViewById(R.id.languageSpinner);
                        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedLanguage = parent.getItemAtPosition(position).toString();
                                switch (selectedLanguage) {
                                    case "Español":
                                        setLocale("es");
                                        break;
                                    case "Italiano":
                                        setLocale("it");
                                        break;
                                    case "Українська":
                                        setLocale("uk");
                                        break;
                                }
                                recreate();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // No se necesita implementar en este caso
                            }
                        });




                        return true;
                }
                return false;
            }
        });

        // FIREBASE LOGOUT:
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // TOP APP BAR:
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
//                AddPetDialog dialogo = new AddPetDialog();
//                dialogo.show(getSupportFragmentManager(), "Add pet dialog");
                DialogAddPet dialogo = new DialogAddPet();
                dialogo.show(getSupportFragmentManager(), "Add pet dialog");

/*
                animales.clear();
*/
            }
        });

        // LISTA:
        animales = new ArrayList<>();
         // Crear una nueva instancia de lista

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

        Pet pet = new Pet(animal, nombre, raza, peso, fechaNacimiento, imagen);
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
        // Obtén una referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chatsRef = database.getReference("chats");

        // Accede a la ubicación de la base de datos donde se almacenan las salas de chat
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRooms.clear(); // Limpia la lista antes de agregar las nuevas salas de chat

                // Leer los datos de Firebase y convertirlos en objetos ChatRoom
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    String chatId = chatSnapshot.getKey();

                    ChatRoom chatRoom = new ChatRoom(chatId);
                    chatRooms.add(chatRoom);
                }

                // Notifica al adaptador que los datos han cambiado
                chatRoomsAdapter.notifyDataSetChanged();

                // Agrega el listener para el evento de clic en la lista

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar cualquier error de lectura de Firebase aquí
            }
        });


    }
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


}





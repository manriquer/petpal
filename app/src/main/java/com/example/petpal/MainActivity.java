package com.example.petpal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
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
    ChatRoomsAdapter.OnItemClickListener chatItemClickListener;
    private RecyclerView recyclerViewChatRooms;
    private ChatRoomsAdapter chatRoomsAdapter;
    int selectedNavItem;
    FrameLayout ln;
    TextView nombre, email;
    Button eliminarPerfil;

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
        eliminarPerfil = findViewById(R.id.eliminarperfil);
        recyclerViewChatRooms = findViewById(R.id.recyclerViewChatRooms);
        chatRoomsAdapter = new ChatRoomsAdapter(chatRooms, this);
        recyclerViewChatRooms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChatRooms.setAdapter(chatRoomsAdapter);
        showChatRooms();

        chatItemClickListener = new ChatRoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ChatRoom chatRoom = chatRooms.get(position);
                String chatId = chatRoom.getRoomId();

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chatId", chatId);
                startActivity(intent);
            }
        };

        chatRoomsAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Llama al método onItemClick de nuestra instancia chatItemClickListener
                chatItemClickListener.onItemClick(position);
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            DatabaseReference mascotasRef = userRef.child("mascotas");

            mascotasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (selectedNavItem == R.id.navigation_home) {
                        animales.clear();

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String animal = childSnapshot.child("animal").getValue(String.class);
                            String nombre = childSnapshot.child("nombre").getValue(String.class);
                            String raza = childSnapshot.child("raza").getValue(String.class);
                            String peso = childSnapshot.child("peso").getValue(String.class);
                            String fechaNacimiento = childSnapshot.child("fechaNacimiento").getValue(String.class);
                            String imagenBase64 = childSnapshot.child("imagenBase64").getValue(String.class);
                            byte[] imageBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
                            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            Pet mascota = new Pet(animal, nombre, raza, peso, fechaNacimiento, imagenBitmap);
                            animales.add(mascota);
                        }

                        adaptador.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedNavItem = R.id.navigation_home;
                        fab.setVisibility(View.VISIBLE);
                        recyclerViewAnimales.setVisibility(View.VISIBLE);
                        recyclerViewChatRooms.setVisibility(View.GONE);
                        ln.setVisibility(View.GONE);

                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser == null) {
                            break;
                        }
                        String userId = currentUser.getUid();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                        DatabaseReference mascotasRef = userRef.child("mascotas");

                        mascotasRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                animales.clear();

                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String animal = childSnapshot.child("animal").getValue(String.class);
                                    String nombre = childSnapshot.child("nombre").getValue(String.class);
                                    String raza = childSnapshot.child("raza").getValue(String.class);
                                    String peso = childSnapshot.child("peso").getValue(String.class);
                                    String fechaNacimiento = childSnapshot.child("fechaNacimiento").getValue(String.class);

                                    String imagenBase64 = childSnapshot.child("imagenBase64").getValue(String.class);
                                    byte[] imageBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
                                    Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                    Pet mascota = new Pet(animal, nombre, raza, peso, fechaNacimiento, imagenBitmap);

                                    animales.add(mascota);
                                }

                                adaptador.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

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
                        selectedNavItem = R.id.navigation_profile;
                        fab.setVisibility(View.GONE);
                        recyclerViewAnimales.setVisibility(View.GONE);
                        recyclerViewChatRooms.setVisibility(View.GONE);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference usersRef = database.getReference("users");
                        ln.setVisibility(View.VISIBLE);
                        if (user != null) {
                            String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;
                            if (photoUrl != null) {
                                ImageView imageView = findViewById(R.id.imageView);
                                Picasso.get().load(photoUrl).into(imageView);
                            }
                            String username = user.getDisplayName();
                            if (username.equals("")) {
                                usersRef.child(user.getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String username = dataSnapshot.getValue(String.class);
                                        nombre.setText(username);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            } else {
                                nombre.setText(username);
                            }
                            email.setText(user.getEmail());

                            eliminarPerfil.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("¿Desea eliminar su cuenta?")
                                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    String uid = user.getUid();
                                                    usersRef.child(uid).removeValue();
                                                    user.delete();
                                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                        } else {
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
                DialogAddPet dialogo = new DialogAddPet();
                dialogo.show(getSupportFragmentManager(), "Add pet dialog");
                //animales.clear();
            }
        });

        // LISTA:
        animales = new ArrayList<>();
        recyclerViewAnimales = findViewById(R.id.recyclerViewAnimales);
        adaptador = new PetsAdapter(animales, this);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewAnimales.setLayoutManager(recyclerLayoutManager);
        recyclerViewAnimales.setAdapter(adaptador);
    }

    @Override
    public void onAgregarAnimal(String animal, String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userId = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference mascotasRef = userRef.child("mascotas");

        Pet pet = new Pet(animal, nombre, raza, peso, fechaNacimiento, imagen);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        pet.setImagenBase64(base64Image);
        mascotasRef.child(nombre).setValue(pet);

        animales.add(pet);
        adaptador.notifyDataSetChanged();
    }

    private void showChatRooms() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chatsRef = database.getReference("chats");
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRooms.clear();
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    String chatId = chatSnapshot.getKey();

                    ChatRoom chatRoom = new ChatRoom(chatId);
                    chatRooms.add(chatRoom);
                }

                chatRoomsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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





package com.example.petpal;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPetDialog.OnAgregarAnimalListener {



    RecyclerView recyclerViewAnimales;
    List<Pet> animales;
    FloatingActionButton fab;
    PetsAdapter adaptador;
    LinearLayoutManager recyclerLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TOP APP BAR:
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
//        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId()== R.id.more) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        // FAB BOTTON:
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                AddPetDialog dialogo = new AddPetDialog();
                dialogo.show(getSupportFragmentManager(), "MiDialogoPersonalizado");
            }
        });

        // LISTA:


        animales = new ArrayList<>();
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back_24dp);*/
        /*Pet gato= new Pet("Stanis","salvaje","3kg","15/06/2003", bitmap);
        animales.add(gato);


        Pet perro = new Pet("faeba","sfinx","100kg","23/03/2000A.C", bitmap);
        animales.add(perro);*/



        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("mascotas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String nombre = childSnapshot.child("nombre").getValue(String.class);
                    String raza = childSnapshot.child("raza").getValue(String.class);
                    String peso = childSnapshot.child("peso").getValue(String.class);
                    String fechaNacimiento = childSnapshot.child("fechaNacimiento").getValue(String.class);

                    // Crea un nuevo objeto Pet con los datos obtenidos
                    Pet mascota = new Pet(nombre, raza, peso, fechaNacimiento, null);

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

        


        // Write a message to the database

      /*  DatabaseReference mMessagesRef = mRootRef.child("mascotas");*/





       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back_24dp);
        Pet friendlyMessage = new Pet("Stanis","salvaje","3kg","15/06/2003", bitmap);*/

       /* mMessagesRef.push().setValue(gato);
        mMessagesRef.push().setValue(perro);*/

        recyclerViewAnimales = findViewById(R.id.recyclerViewAnimales);
        adaptador = new PetsAdapter(animales, this);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewAnimales.setLayoutManager(recyclerLayoutManager);
        recyclerViewAnimales.setAdapter(adaptador);
    }

    public void onAgregarAnimal(String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen) {
        // Agrega los datos ingresados
        Pet nuevoPet = new Pet(nombre, raza, peso,fechaNacimiento, imagen);
        ((PetsAdapter)recyclerViewAnimales.getAdapter()).addAnimal(nuevoPet);
    }


}

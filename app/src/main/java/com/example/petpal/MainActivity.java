package com.example.petpal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()== R.id.more) {
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
                dialogo.show(getSupportFragmentManager(), "MiDialogoPersonalizado");
            }
        });

        // LISTA:
        animales = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icono);
        Pet gato= new Pet("Stanis","salvaje","3kg","15/06/2003", bitmap);
        animales.add(gato);
        Pet perro = new Pet("faeba","sfinx","100kg","23/03/2000A.C", bitmap);
        animales.add(perro);

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

package com.example.petpal;

import static com.example.petpal.R.drawable.baseline_add_24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPetDialog.OnAgregarAnimalListener {
//    ListView listViewAnimales;

    RecyclerView recyclerViewAnimales;
    List<Pet> animales;
    FloatingActionButton fab;
    PetsAdapter adaptador;

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

        recyclerViewAnimales = findViewById(R.id.recyclerViewAnimales);

//        listViewAnimales = findViewById(R.id.lista);

        animales = new ArrayList<>();
        // Convertimos el drawable a bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), baseline_add_24);

        Pet gato= new Pet("Stanis","salvaje","3kg","15/06/2003", bitmap );
        animales.add(gato);
        Pet perro = new Pet("faeba","sfinx","100kg","23/03/2000A.C",bitmap);
        animales.add(perro);

//        PetsAdapter adaptador = new PetsAdapter(animales, this);
//        listViewAnimales.setAdapter(adaptador);

//        listViewAnimales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        adaptador = new PetsAdapter(animales, this);
        recyclerViewAnimales.setAdapter(adaptador);
        recyclerViewAnimales.setLayoutManager(new LinearLayoutManager(this));

//        adaptador.setOnItemClickListener(new PetsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Pet pet = animales.get(position);
//                Intent intent = new Intent(MainActivity.this, PetProfileActivity.class);
//                intent.putExtra("animal", pet);
//                startActivity(intent);
//            }
//        });

//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Obtener el animal seleccionado
//                Pet pet = (Pet) parent.getItemAtPosition(position);
//                // Crea un Intent para abrir la nueva actividad y establece el elemento seleccionado como dato extra
//                Intent intent = new Intent(MainActivity.this, PetProfileActivity.class);
//                intent.putExtra("animal", pet);
//                startActivity(intent);
//            }
//        });

    }

    public void onAgregarAnimal(String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen) {
        // Agrega los datos ingresados al ListView
        Pet nuevoPet = new Pet(nombre, raza, peso,fechaNacimiento, imagen);
        ((PetsAdapter)recyclerViewAnimales.getAdapter()).addAnimal(nuevoPet);
    }


}

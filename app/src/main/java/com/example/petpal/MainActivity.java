package com.example.petpal;

import static com.example.petpal.R.drawable.baseline_add_24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  AddPetDialog.OnAgregarAnimalListener{

    TextView texto;
    ListView listViewAnimales;
    Button anyadir;
    List<Pet> animales;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = findViewById(R.id.petpal);
        texto = findViewById(R.id.textView4);
        listViewAnimales = findViewById(R.id.lista);
        anyadir = findViewById(R.id.button);
        animales = new ArrayList<>();
        // Convertimos el drawable a bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), baseline_add_24);

        Pet gato= new Pet("Stanis","salvaje","3kg","15/06/2003", bitmap );
        animales.add(gato);
        Pet perro = new Pet("faeba","sfinx","100kg","23/03/2000A.C",bitmap);
        animales.add(perro);

        PetsAdapter adaptador = new PetsAdapter(animales, this);
        listViewAnimales.setAdapter(adaptador);

        listViewAnimales.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("ListView", "onTouch: " + event);
                return false;
            }
        });

        listViewAnimales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el animal seleccionado
                Pet pet = (Pet) parent.getItemAtPosition(position);
                // Crea un Intent para abrir la nueva actividad y establece el elemento seleccionado como dato extra
                Intent intent = new Intent(MainActivity.this, PetProfileActivity.class);
                intent.putExtra("animal", pet);
                startActivity(intent);
            }
        });



        anyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPetDialog dialogo = new AddPetDialog();
                dialogo.show(getSupportFragmentManager(), "MiDialogoPersonalizado");
            }
        });




    }
    public void onAgregarAnimal(String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen) {
        // Agrega los datos ingresados al ListView
        Pet nuevoPet = new Pet(nombre, raza, peso,fechaNacimiento, imagen);
        ((PetsAdapter)listViewAnimales.getAdapter()).addAnimal(nuevoPet);
    }






}

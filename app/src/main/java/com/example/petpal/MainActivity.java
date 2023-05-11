package com.example.petpal;

import static com.example.petpal.R.drawable.baseline_add_24;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  MiDialogoPersonalizado.OnAgregarAnimalListener{



// ...

    TextView texto;
    ListView listViewAnimales;
    Button añadir;
    List<Animal> animales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ImageView image = findViewById(R.id.petpal);
        texto = findViewById(R.id.textView4);
        listViewAnimales = findViewById(R.id.lista);
         añadir = findViewById(R.id.button);
        animales = new ArrayList<>();



// Convertimos el drawable a bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), baseline_add_24);

        Animal gato= new Animal("Stanis","salvaje","3kg","15/06/2003", bitmap );
        animales.add(gato);
        Animal perro = new Animal("Heba","sfinx","100kg","23/03/2000A.C",bitmap);
        animales.add(perro);


        AdaptadorAnimales adaptador = new AdaptadorAnimales(animales, this);
        listViewAnimales.setAdapter(adaptador);

        listViewAnimales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtén el elemento seleccionado del ListView
                String elementoSeleccionado = (String) parent.getItemAtPosition(position);

                // Crea un Intent para abrir la nueva actividad y establece el elemento seleccionado como dato extra
                Intent intent = new Intent(getApplicationContext(), PerfilMascota.class);
                intent.putExtra("elemento_seleccionado", elementoSeleccionado);
                startActivity(intent);
            }
        });

        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiDialogoPersonalizado dialogo = new MiDialogoPersonalizado();
                dialogo.show(getSupportFragmentManager(), "MiDialogoPersonalizado");
            }
        });




    }
    public void onAgregarAnimal(String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen) {
        // Agrega los datos ingresados al ListView
        Animal nuevoAnimal = new Animal(nombre, raza, peso,fechaNacimiento, imagen);
        ((AdaptadorAnimales)listViewAnimales.getAdapter()).addAnimal(nuevoAnimal);
    }




    public class AdaptadorAnimales extends BaseAdapter {
        private List<Animal> animales;
        private Context contexto;

        public AdaptadorAnimales(List<Animal> animales, Context contexto) {
            this.animales = animales;
            this.contexto = contexto;
        }

        @Override
        public int getCount() {
            return animales.size();
        }

        @Override
        public Object getItem(int position) {
            return animales.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(contexto);
                convertView = inflater.inflate(R.layout.fila_animal, null);
            }


            TextView nombre = convertView.findViewById(R.id.nombre_animal);
            TextView raza = convertView.findViewById(R.id.raza_animal);
            TextView peso = convertView.findViewById(R.id.peso_animal);
            TextView fechaNacimiento = convertView.findViewById(R.id.fecha_nacimiento_animal);
            ImageView imagen = convertView.findViewById(R.id.imagen_animal);
            Animal animal = animales.get(position);




            nombre.setText(animal.getNombre());
            raza.setText(animal.getRaza());
            peso.setText(animal.getPeso());
            fechaNacimiento.setText(animal.getFechaNacimiento());
            imagen.setImageBitmap(animal.getFoto());
            return convertView;
        }

        public void addAnimal(Animal animal) {
            animales.add(animal);
            notifyDataSetChanged();
        }
    }

}

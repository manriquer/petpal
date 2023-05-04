package com.example.petpal;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    TextView texto;
    ListView lista;
    Button añadir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        ImageView image = findViewById(R.id.petpal);
        texto = findViewById(R.id.textView4);
        lista = findViewById(R.id.lista);
         añadir = findViewById(R.id.button);
        ArrayList<String> listaDeNombres = new ArrayList<>();
        listaDeNombres.add("Juan Provisional");
        listaDeNombres.add("María");
        listaDeNombres.add("Pedro");

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDeNombres);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}

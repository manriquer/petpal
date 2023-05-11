package com.example.petpal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilMascota extends AppCompatActivity {
    ImageView logo,foto;
    TextView info;
    Button darpaseo, anyadirreco;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_mascota);

        Animal animal = (Animal) getIntent().getSerializableExtra("animal");

        logo = findViewById(R.id.petpal);
        foto = findViewById(R.id.foto);
        info = findViewById(R.id.info);
        darpaseo = findViewById(R.id.darpaseo);
        anyadirreco = findViewById(R.id.recordatorio);
//        darpaseo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mostrarConfirmacion();
//            }
//        });

    }

    public void paseo(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void mostrarConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Con mas mascotas?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción que deseas ejecutar si el usuario confirma
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción que deseas ejecutar si el usuario cancela
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

package com.example.petpal;

import android.view.View;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class PetProfileActivity extends AppCompatActivity {

    ImageView foto;
    TextView info;
    Button darpaseo;
    Button reminder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        // TOP APP BAR:
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        foto = findViewById(R.id.foto);
        info = findViewById(R.id.info);
        darpaseo = findViewById(R.id.darpaseo);
//        anyadirreco = findViewById(R.id.recordatorio);
//        chat = findViewById(R.id.chat);
        reminder = findViewById(R.id.reminder);

        // Obtén el objeto Pet del Intent
        Intent intent = getIntent();
        Pet pet = (Pet) getIntent().getSerializableExtra("pet");
        String infoText = getString(R.string.nombre) + ": " + pet.getNombre() + "\n" +
                getString(R.string.animal) + ": " + pet.getAnimal() + "\n" +
                getString(R.string.raza) + ": " + pet.getRaza() + "\n" +
                getString(R.string.fecha_nacimiento) + ": " + pet.getFechaNacimiento() + "\n" +
                getString(R.string.peso) + ": " + pet.getPeso();
        info.setText(infoText);

        byte[] imageBytes = getIntent().getByteArrayExtra("imagen");
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        foto.setImageBitmap(imageBitmap);
    }

    // ...

    public void paseo(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void reco(View view) {
        Intent intent = new Intent(this, DistanceActivity.class);
        startActivity(intent);
    }

    public void reminder(View view) {
        ReminderDialog dialogo = new ReminderDialog();
        dialogo.show(getSupportFragmentManager(), "Reminder");
    }

    private void mostrarConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirmacion);

        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción que deseas ejecutar si el usuario confirma
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

package com.example.petpal;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.text.format.DateFormat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;

public class PetProfileActivity extends AppCompatActivity {

//    private FirebaseListAdapter<ChatMessage> adapter;
    ImageView foto;
    TextView info;
    Button darpaseo, anyadirreco;
//    Button chat;
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

//        chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PetProfileActivity.this, ChatActivity.class);
//                startActivity(intent);
//                // Load chat room contents
//            }
//        });

        // Obtén el objeto Pet del Intent
        Intent intent = getIntent();
        Pet pet = (Pet) getIntent().getSerializableExtra("pet");
        String infoText = getString(R.string.nombre) + ": " + pet.getNombre() + "\n" +
                getString(R.string.animal) + ": " + pet.getAnimal() + "\n" +
                getString(R.string.raza) + ": " + pet.getRaza() + "\n" +
                getString(R.string.fecha_nacimiento) + ": " + pet.getFechaNacimiento() + "\n" +
                getString(R.string.peso) + ": " + pet.getPeso();
        info.setText(infoText);

        String imagen = getIntent().getStringExtra("imagen");
        byte[] imageBytes = Base64.decode(imagen, Base64.DEFAULT);
        Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        foto.setImageBitmap(imagenBitmap);
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
        ExampleDialog dialogo = new ExampleDialog();
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

package com.example.petpal;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class PetDialog {
    private final Context CTX;
    public PetDialog(Context context) {
        this.CTX = context;
    }

    public void show() {
        // Inflate the custom view
        View customView = LayoutInflater.from(CTX).inflate(R.layout.add_pet_dialog, null);

        // Find the form elements in the custom view
        TextView animalTextView = customView.findViewById(R.id.Animal);
        Spinner animalSpinner= customView.findViewById(R.id.spinner);
        TextView nameTextView = customView.findViewById(R.id.nombre);
        EditText nameEditText = customView.findViewById(R.id.enombre);
        Button anyadir = customView.findViewById(R.id.anyadir);
        TextView breedTextView = customView.findViewById(R.id.raza);
        EditText breedEditText = customView.findViewById(R.id.eraza);
        TextView weightTextView = customView.findViewById(R.id.peso);
        EditText weightEditText = customView.findViewById(R.id.epeso);
        TextView dateTextView = customView.findViewById(R.id.anyo);
        EditText dateEditText = customView.findViewById(R.id.eanyo);
        ImageView mImageView = customView.findViewById(R.id.anyadirimagen);

        // Funcionalidades



        // Create and show the dialog
        new MaterialAlertDialogBuilder(CTX)
                .setTitle("Añadir nueva mascota")
                .setView(customView)
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Respond to neutral button press
                            }
                        })
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the entered data
                        String nombre = nameEditText.getText().toString();
                        String animal = animalSpinner.getSelectedItem().toString();
                        String raza = breedEditText.getText().toString();
                        String peso = weightEditText.getText().toString();
                        String fechaNacimiento = dateEditText.getText().toString();
                        Bitmap imagen;
                    }
                })
                .show();
    }
}

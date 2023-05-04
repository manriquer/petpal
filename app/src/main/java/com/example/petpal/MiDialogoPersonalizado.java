package com.example.petpal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class MiDialogoPersonalizado extends DialogFragment {
    TextView raza, eraza, peso, epeso, año,eaño;

    Button añadir;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.mi_dialogo, container, false);

        añadir = vista.findViewById(R.id.añadir);
        raza= vista.findViewById(R.id.raza);
        eraza = vista.findViewById(R.id.eraza);
        peso = vista.findViewById(R.id.peso);
        epeso = vista.findViewById(R.id.epeso);
        año= vista.findViewById(R.id.año);
        eaño= vista.findViewById(R.id.eaño);
        // Agrega cualquier functionalism adicional aquí

        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar al presionar el botón "Aceptar"
                dismiss(); // Cerrar el diálogo
            }
        });
        return vista;
    }
}

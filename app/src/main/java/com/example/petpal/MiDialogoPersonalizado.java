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
    TextView raza, eraza, peso, epeso, año,eaño,nombre,enombre;

    Button añadir;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.mi_dialogo, container, false);
        nombre= vista.findViewById(R.id.nombre);
        enombre= vista.findViewById(R.id.enombre);
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
                String nombre = enombre.getText().toString();
                String raza = eraza.getText().toString();
                String peso = epeso.getText().toString();
                String fechaNacimiento = eaño.getText().toString();


                // Llama al método onAgregarAnimal() de la interfaz
                OnAgregarAnimalListener listener = (OnAgregarAnimalListener) getActivity();
                listener.onAgregarAnimal(nombre, raza, peso, fechaNacimiento);

                dismiss(); // Cerrar el diálogo
            }
        });

        return vista;
    }
    public interface OnAgregarAnimalListener {
        void onAgregarAnimal(String nombre, String raza, String peso, String fechaNacimiento);
    }

}

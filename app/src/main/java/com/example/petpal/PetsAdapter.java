package com.example.petpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PetsAdapter extends BaseAdapter {
    private List<Pet> animales;
    private Context contexto;

    public PetsAdapter(List<Pet> animales, Context contexto) {
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
            convertView = inflater.inflate(R.layout.pet_row, null);
        }


        TextView nombre = convertView.findViewById(R.id.nombre_animal);
        TextView raza = convertView.findViewById(R.id.raza_animal);
        TextView peso = convertView.findViewById(R.id.peso_animal);
        TextView fechaNacimiento = convertView.findViewById(R.id.fecha_nacimiento_animal);
        ImageView imagen = convertView.findViewById(R.id.imagen_animal);
        Pet pet = animales.get(position);




        nombre.setText(pet.getNombre());
        raza.setText(pet.getRaza());
        peso.setText(pet.getPeso());
        fechaNacimiento.setText(pet.getFechaNacimiento());
        imagen.setImageBitmap(pet.getFoto());
        return convertView;
    }

    public void addAnimal(Pet pet) {
        animales.add(pet);
        notifyDataSetChanged();
    }
}
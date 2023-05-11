package com.example.petpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
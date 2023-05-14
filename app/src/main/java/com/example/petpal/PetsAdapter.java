package com.example.petpal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    private List<Pet> animales;
    private Context contexto;

    // Constructor y métodos existentes aquí...
    public PetsAdapter(List<Pet> animales, Context contexto) {
        this.animales = animales;
        this.contexto = contexto;
    }

    @Override
    public int getItemCount() {
        return animales.size();
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View view = inflater.inflate(R.layout.pet_row, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        Pet pet = animales.get(position);
        holder.bind(pet);
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        // Declaraciones de vistas aquí...
        TextView nombre;
        TextView raza;
        TextView peso;
        TextView fechaNacimiento;
        ImageView imagen;

        public PetViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_animal);
            raza = itemView.findViewById(R.id.raza_animal);
            peso = itemView.findViewById(R.id.peso_animal);
            fechaNacimiento = itemView.findViewById(R.id.fecha_nacimiento_animal);
            imagen = itemView.findViewById(R.id.imagen_animal);
        }

        public void bind(Pet pet) {
            nombre.setText(pet.getNombre());
            raza.setText(pet.getRaza());
            peso.setText(pet.getPeso());
            fechaNacimiento.setText(pet.getFechaNacimiento());
            imagen.setImageBitmap(pet.getFoto());

            // Maneja los eventos de clic aquí si es necesario
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, PetProfileActivity.class);
                    intent.putExtra("animal", pet);
                    contexto.startActivity(intent);
                }
            });
        }
    }

    public void addAnimal(Pet pet) {
        animales.add(pet);
        notifyDataSetChanged();
    }
}


//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class PetsAdapter extends BaseAdapter {
//    private List<Pet> animales;
//    private Context contexto;
//
//    public PetsAdapter(List<Pet> animales, Context contexto) {
//        this.animales = animales;
//        this.contexto = contexto;
//    }
//
//    @Override
//    public int getCount() {
//        return animales.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return animales.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            LayoutInflater inflater = LayoutInflater.from(contexto);
//            convertView = inflater.inflate(R.layout.pet_row, null);
//        }
//
//
//        TextView nombre = convertView.findViewById(R.id.nombre_animal);
//        TextView raza = convertView.findViewById(R.id.raza_animal);
//        TextView peso = convertView.findViewById(R.id.peso_animal);
//        TextView fechaNacimiento = convertView.findViewById(R.id.fecha_nacimiento_animal);
//        ImageView imagen = convertView.findViewById(R.id.imagen_animal);
//        Pet pet = animales.get(position);
//
//
//
//
//        nombre.setText(pet.getNombre());
//        raza.setText(pet.getRaza());
//        peso.setText(pet.getPeso());
//        fechaNacimiento.setText(pet.getFechaNacimiento());
//        imagen.setImageBitmap(pet.getFoto());
//        return convertView;
//    }
//
//    public void addAnimal(Pet pet) {
//        animales.add(pet);
//        notifyDataSetChanged();
//    }
//}
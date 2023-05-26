package com.example.petpal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    // Dentro de la clase PetsAdapter
    private DatabaseReference databaseRef;

    private List<Pet> animales;
    private Context contexto;



    // Constructor y métodos existentes aquí...
    public PetsAdapter(List<Pet> animales, Context contexto) {
        this.animales = animales;
        this.contexto = contexto;

        // Obtener referencia a la base de datos
        databaseRef = FirebaseDatabase.getInstance().getReference();
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteDialog(getAdapterPosition());
                    return true;
                }
            });

        }


        private void showDeleteDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle("Eliminar")
                    .setMessage("¿Deseas eliminar este animal?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (position >= 0 && position < animales.size()) {
                                Pet pet = animales.get(position);

                                // Eliminar la mascota de la lista
                                animales.remove(position);
                                notifyDataSetChanged();

                                // Obtener el ID de la mascota
                                String petId = pet.getNombre();

                                // Eliminar el nodo correspondiente de la base de datos
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("mascotas").child(petId);
                                databaseRef.removeValue();


                                Toast.makeText(contexto, "Has eliminado a "+ petId+ " correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }


    public void addAnimal(Pet pet) {
       /* animales.add(pet);
        notifyDataSetChanged();*/
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
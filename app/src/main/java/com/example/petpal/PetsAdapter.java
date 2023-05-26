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
        TextView animal;
        TextView peso;
        TextView fechaNacimiento;
        ImageView imagen;

        public PetViewHolder(View itemView) {
            super(itemView);

            animal = itemView.findViewById(R.id.animal);
            nombre = itemView.findViewById(R.id.nombre_animal);
            raza = itemView.findViewById(R.id.raza_animal);
            peso = itemView.findViewById(R.id.peso_animal);
            fechaNacimiento = itemView.findViewById(R.id.fecha_nacimiento_animal);
            imagen = itemView.findViewById(R.id.imagen_animal);
        }

        public void bind(Pet pet) {
            animal.setText(pet.getAnimal());
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
                    showDeleteDialog( getAdapterPosition());
                    return true;
                }
            });


        }


        private void showDeleteDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            String title = contexto.getString(R.string.dialog_title_delete);
            String message = contexto.getString(R.string.dialog_message_delete);
            String positiveButton = contexto.getString(R.string.dialog_button_yes);
            String negativeButton = contexto.getString(R.string.dialog_button_no);

            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (position >= 0 && position < animales.size()) {
                                Pet pet = animales.get(position);

                                // Eliminar la mascota de la lista
                                animales.remove(position);
                                notifyDataSetChanged();

                                // Obtener el ID de la mascota
                                String petId = pet.getNombre();

                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                dbRef.child("mascotas").child(pet.getNombre().toString()).removeValue();

                                String deleteToastMessage = contexto.getString(R.string.toast_delete_success, petId);
                                Toast.makeText(contexto, deleteToastMessage, Toast.LENGTH_SHORT).show();

                                animales.clear();
                            }
                        }
                    })
                    .setNegativeButton(negativeButton, null)
                    .show();
        }


    }


    public void addAnimal(Pet pet) {
       /* animales.add(pet);
        notifyDataSetChanged();*/
    }
}



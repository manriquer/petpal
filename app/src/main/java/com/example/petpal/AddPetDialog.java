    package com.example.petpal;

    import static android.app.Activity.RESULT_OK;
    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.drawable.BitmapDrawable;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;
    import androidx.fragment.app.DialogFragment;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import java.io.IOException;

    public class AddPetDialog extends DialogFragment {

        TextView nameTextView, breedTextView, weightTextView, dateTextView;
        EditText nameEditText, breedEditText, weightEditText, dateEditText;
        Button anyadir;

        private static final int REQUEST_IMAGE_CAPTURE = 1;
        private static final int REQUEST_IMAGE_GALLERY = 2;

        private ImageView mImageView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View vista = inflater.inflate(R.layout.add_pet_dialog, container, false);
            nameTextView = vista.findViewById(R.id.nombre);
            nameEditText = vista.findViewById(R.id.enombre);
            anyadir = vista.findViewById(R.id.anyadir);
            breedTextView = vista.findViewById(R.id.raza);
            breedEditText = vista.findViewById(R.id.eraza);
            weightTextView = vista.findViewById(R.id.peso);
            weightEditText = vista.findViewById(R.id.epeso);
            dateTextView = vista.findViewById(R.id.anyo);
            dateEditText = vista.findViewById(R.id.eanyo);
            // Agrega cualquier functionalism adicional aquí
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mImageView = vista.findViewById(R.id.anyadirimagen);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog();
                }
            });
            anyadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nombre = nameEditText.getText().toString();
                    String raza = breedEditText.getText().toString();
                    String peso = weightEditText.getText().toString();
                    String fechaNacimiento = dateEditText.getText().toString();
                    Bitmap imagen;

                    if (mImageView.getDrawable() == null) {
                        imagen = BitmapFactory.decodeResource(getResources(), R.drawable.icono);
                    } else {
                        imagen = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                    }

                    if (nombre.isEmpty() || raza.isEmpty() || peso.isEmpty() || fechaNacimiento.isEmpty()) {
                        Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    }  else {
                        // Llama al método onAgregarAnimal() de la interfaz
                        /*OnAgregarAnimalListener listener = (OnAgregarAnimalListener) getActivity();
                        listener.onAgregarAnimal(nombre, raza, peso, fechaNacimiento, imagen);*/


                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                          DatabaseReference mMessagesRef = mRootRef.child("mascotas");

                        Pet pet = new Pet(nombre, raza, peso, fechaNacimiento);

                        mMessagesRef.push().setValue(pet);









                        dismiss(); // Cerrar el diálogo
                    }
                }
            });

            return vista;
        }


        private void showImageDialog() {
            final CharSequence[] options = {"Tomar foto", "Elegir de la galería", "Cancelar"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Pasar getActivity() en lugar de this
            builder.setTitle("Elige una opción");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Tomar foto")) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) { // Cambiar getPackageManager() por getActivity().getPackageManager()
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    } else if (options[item].equals("Elegir de la galería")) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mImageView.setImageBitmap(imageBitmap);
                } else if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                        // Escalar la imagen al tamaño del ImageView
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, mImageView.getWidth(), mImageView.getHeight(), true);

                        // Establecer la imagen escalada en el ImageView
                        mImageView.setImageBitmap(scaledBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        public interface OnAgregarAnimalListener {
            void onAgregarAnimal(String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen);

        }

    }

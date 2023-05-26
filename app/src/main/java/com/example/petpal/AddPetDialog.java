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
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.DialogFragment;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import java.io.IOException;

    public class AddPetDialog extends DialogFragment {

        TextView nameTextView, breedTextView, weightTextView, dateTextView,animalTextView;
        EditText nameEditText, breedEditText, weightEditText, dateEditText ;
        Button anyadir;

        Spinner animalSpinner;

        private static final int REQUEST_IMAGE_CAPTURE = 1;
        private static final int REQUEST_IMAGE_GALLERY = 2;

        private ImageView mImageView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View vista = inflater.inflate(R.layout.add_pet_dialog, container, false);
            animalTextView = vista.findViewById(R.id.Animal);
            animalSpinner= vista.findViewById(R.id.spinner);
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

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.nombres, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            animalSpinner.setAdapter(adapter);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog();
                }
            });
            anyadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String animal = animalSpinner.getSelectedItem().toString();
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
                        Toast.makeText(getActivity(), R.string.complete_campos, Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mMessagesRef = mRootRef.child("mascotas");

                        Pet pet = new Pet(animal,nombre, raza, peso, fechaNacimiento);

                       /* String key = mMessagesRef.push().getKey();*/
                        mMessagesRef.child(nombre).setValue(pet);

                        dismiss(); // Cerrar el diálogo
                    }
                }
            });



            return vista;
        }


        private void showImageDialog() {
            CharSequence[] options = {
                    getResources().getString(R.string.option_take_photo),
                    getResources().getString(R.string.option_choose_gallery),
                    getResources().getString(R.string.option_cancel)
            };


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Pasar getActivity() en lugar de this
            builder.setTitle(R.string.dialog_title);

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    String takePhotoOption = getResources().getString(R.string.option_take_photo);
                    String chooseFromGalleryOption = getResources().getString(R.string.option_choose_gallery);
                    String cancelOption = getResources().getString(R.string.option_cancel);

                    if (options[item].equals(takePhotoOption)) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    } else if (options[item].equals(chooseFromGalleryOption)) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
                    } else if (options[item].equals(cancelOption)) {
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
            void onAgregarAnimal(String animal, String nombre, String raza, String peso, String fechaNacimiento, Bitmap imagen);

        }

    }

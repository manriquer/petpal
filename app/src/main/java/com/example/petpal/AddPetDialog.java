    package com.example.petpal;

    import static android.app.Activity.RESULT_OK;
    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Color;
    import android.graphics.drawable.BitmapDrawable;
    import android.graphics.drawable.ColorDrawable;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.Window;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;
    import androidx.fragment.app.DialogFragment;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import java.io.IOException;
    import android.util.Base64;
    import java.io.ByteArrayOutputStream;

    public class AddPetDialog extends DialogFragment {

        private TextView nameTextView, breedTextView, weightTextView, dateTextView, animalTextView;
        private EditText nameEditText, breedEditText, weightEditText, dateEditText ;
        private Button anyadir;
        private Spinner animalSpinner;
        private static final int REQUEST_IMAGE_CAPTURE = 1;
        private static final int REQUEST_IMAGE_GALLERY = 2;
        private ImageView mImageView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.add_pet_dialog, container, false);
            animalTextView = view.findViewById(R.id.Animal);
            animalSpinner= view.findViewById(R.id.spinner);
            nameTextView = view.findViewById(R.id.nombre);
            nameEditText = view.findViewById(R.id.enombre);
            anyadir = view.findViewById(R.id.anyadir);
            breedTextView = view.findViewById(R.id.raza);
            breedEditText = view.findViewById(R.id.eraza);
            weightTextView = view.findViewById(R.id.peso);
            weightEditText = view.findViewById(R.id.epeso);
            dateTextView = view.findViewById(R.id.anyo);
            dateEditText = view.findViewById(R.id.eanyo);
            // Agrega cualquier functionalism adicional aquí
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mImageView = view.findViewById(R.id.anyadirimagen);

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
                        // Convierte la imagen a un arreglo de bytes
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagen.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();

                        // Codifica los bytes en Base64
                        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference mMessagesRef = mRootRef.child("mascotas");

                        Pet pet = new Pet(animal,nombre, raza, peso, fechaNacimiento,base64Image);
                        pet.setImagenBase64(base64Image);

                        /* String key = mMessagesRef.push().getKey();*/
                        mMessagesRef.child(nombre).setValue(pet);

                        dismiss(); // Cerrar el diálogo
                    }
                }
            });




            return view;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
            return dialog;
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

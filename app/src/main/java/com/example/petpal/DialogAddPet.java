package com.example.petpal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import static android.app.Activity.RESULT_OK;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Locale;

public class DialogAddPet extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "dialog_add_pet";
    private Toolbar toolbar;
    private EditText name, breed, weight, date;

    private EditText otroAnimalEditText;
    private Button anyadir;

    private Spinner animalSpinner;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private ImageView mImageView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

//    public static DialogAddPet display(FragmentManager fragmentManager) {
//        DialogAddPet dialog = new DialogAddPet();
//        dialog.show(fragmentManager, TAG);
//        return dialog;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_pet, container, false);

        name = view.findViewById(R.id.enombre);
        toolbar = view.findViewById(R.id.toolbar);
        breed = view.findViewById(R.id.eraza);
        date = view.findViewById(R.id.eanyo);
        weight = view.findViewById(R.id.epeso);
        animalSpinner= view.findViewById(R.id.spinner);
        anyadir = view.findViewById(R.id.anyadir);

       otroAnimalEditText = view.findViewById(R.id.otroAnimalEditText);

        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeightPickerDialog();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), DialogAddPet.this, year, month, day);

                datePickerDialog.show();
            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mImageView = view.findViewById(R.id.anyadirimagen);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.nombres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalSpinner.setAdapter(adapter);
        /*animalSpinner.setSelection(0);*/

        animalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int lastIndex = parent.getCount() - 1;
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (position == lastIndex) {
                    otroAnimalEditText.setVisibility(View.VISIBLE);
                } else {
                    otroAnimalEditText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada que hacer aquí
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        anyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del usuario actual
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    // El usuario no ha iniciado sesión, maneja este caso según tus necesidades
                    return;
                }
                String userId = currentUser.getUid();

                String animal;
                int lastPosition = animalSpinner.getAdapter().getCount() - 1;
                String lastItem = animalSpinner.getAdapter().getItem(lastPosition).toString();
                String selectedAnimal = animalSpinner.getSelectedItem().toString();

                if (selectedAnimal.equals(lastItem)) {
                    animal = otroAnimalEditText.getText().toString();
                } else {
                    animal = selectedAnimal;
                }


                String nombre = name.getText().toString();
                String raza = breed.getText().toString();
                String peso = weight.getText().toString();
                String fechaNacimiento = date.getText().toString();

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

                    // Obtén la referencia de la base de datos para el usuario actual

                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    String displayName = user.getDisplayName();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                    // Crea un nodo "mascotas" dentro del nodo del usuario actual
                    DatabaseReference mascotasRef = userRef.child("mascotas");

                    Pet pet = new Pet(animal, nombre, raza, peso, fechaNacimiento, base64Image);
                    pet.setImagenBase64(base64Image);



                    // Guarda la mascota en la base de datos usando la clave generada
                    mascotasRef.child(nombre).setValue(pet);

                    dismiss(); // Cerrar el diálogo
                }
            }
        });
        return view;
    }






    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
//        toolbar.setTitle("Añade una nueva mascota");
        toolbar.inflateMenu(R.menu.dialog_add_pet);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_save) {

                    // Obtén el ID del usuario actual
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser == null) {
                        // El usuario no ha iniciado sesión, maneja este caso según tus necesidades
//                        return;
                    }
                    String userId = currentUser.getUid();

                    String animal;
                    int lastPosition = animalSpinner.getAdapter().getCount() - 1;
                    String lastItem = animalSpinner.getAdapter().getItem(lastPosition).toString();
                    String selectedAnimal = animalSpinner.getSelectedItem().toString();

                    if (selectedAnimal.equals(lastItem)) {
                        animal = otroAnimalEditText.getText().toString();
                    } else {
                        animal = selectedAnimal;
                    }


                    String nombre = name.getText().toString();
                    String raza = breed.getText().toString();
                    String peso = weight.getText().toString();
                    String fechaNacimiento = date.getText().toString();

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

                        // Obtén la referencia de la base de datos para el usuario actual

                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        String displayName = user.getDisplayName();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                        // Crea un nodo "mascotas" dentro del nodo del usuario actual
                        DatabaseReference mascotasRef = userRef.child("mascotas");

                        Pet pet = new Pet(animal, nombre, raza, peso, fechaNacimiento, base64Image);
                        pet.setImagenBase64(base64Image);

                        // Guarda la mascota en la base de datos usando la clave generada
                        mascotasRef.child(nombre).setValue(pet);

                        dismiss(); // Cerrar el diálogo
                    }
                    dismiss();
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.Theme_Slide);
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Aquí obtendrás la fecha seleccionada
        // Conviértela al formato deseado y establece el texto en dateEditText
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        date.setText(selectedDate);
    }

    private void showWeightPickerDialog() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_number_picker_dialog, null);
        final NumberPicker firstNumberPicker = dialogView.findViewById(R.id.first_number);
        final NumberPicker secondNumberPicker = dialogView.findViewById(R.id.second_number);

        firstNumberPicker.setMinValue(0);
        firstNumberPicker.setMaxValue(20);
        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(100);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title_select_weight));
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.dialog_button_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedFirstNumber = firstNumberPicker.getValue();
                int selectedSecondNumber = secondNumberPicker.getValue();

                String weight = selectedFirstNumber + "." + selectedSecondNumber + " kg ";
                DialogAddPet.this.weight.setText(weight);
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_button_cancel), null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
    public class DateDialog extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }


    }




}
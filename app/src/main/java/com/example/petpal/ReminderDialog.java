package com.example.petpal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Locale;

public class ReminderDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "reminder_dialog";
    private Toolbar toolbar;
    private EditText subject;
    private EditText datePicker;
//    private TimePicker timePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FullScreenDialog);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.example_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        subject = view.findViewById(R.id.subject);
        datePicker = view.findViewById(R.id.date_picker);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), ReminderDialog.this, year, month, day);

                datePickerDialog.show();
            }
        });

        return view;
    }

//    private void guardarNotificacion() {
//        String asunto = subject.getText().toString();
//        String fecha = datePicker.getText().toString();
//
//        int dia, mes, anio;
//        try {
//            String[] fechaSplit = fecha.split("/");
//            dia = Integer.parseInt(fechaSplit[0]);
//            mes = Integer.parseInt(fechaSplit[1]) - 1; // Restar 1 al mes ya que Calendar.MONTH comienza desde 0
//            anio = Integer.parseInt(fechaSplit[2]);
//        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
//            Toast.makeText(this, "Por favor, ingrese una fecha válida en el formato dd/mm/YYYY", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
////        int hora, minutos;
////        if (Build.VERSION.SDK_INT >= 23) {
////            hora = timePicker.getHour();
////            minutos = timePicker.getMinute();
////        } else {
////            hora = timePicker.getCurrentHour();
////            minutos = timePicker.getCurrentMinute();
////        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(anio, mes, dia, hora, minutos);
//
//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            Toast.makeText(this, "Por favor, seleccione una fecha y hora futura", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Crear una notificación
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String channelId = "default_channel_id";
//        String channelName = "Default Channel";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setContentTitle("Recordatorio")
//                .setContentText(asunto)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        notificationManager.notify(0, builder.build());
//
//        Toast.makeText(this, "Notificación programada", Toast.LENGTH_SHORT).show();
//        finish();
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
//        toolbar.setTitle("Some Title");
        toolbar.inflateMenu(R.menu.example_dialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_save) {



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
        datePicker.setText(selectedDate);
    }


}
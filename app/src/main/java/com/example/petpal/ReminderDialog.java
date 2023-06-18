package com.example.petpal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

import java.util.Calendar;
import java.util.Locale;

public class ReminderDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;
    private EditText subject;
    private EditText datePicker;
    private EditText timePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.reminder_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        subject = view.findViewById(R.id.subject);
        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);

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

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), ReminderDialog.this, 0, 0, true);
                timePickerDialog.show();
            }
        });

        return view;
    }

    private void guardarNotificacion() {
        String titulo = "Tu mascota te necesita";
        String mensaje = subject.getText().toString();
        String fecha = datePicker.getText().toString();
        String tiempo = timePicker.getText().toString();

        NotificationUtils.scheduleNotification(requireContext(), fecha, tiempo, mensaje, titulo);
        Toast.makeText(requireContext(), "Notificación programada", Toast.LENGTH_SHORT).show();
    }


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

//                    String fecha = "18/06/2023";
//                    String hora = "10:21";
//                    String mensaje = "¡Es hora de tu notificación!";
//                    NotificationUtils.scheduleNotification(requireContext(), fecha, hora, mensaje);
                    guardarNotificacion();
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
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        datePicker.setText(selectedDate);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        timePicker.setText(selectedTime);
    }
}
package com.example.petpal;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotificationUtils {

    public static void scheduleNotification(Context context, String fecha, String hora, String mensaje, String titulo) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Obtén la fecha y hora actual en milisegundos
        long currentTimeMillis = System.currentTimeMillis();

        // Calcula la fecha y hora deseada en milisegundos
        long notificationTimeMillis = calculateNotificationTimeMillis(fecha, hora);

        // Verifica si la fecha y hora ya pasaron
        if (notificationTimeMillis <= currentTimeMillis) {
            // La fecha y hora ya pasaron, no se programa la notificación
            return;
        }

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("mensaje", mensaje);
        intent.putExtra("titulo", titulo);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent);
        }
        System.out.println("ccccccccccccccccccc");
    }

    private static long calculateNotificationTimeMillis(String fecha, String hora) {
        // Aquí debes implementar la lógica para convertir la fecha y hora en milisegundos
        // Puedes usar las clases SimpleDateFormat y Calendar para realizar la conversión
        // Ejemplo:
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
         SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
         Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(fecha));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            calendar.set(Calendar.HOUR_OF_DAY, timeFormat.parse(hora).getHours());
            calendar.set(Calendar.MINUTE, timeFormat.parse(hora).getMinutes());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
         return calendar.getTimeInMillis();

    }
}

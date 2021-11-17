package com.kakusei.simpletodolist.sevice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kakusei.simpletodolist.entity.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmTimer {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static void setAlarmTimer(Context context, Event event, Integer AlarmManagerType) {
        if (event == null) {
            throw new NullPointerException("Event can not be null.");
        }
        try {
            Log.d("kakusei","AlarmTimer:" + event.toString());
            Date date = simpleDateFormat.parse(event.getTime());
            Intent intent = new Intent(context,AlarmReceiver.class);
            intent.putExtra("eventTitle", event.getTitle());
            Integer alarmId = Integer.parseInt(event.getId().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId ,intent,0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManagerType, date.getTime(),pendingIntent);
            alarmManager.set(AlarmManagerType, date.getTime(),pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static void cancelAlarmTimer(Context context) {
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, myIntent,0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
    }
}

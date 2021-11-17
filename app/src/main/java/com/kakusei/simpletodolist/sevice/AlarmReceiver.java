package com.kakusei.simpletodolist.sevice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kakusei.simpletodolist.MainActivity;
import com.kakusei.simpletodolist.R;
import com.kakusei.simpletodolist.entity.Event;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
//        Event event = (Event) intent.getParcelableExtra("event");
//        Log.d("kakusei", "-------" + event.toString());
//        if (event == null) {
//            throw new NullPointerException("Event info was empty.");
//        }
//        Log.d("kakusei",intent.getStringExtra("eventTitle"));
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,mainIntent,0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("notification","Notification",NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(context,"notification")
                .setContentTitle("You have one event unfinished.")
                .setContentText(intent.getStringExtra("eventTitle"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_floatactionbutton_done)
                .build();
        notificationManager.notify(1,notification);
        AlarmTimer.cancelAlarmTimer(context);
    }
}
package com.example.fhir_communication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private Context context;
    private NotificationManager manager;
    private static final String CHANNEL_ID = "tasks_notification_channel";
    private final int NOTIFICATION_ID = 0;

    public NotificationHandler(Context context){
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Tasks Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Noti");
        this.manager.createNotificationChannel(channel);

    }

    public void send(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle("Task application")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_saved);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }
}

package com.example.tp62;
import androidx.work.WorkManager;
import androidx.work.ExistingWorkPolicy;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "backup_channel";

    public static void createNotificationChannel(Context context) {

        NotificationChannel channel =
                new NotificationChannel(
                        CHANNEL_ID,
                        "Sauvegarde",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

        channel.setDescription("Notifications de sauvegarde");

        NotificationManager manager =
                context.getSystemService(NotificationManager.class);

        manager.createNotificationChannel(channel);
    }

    public static void showNotification(Context context) {

        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_menu_save)
                        .setContentTitle("Sauvegarde terminée")
                        .setContentText(
                                "Données sauvegardées avec succès !"
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify(1, builder.build());
    }
}
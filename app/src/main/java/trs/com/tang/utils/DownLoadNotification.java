package trs.com.tang.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;

import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DownLoadNotification {

    private Activity activity;
    private NotificationManager notificationManager;
    private Notification notification;

    public DownLoadNotification(Activity activity){
        this.activity = activity;
        notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);

    }

    public void setNotification(int id,NotificationCompat.Builder builder){
        notification = builder.build();
        notificationManager.notify(id,notification);
    }

    public void upDataNotification(int id,NotificationCompat.Builder builder){
        notificationManager.notify(id,builder.build());
    }

}

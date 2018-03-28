package uk.co.yellowrays.collegeinfo.classcubby.Server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.File;
import java.util.Date;
import java.util.Random;

import uk.co.yellowrays.collegeinfo.classcubby.MainActivity;
import uk.co.yellowrays.collegeinfo.classcubby.R;

/**
 * Created by Vino on 10/10/2017.
 */

public class GCMPushReceiverService extends GcmListenerService {


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";

    @Override
    public void onMessageReceived(String from, final Bundle data) {

        deleteCache(getApplicationContext());

        final String message = data.getString("message");
        final String title = data.getString("titledata");
        final String type = data.getString("typedata");

        sendNotification(message, title, type);

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    private void sendNotification(String message, String title, String type) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        int dummyuniqueInt = new Random().nextInt(543254);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, dummyuniqueInt, intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder=new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setSubText(type)
                .setContentIntent(pendingIntent);

        Notification inboxStyle = new Notification.InboxStyle(builder)
                        .addLine(message)
                        .setBigContentTitle(title)
                        .setSummaryText(type)
                        .build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE),inboxStyle);
    }

}

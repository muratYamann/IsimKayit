package com.yamankod.isimkayit.firebase;

/**
 * Created by murat on 31.01.2017.
 */

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yamankod.isimkayit.R;
import com.yamankod.isimkayit.activity.MainActivity;
import com.yamankod.isimkayit.activity.PushMessages;
import com.yamankod.isimkayit.util.TinyDB;

import java.util.Map;

public class MyFirebaseMessagingService1 extends FirebaseMessagingService {

    Bundle bundle ;
    private static final String TAG = "MyFirebaseMsgService";

    TinyDB tinyDB;

    String message;
    String image ;
    String content;
    String date ;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        tinyDB = new TinyDB(getApplicationContext());

        if (remoteMessage.getData().size() > 0) {
            // Data mesajı içeriyor mu
            //Uygulama arkaplanda veya ön planda olması farketmez. Her zaman çağırılacaktır.
            //Gelen içerik json formatındadır.
            Log.d(TAG, "Mesaj data içeriği: " + remoteMessage.getData());


            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d(TAG, "key, " + key + " value " + value);
            }
            message = remoteMessage.getData().get("message");
            Log.d(TAG, "onMessageReceived: "+message);
            tinyDB.putString("message",message);


            //Json formatındaki datayı parse edip kullanabiliriz. Biz direk datayı Push Notification olarak bastırıyoruz


            //new DownloadTask(MyFirebaseMessagingService.this, remoteMessage.getData().toString());
            sendNotification("İsim Kayit", "" + remoteMessage.getData().toString());

        }

            if (remoteMessage.getNotification() != null) {
                //Notification mesajı içeriyor mu
                //Uygulama arkaplanda ise burası çağrılmaz.Ön planda ise notification mesajı geldiğinde çağırılır
                //getBody() ile mesaj içeriği
                //getTitle() ile mesaj başlığı
                Log.d(TAG, "Mesaj Notification Başlığı: " + remoteMessage.getNotification().getTitle() + " " + "Mesaj Notification İçeriği: " + remoteMessage.getNotification().getBody());
                //remoteMessage.getNotification().getBody()


                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }


    }

    private void sendNotification(String messageTitle,String messageBody) {

        Log.d(TAG, "sendNotificationMbody: "+messageBody);
        Log.d(TAG, "sendNotificationMTitle: "+messageTitle);

        String outputData = messageBody;
        int locationMessageEnd = outputData.indexOf("=");
        String parseOutput = outputData.substring(locationMessageEnd+1,messageBody.length()-1);

        Log.d(TAG, "onMessageReceived: parseOutput : "+parseOutput);

        Intent intent = new Intent(this, PushMessages.class);
        bundle = new Bundle();
        bundle.putString("data",parseOutput);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] pattern = {1000,1000,500,500};//Titreşim ayarı

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(messageTitle)
                .setContentText(parseOutput)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + this.getPackageName() + "/raw/isimkayit");
            Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
           // r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
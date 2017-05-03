package com.yamankod.isimkayit.firebase;

/**
 * Created by Muratymn.72@gmail.com on 20.04.2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yamankod.isimkayit.util.TinyDB;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    TinyDB tinyDB;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: "+refreshedToken);
        // Saving reg id to shared preferences
        tinyDB = new TinyDB(getApplicationContext());
        tinyDB.putString("regId",refreshedToken);

        Log.d(TAG, "onTokenRefresh: "+refreshedToken.toString());
        // sending reg id to your server
       //sendRegistrationToServer2(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }





    private void sendRegistrationToServer2(final String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("reg_id",token)
                .add("user_name","Murat")
                .add("user_pass","$10$Li7MlgRECjQetnLDky.z1OCc0B5kwHC7uGxcTWKcBoKBWu.txRlIW")
                .build();

        Log.d(TAG, "sendRegistrationToServer: "+token);

        Request request = new Request.Builder()

                .url("http://monitor.isimkayit.com/push_notification/reg_id_kayit.php")
                //  .url("http://aryenhaber20.co.uk/md_notification/push_notification_gonder.php")
                .post(body)
                .build();

        try {

            Call call = client.newCall(request);
            Response response = call.execute();

            Log.d(TAG, "sendRegistrationToServer 1: "+response.body().string());
            Log.d(TAG, "sendRegistrationToServer:2 "+response.body().toString());
            Log.d(TAG, "sendRegistrationToServer:3 "+response.message());
            Log.d(TAG, "sendRegistrationToServer:4 "+response.isSuccessful());


            System.out.println("YEAH: " + response.body().string());

            if(!response.isSuccessful()) {
                Log.i("Response code", " " + response.code());
            }

            Log.i("Response code", response.code() + " ");
            String results = response.body().toString();

            Log.i("OkHTTP Results: ", results);



        }catch (Exception e){

        }



    }



}
package com.yamankod.isimkayit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.yamankod.isimkayit.R;
import com.yamankod.isimkayit.util.TinyDB;


public class SplashScreen extends AppCompatActivity {

    private  static String TAG = "Splash";
    TinyDB tinyDB;
    private String name;
    private String pass;
    private boolean loginstatus = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tinyDB = new TinyDB(getApplicationContext());
        loginstatus = tinyDB.getBoolean("loginStatus");

        Thread mSplashThread;

        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                    }

                    try {
                        name = tinyDB.getString("userName");
                        pass = tinyDB.getString("userPass");

                        Log.d(TAG, "run name: "+name);
                        Log.d(TAG, "run pass: "+pass);


                        
                        if (loginstatus) {
                            Log.d(TAG, "run: not null ");
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                            System.exit(1);
                            System.gc();

                       } else {
                            Log.d(TAG, "run: null");
                            startActivity(new Intent(SplashScreen.this,LoginInfo.class));
                            finish();
                            System.gc();
                            System.exit(1);

                        }
                    }catch (Exception e){}

                } catch (InterruptedException ex) {
                }

            }
        };
        mSplashThread.start();

    }

    }


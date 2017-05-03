package com.yamankod.isimkayit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.yamankod.isimkayit.R;
import com.yamankod.isimkayit.fragment.FragmentMSL_Monitor_web;
import com.yamankod.isimkayit.util.Const;
import com.yamankod.isimkayit.util.TinyDB;

/**
 * Created by macbookpro on 27.04.2017.
 */

public class PushMessages extends Activity {
    private TextView tvMessages;
    TinyDB tinyDB;
    Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_messages);
        tinyDB = new TinyDB(getApplicationContext());


        String message = tinyDB.getString("message");
        tvMessages = (TextView)findViewById(R.id.tvmessage);
        tvMessages.setText(message);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);


    }
}

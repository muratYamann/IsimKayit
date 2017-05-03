package com.yamankod.isimkayit.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yamankod.isimkayit.R;
import com.yamankod.isimkayit.WebService;
import com.yamankod.isimkayit.util.TinyDB;

import org.w3c.dom.Text;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginInfo extends AppCompatActivity {
    private static String TAG ="loginInfo";
    WebView webView;
    EditText etAd, etPas;
    Bundle bundle;
    Button btnn;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        tinyDB = new TinyDB(getApplicationContext());


        etAd = (EditText)findViewById(R.id.kadi);
        etPas = (EditText)findViewById(R.id.kpass);
        btnn = (Button)findViewById(R.id.btn);

        bundle = new Bundle();



        btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String userName = etAd.getText().toString().trim();
                    String userPass = etPas.getText().toString().trim();
                    String regID = tinyDB.getString("regId");

                    Log.d(TAG, "onClick: username:" + userName + "\npass:" + userPass + "\nregId:" + regID);

                    if (checkNetworkStatus()) {


                        if (!userName.isEmpty() && !userPass.isEmpty() && !regID.isEmpty()) {


                            tinyDB.putString("userName", userName);
                            tinyDB.putString("userPass", userPass);
                            tinyDB.putString("url", "https://monitor.isimkayit.com//");
                            tinyDB.putBoolean("loginStatus", true);


                            bundle.putString("ad", userName);
                            bundle.putString("pas", userPass);

                            sendRegistrationToServer2(regID, userName, userPass);

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            System.gc();
                            System.exit(1);
                        } else {
                            Toast.makeText(LoginInfo.this, "Değerler Boş geçilemez...!!!", Toast.LENGTH_SHORT).show();
                        }

                    } else{
                        showDialogOK(getString(R.string.error_msg), getString(R.string.app_name), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    }

                    }catch(Exception e){
                        System.out.print(e);
                        System.err.print(e);
                    }
            }
        });

    }


    private void sendRegistrationToServer2(final String token,String name,String pass) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("reg_id",token)
                .add("user_name",name)
                .add("user_pass",pass)
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


            if(!response.isSuccessful()) {
                Log.i("Response code", " " + response.code());
            }

            Log.i("Response code", response.code() + " ");
            String results = response.body().toString();

            Log.i("OkHTTP Results: ", results);
        }catch (Exception e){}
    }



    private boolean checkNetworkStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


            /*Dialog for Asking permission*/
            private void showDialogOK(String message, String title, DialogInterface.OnClickListener okListener) {
                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setTitle(title)
                        .setPositiveButton(getString(R.string.dialog_OK), okListener)
                        .create()
                        .show();
            }


        }
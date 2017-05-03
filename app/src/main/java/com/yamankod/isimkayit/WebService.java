package com.yamankod.isimkayit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by murat on 19.01.2017.
 */

public class WebService extends AsyncTask<String ,Void,String> {

    int indexOf =0;
    Context ctx;
    private String TAG = "background";
    private HttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> nameValuePair;

    JSONObject jsonObject;
    private ProgressDialog dialog;

    public WebService(Context ctx) {
        this.ctx = ctx;
        dialog = new ProgressDialog(ctx);
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_id = params[0];
        String name = params[1];
        String pass = params[2];


        //Date & Time
        Calendar c = Calendar.getInstance();
        jsonObject = new JSONObject();
        String json = "";

        try {

            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost("https://www.monitor.isimkayit.com/reg_id_save.php");

            jsonObject.put("reg_id",reg_id);
            jsonObject.put("user_name",name);
            jsonObject.put("user_pass",pass);

            nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("parametre", jsonObject.toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
         Log.d(TAG, "makePostRequest: Jsonn" + jsonObject.toString());

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //making POST request.
        try{

            HttpResponse response = httpClient.execute(httpPost);
            json = EntityUtils.toString(response.getEntity());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = "gonderiliyor...";
        this.dialog.setMessage(message);
     }

    @Override
    protected void onPostExecute(String results) {
        this.dialog.dismiss();
        Log.d(TAG, "onPostExecute:  Results =  "+results.toString());

    }
}

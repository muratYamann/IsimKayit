package com.yamankod.isimkayit.fragment;


/**
 * Created by Muratymn.72@gmail.com on.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yamankod.isimkayit.R;
import com.yamankod.isimkayit.util.TinyDB;

public class FragmentMSL_Sunucu_web extends  Fragment {
    private static String TAG ="web_";
    private String url;
    private WebView webView;
    private float xCor;
    private TinyDB tinyDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_msl_sunucu_web, container, false);
        tinyDB = new TinyDB(rv.getContext());
        //--

        url = String.valueOf(getArguments().get("url"));
        Log.d(TAG, "onCreateView: "+url);
        webView = (WebView)rv.findViewById(R.id.load_url_web_view);

        webView.setWebChromeClient(new MyWebChromeClient(getActivity()));


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            public void onPageFinished(WebView view, String url) {

                String user = tinyDB.getString("userName");
                String pwd = tinyDB.getString("userPass");

                if(!user.equals(null)) {
                    view.loadUrl("javascript:document.getElementsByName('user_name')[0].value = '" + user
                            + "';document.getElementsByName('user_password')[0].value='" + pwd + "';" +
                            "$('button[type=submit]').trigger('click');\n");


                }
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);

        webView.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });


        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        {
                            xCor = event.getX();
                        }
                        break; //save the x

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: { // set x so that it doesn't move
                        event.setLocation(xCor, event.getY()); } break;
                }
                return false;
            }
        });

        webView.loadUrl(url);




        //--
        return rv;
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    private void webViewGoBack(){
        webView.goBack();
    }





    private class MyWebChromeClient extends WebChromeClient {
        Context context;
        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }


}

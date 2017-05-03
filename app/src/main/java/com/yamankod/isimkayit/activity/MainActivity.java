package com.yamankod.isimkayit.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yamankod.isimkayit.R;
import com.yamankod.isimkayit.fragment.FragmentMSL_Log_web;
import com.yamankod.isimkayit.fragment.FragmentMSL_Monitor_web;
import com.yamankod.isimkayit.fragment.FragmentMSL_Sunucu_web;
import com.yamankod.isimkayit.util.Const;
import com.yamankod.isimkayit.util.TinyDB;

public class MainActivity extends AppCompatActivity {

    private WebView mWebview;
    private WebSettings webSettings;
    private String newsURL, newsTitle, currentURL;
    private String HASHTAG;
    private Menu menu;
    private ProgressBar progressBar;
    private LinearLayout adViewTop;
    private Intent sendIntent;
    private FloatingActionButton fab;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;
    private Boolean isZoom = false;
    private int ZOOM_CONSTANT = 30;


    FragmentManager myFragmentManager;
    FragmentTransaction myFragmentTransaction;
    FragmentMSL_Monitor_web frmMonitor;
    TinyDB tinyDB;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = new Bundle();
        tinyDB = new TinyDB(getApplicationContext());

        relativeLayout = (RelativeLayout)findViewById(R.id.activity_main_layout);
        newsURL = "https://monitor.isimkayit.com/";


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the HomeFragment as the first Fragment
         */

        myFragmentManager = getSupportFragmentManager();
        frmMonitor = new FragmentMSL_Monitor_web();
        final FragmentTransaction transaction = myFragmentManager.beginTransaction();
        bundle.putString("url", Const.urlMonitor);
        frmMonitor.setArguments(bundle);
        transaction.add(R.id.content, frmMonitor).commit();



       // myFragmentManager = getSupportFragmentManager();
       // myFragmentTransaction = myFragmentManager.beginTransaction();
       // myFragmentTransaction.replace(R.id.content, new FragmentMonitor()).commit();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

                //    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                //    fragmentTransaction.replace(R.id.content, new FragmentMonitor()).commit();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nv_monitor:

                    FragmentMSL_Monitor_web frmMonitor = new FragmentMSL_Monitor_web();
                    bundle.putString("url", Const.urlMonitor);
                    frmMonitor.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, frmMonitor).commit();

                    return true;
                case R.id.nv_sunucu:
                    FragmentMSL_Sunucu_web frmSunucu = new FragmentMSL_Sunucu_web();
                    bundle.putString("url", Const.urlSunucu);
                    frmSunucu.setArguments(bundle);
                    FragmentTransaction fragmentTransactionfrmSunucu = myFragmentManager.beginTransaction();
                    fragmentTransactionfrmSunucu.replace(R.id.content, frmSunucu).commit();
                    return true;

                case R.id.nv_log:
                    FragmentMSL_Log_web frmLog = new FragmentMSL_Log_web();
                    bundle.putString("url", Const.urlLog);
                    frmLog.setArguments(bundle);
                    FragmentTransaction fragmentTransactionlog = myFragmentManager.beginTransaction();
                    fragmentTransactionlog.replace(R.id.content, frmLog).commit();

                    return true;
            }

            return false;

        }

    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

      //  getMenuInflater().inflate(R.menu.detail_menu, s);
        switch (item.getItemId()) {
            case R.id.setting:
                tinyDB.remove("userName");
                tinyDB.remove("userPass");
                tinyDB.remove("loginStatus");
                Intent i = new Intent(getApplicationContext(),LoginInfo.class);
                startActivity(i);
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            case R.id.action_about:
                aboutApp();
                return true;
            case R.id.action_check_updates:
                rateMe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




    private boolean checkNetworkStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void zoominWebview(Menu menu) {
        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_zoom_in_black_24dp));
        webSettings.setTextZoom(webSettings.getTextZoom() + ZOOM_CONSTANT);

    }

    private void zoomOutWeview(Menu menu) {
        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_zoom_in_black_24dp));
        webSettings.setTextZoom(webSettings.getTextZoom() - ZOOM_CONSTANT);
    }

    private void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + this.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    /*Description about app in popup window*/
    private void aboutApp() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.datal_activity_abaout, null);
        // Initialize a new instance of popup window
        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
        TextView textView = (TextView) customView.findViewById(R.id.tv);
        textView.setText(Html.fromHtml(getResources().getString(R.string.about_app_desc)));


        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                popupWindow.dismiss();
            }
        });

        // Closes the popup window when touch outside.
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // Removes default background.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
    }



}

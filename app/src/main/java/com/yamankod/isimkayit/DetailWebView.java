package com.yamankod.isimkayit;

/**
 * Created by Muratymn.72@gmail.com on 21.04.2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DetailWebView extends AppCompatActivity {

    private WebView mWebview;
    private WebSettings webSettings;
    private String newsURL, newsTitle, currentURL;
    private String HASHTAG;
    private Menu menu;
    private ProgressBar progressBar;
    private Bundle bundle;
    private LinearLayout adViewTop;
    private Intent sendIntent;
    private FloatingActionButton fab;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_webview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadNewsPaper();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            newsURL = "https://monitor.isimkayit.com/";//bundle.getString("NEWS_URL");
            //newsTitle = bundle.getString("NEWS_TITLE", "DAILYNEWS");
        }
        /*Setting the current news url*/

        mWebview = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        relativeLayout = (RelativeLayout) findViewById(R.id.detail_news);



        progressBar.setProgress(0);
        setTitle(newsTitle);

        /*Configuring the webview*/
        webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= 19) {
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebview.setWebViewClient(new NewsWebViewClient());

        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, final int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setProgress(progress);
                }
            }
        });

        mWebview.loadUrl(newsURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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

    private class NewsWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            currentURL = url;
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void shareNews(String currentURL) {
        sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, currentURL + HASHTAG);
        sendIntent.setType("text/plain");
        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        currentURL = newsURL;

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
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

    /*Start the Activity */
    private void loadNewsPaper() {
        if (!checkNetworkStatus()) {
            showDialogOK(getString(R.string.error_msg), getString(R.string.app_name), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    private boolean checkNetworkStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
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
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
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
package com.test.inshortsclone.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.test.inshortsclone.R;

public class LoadWebsiteActivity extends AppCompatActivity {
    private static final String TAG = LoadWebsiteActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_website);

        final ProgressDialog progressDialog = new ProgressDialog(this);

        //To display the up navigation arrow in Action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //For getting Article URL from VerticalPagerAdapter
        Bundle bundle = getIntent().getExtras();
        String articleUrl = bundle.getString("newsArticleUrl");
        //Log.i(TAG, "onCreate: newsArticleUrl: " + articleUrl);

        //For loading website
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.setMessage("Loading content for you...");
                progressDialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(LoadWebsiteActivity.this, error.getDescription().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });

        webView.loadUrl(articleUrl);

    }


}

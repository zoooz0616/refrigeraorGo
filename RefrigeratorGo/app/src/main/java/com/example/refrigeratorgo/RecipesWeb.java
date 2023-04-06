package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class RecipesWeb extends AppCompatActivity {

    public WebView webView;
    private String url = "http://www.10000recipe.com/recipe/list.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_web);

        Intent intent = getIntent();
        String receiveStr = intent.getStringExtra("editText"); //edit텍스트 값 데려옴

        Log.i(String.valueOf(intent), "ip");


//        http://www.10000recipe.com/recipe/list.html?q=%EB%B3%B6%EC%9D%8C%EB%B0%A5
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url + "?q=" + receiveStr);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            ;
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}


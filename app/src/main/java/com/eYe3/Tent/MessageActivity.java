package com.eYe3.Tent;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MessageActivity extends AppCompatActivity {
    WebView messageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageView=findViewById(R.id.message_webview);
        String Url=getIntent().getExtras().getString("url");

        messageView.setWebViewClient(new WebViewClient() {
            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                messageView.loadUrl("file:///android_asset/web/error_file.html");
            } });
        messageView.loadUrl(Url);
    }
}

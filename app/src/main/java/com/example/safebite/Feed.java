package com.example.safebite;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebChromeClient;
import android.net.Uri;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Feed extends AppCompatActivity {
    private static final String TAG = "FeedActivity";
    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String postUrl;
    private byte[] postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fee), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView = findViewById(R.id.webview);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.getSettings().setDomStorageEnabled(true); // Enable DOM storage if needed
        webView.setWebChromeClient(new WebChromeClient());

        // Get the URL from the intent
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView.loadUrl(url);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "SwipeRefresh triggered");
                if (postUrl != null && postData != null) {
                    webView.postUrl(postUrl, postData);
                } else {
                    webView.reload();
                }
            }
        });
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "Page finished loading: " + url);
            swipeRefreshLayout.setRefreshing(false);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                postUrl = uri.toString();
                // Handling POST data manually is complex and may require server-side support
                // Here we are assuming a simple form submission without special handling
                // Ensure the server accepts resubmission without issues.
            } else {
                postUrl = null;
                postData = null;
            }
            return false; // Returning false to let WebView handle the URL
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
    }
}

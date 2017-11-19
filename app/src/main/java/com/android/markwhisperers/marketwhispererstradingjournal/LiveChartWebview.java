package com.android.markwhisperers.marketwhispererstradingjournal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;

public class LiveChartWebview extends Activity {

    WebView webView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_live_chart_webview);

        intent = getIntent();
        intent.getExtras();

        webView = findViewById(R.id.chart);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.tradingview.com/chart/EURJPY/av1WlDy0-EURJPY-Short/");
        webView.setHorizontalScrollBarEnabled(false);

    }
}

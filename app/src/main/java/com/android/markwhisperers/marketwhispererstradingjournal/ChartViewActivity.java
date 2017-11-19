package com.android.markwhisperers.marketwhispererstradingjournal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.android.markwhisperers.marketwhispererstradingjournal.view.TouchImageView;
import com.bumptech.glide.Glide;

public class ChartViewActivity extends Activity {

    TouchImageView imageView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_chart_view);

        intent = getIntent();
        intent.getExtras();

        imageView = findViewById(R.id.chart);

        Glide.with(this).load(intent.getStringExtra("chart_url")).into(imageView);

    }
}

package com.android.markwhisperers.marketwhispererstradingjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScrollingActivity extends AppCompatActivity {

    Intent intent;
    CircleImageView displayPicture;
    ImageView mChart;

    TextView mPosition, mStopLoss, mTakeProfit, mLotSize, lossAmount, profitAmount;
    TextInputEditText mAccountBalance, mRiskPercentage;

    Button btnCalculateLotSize, btnOpenLiveChart;

    String displayPictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        intent = getIntent();
        intent.getExtras();

        displayPictureUrl = intent.getStringExtra("url");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("pair"));
        setSupportActionBar(toolbar);

        displayPicture = findViewById(R.id.display_picture);
        mChart = findViewById(R.id.image);

        Glide.with(this).load(displayPictureUrl).into(mChart);
        Glide.with(this).load("https://scontent-amt2-1.cdninstagram.com/t51.2885-15/e35/21568724_197161080826062_7393434884624089088_n.jpg").into(displayPicture);

        displayPicture.requestFocus();


        mPosition = findViewById(R.id.position);
        mStopLoss = findViewById(R.id.stop_loss);
        mTakeProfit = findViewById(R.id.take_profit);
        mAccountBalance = findViewById(R.id.account_balance);
        mLotSize = findViewById(R.id.lot_size);
        mRiskPercentage = findViewById(R.id.risk_percentage);
        lossAmount = findViewById(R.id.loss_amount);
        profitAmount = findViewById(R.id.profit_amount);

        btnCalculateLotSize = findViewById(R.id.btnCalculateLotSize);
        btnOpenLiveChart = findViewById(R.id.btnOpenChart);

        mPosition.setText(intent.getStringExtra("pair") + " " + intent.getStringExtra("position") + " @ " + intent.getStringExtra("price"));
        mStopLoss.setText("SL : " + intent.getStringExtra("stop_loss"));
        mTakeProfit.setText("TP : " + intent.getStringExtra("take_profit"));


        btnCalculateLotSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAccountBalance.getText().toString().trim() == "" || Double.parseDouble(mAccountBalance.getText().toString().trim()) <= 0) {
                    mAccountBalance.setError("Please insert value");
                    mAccountBalance.requestFocus();
                    return;
                }

                if (mAccountBalance.getText().toString().trim() == "" || Double.parseDouble(mAccountBalance.getText().toString().trim()) <= 0) {
                    mAccountBalance.setError("Please insert value");
                    mAccountBalance.requestFocus();
                    return;
                }

                calculateLotSize();
            }
        });

        btnOpenLiveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScrollingActivity.this, LiveChartWebview.class));
            }
        });

        FloatingActionButton fabButton = findViewById(R.id.view_chart);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChartViewActivity.class);
                intent.putExtra("chart_url", displayPictureUrl);
                startActivity(intent);
            }
        });


    }

    public void calculateLotSize() {
        Double riskAmount = (Double.parseDouble(mAccountBalance.getText().toString().trim()) * ((Double.parseDouble(mRiskPercentage.getText().toString().trim())) / 100));
        Double lotSize = (riskAmount / Double.parseDouble(intent.getStringExtra("pips_to_sl")));

        mLotSize.setText("use " + new DecimalFormat("##.##").format(lotSize));
        lossAmount.setText("-$" + new DecimalFormat("##.##").format(lotSize * Double.parseDouble(intent.getStringExtra("pips_to_sl"))));
        profitAmount.setText("+$" + new DecimalFormat("##.##").format(lotSize * Double.parseDouble(intent.getStringExtra("pips_to_tp"))));

    }

}

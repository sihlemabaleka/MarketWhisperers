package com.android.markwhisperers.marketwhispererstradingjournal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.container);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();

    }

}


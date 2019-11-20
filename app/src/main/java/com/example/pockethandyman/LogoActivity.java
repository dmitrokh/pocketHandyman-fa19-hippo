package com.example.pockethandyman;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        /*Duration of wait*/
        int LOGO_DISPLAY_DURATION = 2000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the MainActivity. */
                Intent mainIntent = new Intent(LogoActivity.this, EmailPasswordActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, LOGO_DISPLAY_DURATION);
    }
}

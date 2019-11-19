package com.example.pockethandyman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Integer buttonSelector = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Button homeAppliances = (Button) findViewById(R.id.Home_appliance_button);
        homeAppliances.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 0;
                OpenButtonActivity();
            }
        });

        Button officeElectronics = (Button) findViewById(R.id.Office_electronics);
        officeElectronics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 1;
                OpenButtonActivity();
            }
        });

        Button automotive = (Button) findViewById(R.id.Automotive);
        automotive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 2;
                OpenButtonActivity();
            }
        });

        Button homeRepairs = (Button) findViewById(R.id.Home_Repairs);
        homeRepairs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 3;
                OpenButtonActivity();
            }
        });

    }



    public void OpenButtonActivity() {
        Intent intent = new Intent(this, ButtonActivity.class);

        if (buttonSelector == 0) {
            intent.putExtra("ActivityName", "Home Appliances");
        }
        else if (buttonSelector == 1) {
            intent.putExtra("ActivityName", "Office Electronics");
        }
         else if (buttonSelector == 2) {
            intent.putExtra("ActivityName", "Automotive");
        }
         else {
            intent.putExtra("ActivityName", "Home Repairs");
        }

        startActivity(intent);
    }
}

package com.urbantechies.fetch_me_up.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.urbantechies.fetch_me_up.R;

public class startup extends AppCompatActivity {

    private Button driver_btn;
    private Button passenger_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        driver_btn = findViewById(R.id.driverbtn);
        passenger_btn = findViewById(R.id.passengerbtn);

        driver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(startup.this, logindriver.class);
                startActivity(toLogin);
            }
        });

    }
}

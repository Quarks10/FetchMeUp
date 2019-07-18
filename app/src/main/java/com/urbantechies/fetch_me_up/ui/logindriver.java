package com.urbantechies.fetch_me_up.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.urbantechies.fetch_me_up.R;
import com.urbantechies.fetch_me_up.drivers.HomePage;

public class logindriver extends AppCompatActivity {

    private Button signin_btn;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logindriver);

        signin_btn = findViewById(R.id.signinbtn);
        signup_btn = findViewById(R.id.signupbtn);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomepage = new Intent(logindriver.this, HomePage.class);
                startActivity(toHomepage);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUp = new Intent(logindriver.this, signupdriver.class);
                startActivity(toSignUp);
            }
        });


    }
}

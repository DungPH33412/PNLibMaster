package com.example.pnlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class WelComeActivity extends AppCompatActivity {
    private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
        imgLogo= findViewById(R.id.imgLogo);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelComeActivity.this, LoginActivity.class));
                finish();
            }
        });
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelComeActivity.this, LoginActivity.class));
                finish();
            }
        },1500);*/
    }
}
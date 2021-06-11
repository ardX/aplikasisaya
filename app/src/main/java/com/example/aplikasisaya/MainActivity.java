package com.example.aplikasisaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSharedPreferences("MOBILE", 0) == null || !getSharedPreferences("MOBILE", 0).contains("user")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        ((TextView) findViewById(R.id.txtuser)).setText(getSharedPreferences("MOBILE", 0).getString("user", ""));
        ((TextView) findViewById(R.id.txtemail)).setText(getSharedPreferences("MOBILE", 0).getString("email", ""));
        ((TextView) findViewById(R.id.txtphone)).setText(getSharedPreferences("MOBILE", 0).getString("ponsel", ""));

        findViewById(R.id.btnGempa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabActivity.class));
            }
        });
        findViewById(R.id.btnkeluar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("MOBILE", 0).edit().clear().apply();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
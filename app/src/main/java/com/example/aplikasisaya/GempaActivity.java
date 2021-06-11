package com.example.aplikasisaya;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class GempaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gempa);

        String tanggaljam = getIntent().getStringExtra("tanggaljam");
        String lintangbujur = getIntent().getStringExtra("lintangbujur");
        String magnitudekedalaman = getIntent().getStringExtra("magnitudekedalaman");
        String wilayah = getIntent().getStringExtra("wilayah");
        String url = getIntent().getStringExtra("url");

        Picasso.get().load(url).into((ImageView) findViewById(R.id.imageView));
        ((TextView) findViewById(R.id.tanggaljam)).setText(tanggaljam);
        ((TextView) findViewById(R.id.lintangbujur)).setText(lintangbujur);
        ((TextView) findViewById(R.id.magnitudekedalaman)).setText(magnitudekedalaman);
        ((TextView) findViewById(R.id.wilayah)).setText(wilayah);
    }
}
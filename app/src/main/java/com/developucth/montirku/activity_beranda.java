package com.developucth.montirku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class activity_beranda extends AppCompatActivity {

    ImageButton btn_bengkel, btn_servis, btn_darurat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        btn_bengkel = findViewById(R.id.btn_bengkel);
        btn_servis = findViewById(R.id.btn_servis);
        btn_darurat = findViewById(R.id.btn_darurat);

        btn_bengkel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_beranda.this, Maps.class));
            }
        });

        btn_servis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_beranda.this, Service_rutin.class));
            }
        });

        btn_darurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_beranda.this, Activity_layanan_darurat.class));
            }
        });

    }
}
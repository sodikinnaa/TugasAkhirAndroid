package com.developucth.montirku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class home extends AppCompatActivity {







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.your_placeholder, new fragment_home(), "Fitur").
                commit();

    }
}
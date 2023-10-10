package com.example.fondosdepantalla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        final  int DURACION=3000;

        //USO DEL OBJETO HANDLER
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //codigo que se ejecutara
                Intent intent=new Intent(Carga.this, MainActivityAdministrador.class);
                startActivity(intent);
                finish();
            }
        },DURACION);
    }
}
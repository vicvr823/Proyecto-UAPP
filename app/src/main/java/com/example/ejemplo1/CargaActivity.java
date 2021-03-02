package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        Thread tCarga = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(4000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {

                    Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(iLogin);
                    finish();
                }
            }
        };
        tCarga.start();
    }
}
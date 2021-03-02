package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    String alumno = "";
    String profesor = "";
    String tipo = "";

    Button jbtnCursos, jbtnRegistro, jbtnNotas, jbtnCerarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        jbtnCursos = findViewById(R.id.btnCursos);
        jbtnCursos.setOnClickListener(this);

        jbtnRegistro = findViewById(R.id.btnRegistro);
        jbtnRegistro.setOnClickListener(this);

        jbtnNotas = findViewById(R.id.btnNotas);
        jbtnNotas.setOnClickListener(this);

        jbtnCerarSesion = findViewById(R.id.btnCerrarSesion);
        jbtnCerarSesion.setOnClickListener(this);

        tipo = this.getIntent().getStringExtra("tipo");
        TextView jlblTipoUsuario = findViewById(R.id.lblTipoUsuario);
        TextView jtxtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        jtxtNombreUsuario.setEnabled(false);

        if (tipo.equals("P")) {
            profesor = this.getIntent().getStringExtra("usuario");
            jlblTipoUsuario.setText("Profesor: ");
            jtxtNombreUsuario.setText(profesor);
        } else {
            alumno = this.getIntent().getStringExtra("usuario");

            jlblTipoUsuario.setText("Alumno: ");
            jtxtNombreUsuario.setText(alumno);

            jbtnRegistro.setEnabled(false);
            jbtnRegistro.setVisibility(View.GONE);
        }
       // jbtnNotas.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCursos:
                navegarACursos();
                break;

            case R.id.btnRegistro:
                navegarARegistros();
                break;

            case R.id.btnNotas:
                navegarAVerRegistros();
                break;

            case R.id.btnCerrarSesion:
                CerrarSesion();
        }
    }

    private void navegarAVerRegistros() {
        Intent iVerRegistros = new Intent(getApplicationContext(), VerRegistrosActivity.class);
        iVerRegistros.putExtra("alumno", alumno);
        iVerRegistros.putExtra("tipo", tipo); // Alumno (A), Profesor (P)
        iVerRegistros.putExtra("profesor", profesor);
        startActivity(iVerRegistros);
    }


    private void navegarACursos() {
        Intent iCursos = new Intent(getApplicationContext(), CursosActivity.class);
        iCursos.putExtra("alumno", alumno);
        iCursos.putExtra("tipo", tipo); // Alumno (A), Profesor (P)
        iCursos.putExtra("profesor", profesor);
        startActivity(iCursos);

    }
    private void navegarARegistros() {
        Intent iRegistro = new Intent(getApplicationContext(), RegistrosActivity.class);
        iRegistro.putExtra("profesor", profesor);
        startActivity(iRegistro);

    }

    private void CerrarSesion() {
        Intent iCerrar = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(iCerrar);
        finish();
    }
}
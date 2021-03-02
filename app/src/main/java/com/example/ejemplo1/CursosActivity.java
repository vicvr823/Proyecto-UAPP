package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ejemplo1.webservices.ExitoObserver;
import com.example.ejemplo1.webservices.WebServiceClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CursosActivity extends AppCompatActivity implements View.OnClickListener {

    WebServiceClient cliente = WebServiceClient.client();


    TextView curso1, curso2, curso3, curso4;

    String alumno = "";
    String profesor = "";
    String tipo = "";
    String cursos[] = {"", "", "", ""};

    Button jbtnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        jbtnMenu = findViewById(R.id.btnMenu);
        jbtnMenu.setOnClickListener(this);

        curso1 = findViewById(R.id.lblCurso1);
        curso2 = findViewById(R.id.lblCurso2);
        curso3 = findViewById(R.id.lblCurso3);
        curso4 = findViewById(R.id.lblCurso4);

        curso1.setOnClickListener(this);
        curso2.setOnClickListener(this);
        curso3.setOnClickListener(this);
        curso4.setOnClickListener(this);

        tipo = this.getIntent().getStringExtra("tipo");
        alumno = this.getIntent().getStringExtra("alumno");
        profesor = this.getIntent().getStringExtra("profesor");

        rellenarCursos();
    }

    private void rellenarCursos() {
        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                TextView[] textViews = {curso1, curso2, curso3, curso4};
                for (int i = 0; i < jsonArray.length() && i < textViews.length; i++) {
                    JSONObject curso = jsonArray.getJSONObject(i);
                    String cod = curso.getString("cod_curso");
                    String descripcion = curso.getString("descripcion");
                    textViews[i].setText(cod + " - " + descripcion);
                    cursos[i] = cod;
                }

            }
        };
        RequestParams params = new RequestParams();
        params.add("tipo", tipo);
        params.add("alumno", alumno);
        params.add("profesor", profesor);
        cliente.get("cursos.php", params, observer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMenu:
                super.onBackPressed();
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.lblCurso1:
                navegarANotas(0);
                break;
            case R.id.lblCurso2:
                navegarANotas(1);
                break;
            case R.id.lblCurso3:
                navegarANotas(2);
                break;
            case R.id.lblCurso4:
                navegarANotas(3);
                break;
        }
    }

    private void navegarANotas(int c) {
        if (cursos[c].isEmpty()) return;

        Intent iNotas = new Intent(getApplicationContext(), NotasActivity.class);
        iNotas.putExtra("alumno", alumno);
        iNotas.putExtra("tipo", tipo); // Alumno (A), Profesor (P)
        iNotas.putExtra("profesor", profesor);
        iNotas.putExtra("curso", cursos[c]);
        startActivity(iNotas);

    }
}
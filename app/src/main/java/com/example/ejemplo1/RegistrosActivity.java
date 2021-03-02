package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ejemplo1.webservices.ExitoObserver;
import com.example.ejemplo1.webservices.WebServiceClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    WebServiceClient cliente = WebServiceClient.client();

    Spinner jspCiclos, jspAlumnos, jspCursos;
    String profesor = "";
    String tipo = "P";//Profesor (P)
    String alumno = "";
    String curso = "";
    String registro = "";

    ArrayList<String> idCiclos, codAlumnos, codCursos;

    Button jbtnRegistrar, jbtnIrNotas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);


        jspCiclos = findViewById(R.id.spCiclos);
        jspAlumnos = findViewById(R.id.spAlumnos);
        jspCursos = findViewById(R.id.spCursos);

        jbtnRegistrar = findViewById(R.id.btnRegistrar);
        jbtnRegistrar.setOnClickListener(this);

        jbtnIrNotas = findViewById(R.id.btnNotasAlumno);
        jbtnIrNotas.setOnClickListener(this);
        jbtnIrNotas.setEnabled(false);

        profesor = this.getIntent().getStringExtra("profesor");

        cargarCiclos();
    }

    private void cargarCiclos() {
        idCiclos = new ArrayList<>();
        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    String ciclos[] = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject ciclo = jsonArray.getJSONObject(i);
                        String id = ciclo.getString("ciclo");
                        String desc = ciclo.getString("descripcion");
                        ciclos[i] = id + " - " + desc;
                        idCiclos.add(id);
                    }
                    llenarSpinner(ciclos, jspCiclos);
                }
            }
        };
        RequestParams params = new RequestParams();
        cliente.get("ciclos.php", params, observer);
    }

    private void llenarSpinner(String[] ciclos, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrosActivity.this,
                android.R.layout.simple_spinner_item, ciclos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    private void cargarAlumnos(String idCiclo) {
        codAlumnos = new ArrayList<>();
        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    String profesores[] = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject response = jsonArray.getJSONObject(i);
                        String cod = response.getString("cod_alumno");
                        String nombre = response.getString("nombre");
                        String apellidos = response.getString("apellidos");
                        profesores[i] = nombre + " - " + apellidos;
                        codAlumnos.add(cod);
                    }
                    llenarSpinner(profesores, jspAlumnos);
                }
            }
        };
        RequestParams params = new RequestParams();
        params.put("ciclo", idCiclo);
        cliente.get("alumnos.php", params, observer);
    }


    private void cargarCursos(String idCiclo) {
        codCursos = new ArrayList<>();
        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    String cursos[] = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject response = jsonArray.getJSONObject(i);
                        String cod = response.getString("cod_curso");
                        String desc = response.getString("descripcion");
                        cursos[i] = cod + " - " + desc;
                        codCursos.add(cod);
                    }
                    llenarSpinner(cursos, jspCursos);
                }
            }
        };
        RequestParams params = new RequestParams();
        params.put("ciclo", idCiclo);
        cliente.get("cursos-ciclo.php", params, observer);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        switch (adapterView.getId()) {
            case R.id.spCiclos:
                Toast.makeText(getApplicationContext(), "Ciclos: pos:" + pos + " id:" + id, Toast.LENGTH_SHORT).show();
                cargarAlumnos(idCiclos.get(pos));
                cargarCursos(idCiclos.get(pos));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistrar:
                guardarRegistro();
                break;

            case R.id.btnNotasAlumno:
                navegarANotas();
                break;
        }
    }

    private void guardarRegistro() {

        int posAlumno = jspAlumnos.getSelectedItemPosition();
        if (posAlumno < 0 || posAlumno >= codAlumnos.size()) {
            Toast.makeText(getApplicationContext(), "Seleccione un alumno", Toast.LENGTH_SHORT).show();
            return;
        }
        int posCurso = jspCursos.getSelectedItemPosition();

        if (posCurso < 0 || posCurso >= codCursos.size()) {
            Toast.makeText(getApplicationContext(), "Seleccione un curso", Toast.LENGTH_SHORT).show();
            return;
        }
        alumno = codAlumnos.get(posAlumno);
        curso = codCursos.get(posCurso);

        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    JSONObject response = jsonArray.getJSONObject(0);
                    registro = response.getString("id_registro");
                    Toast.makeText(getApplicationContext(), "Registro exitoso!", Toast.LENGTH_SHORT).show();
                    deshabilitarSpinners();
                }
            }
        };

        RequestParams params = new RequestParams();
        params.add("alumno", alumno);
        params.add("curso", curso);
        params.add("profesor", profesor);
        cliente.post("registros.php", params, observer);

    }

    private void deshabilitarSpinners() {

        jbtnIrNotas.setEnabled(true);
        jbtnRegistrar.setVisibility(View.GONE);
        jspCiclos.setEnabled(true);
        jspAlumnos.setEnabled(true);
        jspCursos.setEnabled(true);
    }

    private void navegarANotas() {
        Intent iNotas = new Intent(getApplicationContext(), NotasActivity.class);
        iNotas.putExtra("alumno", alumno);
        iNotas.putExtra("tipo", tipo);
        iNotas.putExtra("profesor", profesor);
        iNotas.putExtra("curso", curso);
        iNotas.putExtra("registro", registro);
        startActivity(iNotas);
    }
}
package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejemplo1.webservices.ExitoObserver;
import com.example.ejemplo1.webservices.WebServiceClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VerRegistrosActivity extends AppCompatActivity implements View.OnClickListener {
    WebServiceClient cliente = WebServiceClient.client();

    String alumno = "";
    String profesor = "";
    String tipo = "";
    String curso = "";
    String registro = "";

    List<String> idRegistros;
    List<String> idCursos;
    List<String> idAlumnos;
    List<String> idProfesores;


    List<Button> buttonList;
    LinearLayout jLayoutRegistros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_registros);

        tipo = this.getIntent().getStringExtra("tipo");
        alumno = this.getIntent().getStringExtra("alumno");
        profesor = this.getIntent().getStringExtra("profesor");
        jLayoutRegistros = (LinearLayout) findViewById(R.id.layoutRegistros);
        cargarRegistros();
    }

    private void cargarRegistros() {
        idRegistros = new ArrayList<>();
        idCursos = new ArrayList<>();
        buttonList = new ArrayList<>();
        idAlumnos = new ArrayList<>();
        idProfesores = new ArrayList<>();

        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    String registros[] = new String[jsonArray.length()];
                    String cur = "", prof = "", alu = "", desc;
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject response = jsonArray.getJSONObject(i);
                        String id = response.getString("id_registro");
                        cur = response.getString("cod_curso");
                        desc = response.getString("descripcion");
                        registros[i] = cur + " - " + desc;
                        // si es profe cargar alumnos
                        if (tipo.equals("P")) {
                            alu = response.getString("cod_alumno");
                            registros[i] += " - " + alu;
                            idAlumnos.add(alu);
                        } else {
                            // si es alumno cargar profesores
                            prof = response.getString("id_profesor");
                            registros[i] += " - " + prof;
                            idProfesores.add(prof);
                        }
                        idCursos.add(cur);
                        idRegistros.add(id);
                    }

                    crearRegistrostextView(registros);
                }
            }
        };

        RequestParams params = new RequestParams();
        params.add("alumno", alumno);
        params.add("curso", curso);
        params.add("profesor", profesor);
        cliente.get("registros.php", params, observer);
    }

    private void crearRegistrostextView(String[] registros) {
        int i = 0;
        for (String r : registros) {
            Button button = new Button(this);
            button.setId(i++);
            button.setText(r);
            button.setTextSize(15);
            button.setOnClickListener(this);
            /*
            TextView textView = new TextView(this);
            textView.setId(i++);
            textView.setText(r);
            textView.setTextSize(25);
            textView.setOnClickListener(this);
*/
            buttonList.add(button);
            jLayoutRegistros.addView(button);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (v.getId() == buttonList.get(i).getId()) {
                Toast.makeText(getApplicationContext(), i + " seleccionado", Toast.LENGTH_SHORT).show();
                navegarANotas(i);
            }
        }
    }

    private void navegarANotas(int i) {
        registro = idRegistros.get(i);
        curso = idCursos.get(i);

        if (tipo.equals("P")) {
            alumno = idAlumnos.get(i);
        } else {
            profesor = idProfesores.get(i);
        }

        Intent iNotas = new Intent(getApplicationContext(), NotasActivity.class);
        iNotas.putExtra("alumno", alumno);
        iNotas.putExtra("tipo", tipo);
        iNotas.putExtra("profesor", profesor);
        iNotas.putExtra("curso", curso);
        iNotas.putExtra("registro", registro);
        iNotas.putExtra("cargarNotas", "SI"); // cuando es profesor
        startActivity(iNotas);
    }

}
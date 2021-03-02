package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejemplo1.webservices.ExitoObserver;
import com.example.ejemplo1.webservices.WebServiceClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class NotasActivity extends AppCompatActivity implements View.OnClickListener {

    WebServiceClient cliente = WebServiceClient.client();

    String alumno = "";
    String profesor = "";
    String tipo = "";
    String curso = "";
    String registro = "";

    EditText jtxtNotaT1, jtxtNotaT2, jtxtNotaT3, jtxtNotaT4, jtxtNotaFinal, jtxtComent;
    Button btnRegistrarNotas, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        jtxtNotaT1 = findViewById(R.id.txtNotaT1);
        jtxtNotaT2 = findViewById(R.id.txtNotaT2);
        jtxtNotaT3 = findViewById(R.id.txtNotaT3);
        jtxtNotaT4 = findViewById(R.id.txtNotaT4);
        jtxtNotaFinal = findViewById(R.id.txtNotaFinal);
        jtxtComent = findViewById(R.id.txtComentario);


        btnRegistrarNotas = findViewById(R.id.btnRegistrarNota);
        btnRegistrarNotas.setOnClickListener(this);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        tipo = this.getIntent().getStringExtra("tipo");
        alumno = this.getIntent().getStringExtra("alumno");
        profesor = this.getIntent().getStringExtra("profesor");
        curso = this.getIntent().getStringExtra("curso");
        registro = this.getIntent().getStringExtra("registro");

        if (tipo.equals("A")) {
            mostrarNotas();
            deshabilitarBotones();
        }else{
            // si es profesor pero debe leer la nota
            String cargarNotas = this.getIntent().getStringExtra("cargarNotas");
            if(cargarNotas.equals("SI")){
                mostrarNotas();
            }
        }

    }

    private void mostrarNotas() {

        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    JSONObject notas = jsonArray.getJSONObject(0);
                    String t1 = notas.getString("Nota_T1");
                    String t2 = notas.getString("Nota_T2");
                    String t3 = notas.getString("Nota_T3");
                    String t4 = notas.getString("Nota_T4");
                    String tfinal = notas.getString("Examen_Final");
                    String coment = notas.getString("Comentario");

                    jtxtNotaT1.setText(t1);
                    jtxtNotaT2.setText(t2);
                    jtxtNotaT3.setText(t3);
                    jtxtNotaT4.setText(t4);
                    jtxtNotaFinal.setText(tfinal);
                    jtxtComent.setText(coment);

                    deshabilitarBotones();
                }
            }
        };
        RequestParams params = new RequestParams();
        params.add("tipo", tipo);
        params.add("alumno", alumno);
        params.add("profesor", profesor);
        params.add("curso", curso);
        cliente.get("notas.php", params, observer);

    }

    private void deshabilitarBotones() {
        btnRegistrarNotas.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);

        jtxtNotaT1.setEnabled(false);
        jtxtNotaT2.setEnabled(false);
        jtxtNotaT3.setEnabled(false);
        jtxtNotaT4.setEnabled(false);
        jtxtNotaFinal.setEnabled(false);
        jtxtComent.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistrarNota:
                guardarNota();
                //guardarRegistroYNota();
                break;
        }
    }

    private void guardarRegistroYNota() {
        // Id generado aletorio por temas de tiempo
        // String registro = "R" + (new Random().nextInt(9999));

        guardarRegistro();
    }

    private void guardarRegistro() {

        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    JSONObject response = jsonArray.getJSONObject(0);
                    registro = response.getString("id_registro");
                    guardarNota();
                }
            }
        };

        RequestParams params = new RequestParams();
        params.add("alumno", alumno);
        params.add("curso", curso);
        params.add("profesor", profesor);
        cliente.post("registros.php", params, observer);
    }

    private void guardarNota() {

        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    Toast.makeText(getApplicationContext(), "Guardado!, Id registro:" + registro, Toast.LENGTH_SHORT).show();
                }
            }
        };

        String t1 = jtxtNotaT1.getText().toString().trim();
        String t2 = jtxtNotaT2.getText().toString().trim();
        String t3 = jtxtNotaT3.getText().toString().trim();
        String t4 = jtxtNotaT4.getText().toString().trim();
        String tfinal = jtxtNotaFinal.getText().toString().trim();
        String coment = jtxtComent.getText().toString().trim();

        if (validarCampo(t1, "Ingrese Nota 1")) return;
        if (validarCampo(t2, "Ingrese Nota 2")) return;
        if (validarCampo(t3, "Ingrese Nota 3")) return;
        if (validarCampo(t4, "Ingrese Nota")) return;
        if (validarCampo(tfinal, "Ingrese Nota Final")) return;

        //String registro = "R001";
        RequestParams params = new RequestParams();
        params.add("t1", t1);
        params.add("t2", t2);
        params.add("t3", t3);
        params.add("t4", t4);
        params.add("final", tfinal);
        params.add("coment", coment);
        params.add("registro", registro);
        cliente.post("notas.php", params, observer);

    }

    private boolean validarCampo(String campo, String mensaje) {
        if (campo.isEmpty()) {
            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
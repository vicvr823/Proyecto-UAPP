package com.example.ejemplo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ejemplo1.webservices.ExitoObserver;
import com.example.ejemplo1.webservices.Hash;
import com.example.ejemplo1.webservices.WebServiceClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    WebServiceClient cliente = WebServiceClient.client();

    Hash hash = new Hash();
    EditText jtxtUsuario, jtxtClave;
    Button jbtnIngresar, jbtnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        jtxtUsuario = findViewById(R.id.txtUsuario);
        jtxtClave = findViewById(R.id.txtClave);

        jbtnIngresar = findViewById(R.id.btnIngresar);
        jbtnIngresar.setOnClickListener(this);

        jbtnSalir = findViewById(R.id.btnSalir);
        jbtnSalir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIngresar:
                login();
                break;
            case R.id.btnSalir:
                cerrarApliacacion();
                break;
        }
    }



    private void login() {
        String usuario = jtxtUsuario.getText().toString().trim();
        String clave = jtxtClave.getText().toString().trim();

        if (usuario.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (clave.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese clave.", Toast.LENGTH_SHORT).show();
            return;
        }

        clave = hash.stringToHash(clave);

        RequestParams params = new RequestParams();
        params.add("usuario", usuario);
        params.add("pwd", clave);

        ExitoObserver observer = new ExitoObserver() {
            @Override
            public void exito(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    JSONObject user = jsonArray.getJSONObject(0);
                    String tipo = user.getString("tipo");
                    navegarAMenu(usuario, tipo);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o clave incorrectos.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        cliente.post("login.php", params, observer);
    }

    private void cerrarApliacacion() {
        System.exit(1);
    }

    private void navegarAMenu(String usuario, String tipo) {
        Intent iMenu = new Intent(getApplicationContext(), MenuActivity.class);
        iMenu.putExtra("usuario", usuario);
        iMenu.putExtra("tipo", tipo); // Alumno (A), Profesor (P)
        startActivity(iMenu);
        finish();
    }
}
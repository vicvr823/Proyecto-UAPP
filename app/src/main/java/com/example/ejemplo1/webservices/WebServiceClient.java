package com.example.ejemplo1.webservices;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class WebServiceClient {

    String url = "http://upnproyecto.atwebpages.com/WebService/";
    AsyncHttpClient cliente = new AsyncHttpClient();

    public static WebServiceClient client() {
        return new WebServiceClient();
    }

    public void get(String servicio, RequestParams params, ExitoObserver observer) {

        cliente.get(url + servicio, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        observer.exito(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                //Significa que ocurrio un error al conectarse al web service
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    public void post(String servicio, RequestParams params, ExitoObserver observer) {

        cliente.post(url + servicio, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                //Significa que el servicio web esta trabajando correctamente (estado 200)
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        observer.exito(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            observer.exito(new JSONArray("[]"));
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, String rawJsonData, Object errorResponse) {
                //Significa que ocurrio un error al conectarse al web service
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws
                    Throwable {
                return null;
            }
        });
    }

}

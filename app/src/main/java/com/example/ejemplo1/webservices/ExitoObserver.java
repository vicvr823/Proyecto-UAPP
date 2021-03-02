package com.example.ejemplo1.webservices;

import org.json.JSONArray;
import org.json.JSONException;

public interface ExitoObserver {
    void exito(JSONArray jsonArray) throws JSONException;
}

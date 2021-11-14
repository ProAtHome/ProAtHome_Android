package com.proathome.servicios.api;

import org.json.JSONException;

public interface AsyncResponseAPI {

    void processFinish(String response) throws JSONException;

}

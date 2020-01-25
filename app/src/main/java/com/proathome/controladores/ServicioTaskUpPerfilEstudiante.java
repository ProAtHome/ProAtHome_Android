package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ServicioTaskUpPerfilEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    private String respuesta;
    private String resultadoApi = "";
    private String linkRequestApi;
    private int idEstudiante, edad;
    private String nombre, descripcion;

    public ServicioTaskUpPerfilEstudiante(Context httpContext, String linkRequestApi, int idEstudiante, String nombre, int edad, String descripcion){

        this.httpContext = httpContext;
        this.linkRequestApi = linkRequestApi;
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.edad = edad;
        this.descripcion = descripcion;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

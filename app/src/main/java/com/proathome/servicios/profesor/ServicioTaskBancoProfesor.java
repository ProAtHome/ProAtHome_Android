package com.proathome.servicios.profesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.proathome.ui.editarPerfilProfesor.EditarPerfilProfesorFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskBancoProfesor extends AsyncTask<Void, Void, String>{

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi = "";
    public String linkrequestAPI = "";
    public String respuesta;
    public int idProfesor;

    public ServicioTaskBancoProfesor(Context ctx, String linkAPI, int idProfesor) {
        this.httpContext = ctx;
        this.idProfesor = idProfesor;
        this.linkrequestAPI = linkAPI + "/" + this.idProfesor;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Cargando Datos Bancarios.",
                "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... params) {

        String result = null;

        String wsURL = linkrequestAPI;
        URL url = null;
        try {

            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";

                while ((linea = in.readLine()) != null) {
                    sb.append(linea);
                    break;
                }

                in.close();
                result = sb.toString();
                respuesta = result;
            } else {
                result = new String("Error: " + responseCode);
                respuesta = null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta")){
                JSONObject jsonDatos = jsonObject.getJSONObject("mensaje");
                if(jsonDatos.getBoolean("existe")){
                    EditarPerfilProfesorFragment.etTitular.setText(jsonDatos.getString("nombreTitular"));
                    EditarPerfilProfesorFragment.etBanco.setText(jsonDatos.getString("banco"));
                    EditarPerfilProfesorFragment.etClabe.setText(jsonDatos.getString("clabe"));
                }
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.PlanesFragment;
import com.proathome.ui.editarPerfil.EditarPerfilFragment;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskBancoEstudiante extends AsyncTask<Void, Void, String>{

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi, linkrequestAPI, respuesta;
    public int idEstudiante, tipoSolicitud;
    public static int OBTENER_DATOS = 1, VALIDAR_BANCO = 2, DATOS_PLANES = 3;

    public ServicioTaskBancoEstudiante(Context ctx, String linkAPI, int idEstudiante, int tipoSolicitud) {
        this.httpContext = ctx;
        this.idEstudiante = idEstudiante;
        this.linkrequestAPI = linkAPI + "/" + this.idEstudiante;
        this.tipoSolicitud = tipoSolicitud;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Cargando Datos Bancarios.", "Por favor, espere...");
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
        resultadoapi = s;

        if (resultadoapi == null) {
            infoMsg("¡ERROR!","Ocurrió un error inesperado.", SweetAlert.WARNING_TYPE);
        } else {
            if(this.tipoSolicitud == ServicioTaskBancoEstudiante.OBTENER_DATOS){
                if (!resultadoapi.equals("null")) {
                    try {
                        JSONObject jsonObject = new JSONObject(resultadoapi);
                        EditarPerfilFragment.etNombreTitular.setText(jsonObject.getString("nombreTitular"));
                        EditarPerfilFragment.etTarjeta.setText(jsonObject.getString("tarjeta"));
                        EditarPerfilFragment.etMes.setText(jsonObject.getString("mes"));
                        EditarPerfilFragment.etAño.setText(jsonObject.getString("ano"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    infoMsg("¡AVISO!","Sin datos bancarios.", SweetAlert.WARNING_TYPE);
                }
            }else if(this.tipoSolicitud == ServicioTaskBancoEstudiante.VALIDAR_BANCO){
                if(resultadoapi.equals("null")){
                    DetallesFragment.banco = false;
                }else{
                    DetallesFragment.banco = true;
                }
            }else if(this.tipoSolicitud == ServicioTaskBancoEstudiante.DATOS_PLANES){
                if (!resultadoapi.equals("null")) {
                    try {
                        JSONObject jsonObject = new JSONObject(resultadoapi);
                        PlanesFragment.nombreTitular = jsonObject.getString("nombreTitular");
                        PlanesFragment.tarjeta = jsonObject.getString("tarjeta");
                        PlanesFragment.mes = jsonObject.getString("mes");
                        PlanesFragment.ano = jsonObject.getString("ano");
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    infoMsg("¡AVISO!","Sin datos bancarios.", SweetAlert.WARNING_TYPE);
                }
            }
        }
    }

    public void infoMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.httpContext, tipo, SweetAlert.ESTUDIANTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

}
package com.proathome.servicios.cliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.fragments.PlanesFragment;
import com.proathome.fragments.PreOrdenServicio;
import com.proathome.ui.editarPerfil.EditarPerfilFragment;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskBancoCliente extends AsyncTask<Void, Void, String>{

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi, linkrequestAPI, respuesta;
    public int idCliente, tipoSolicitud;
    public static int OBTENER_DATOS = 1, VALIDAR_BANCO = 2, DATOS_PLANES = 3;

    public ServicioTaskBancoCliente(Context ctx, String linkAPI, int idCliente, int tipoSolicitud) {
        this.httpContext = ctx;
        this.idCliente = idCliente;
        this.linkrequestAPI = linkAPI + "/" + this.idCliente;
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
        try {
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta")){
                JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                if(this.tipoSolicitud == ServicioTaskBancoCliente.OBTENER_DATOS){
                    if(mensaje.getBoolean("existe")){
                        EditarPerfilFragment.etNombreTitular.setText(mensaje.getString("nombreTitular"));
                        EditarPerfilFragment.etTarjeta.setText(mensaje.getString("tarjeta"));
                        EditarPerfilFragment.etMes.setText(mensaje.getString("mes"));
                        EditarPerfilFragment.etAño.setText(mensaje.getString("ano"));
                    }else
                        infoMsg("¡AVISO!", "No tienes datos bancarios registrados", SweetAlert.WARNING_TYPE);
                }else if(this.tipoSolicitud == ServicioTaskBancoCliente.VALIDAR_BANCO){
                    if(mensaje.getBoolean("existe")){
                        DetallesFragment.banco = true;
                        NuevaSesionFragment.banco = true;
                        //Datos bancarios Pre Orden.}
                        PreOrdenServicio.nombreTitular = mensaje.getString("nombreTitular");
                        PreOrdenServicio.tarjeta = mensaje.get("tarjeta").toString();
                        PreOrdenServicio.mes = mensaje.get("mes").toString();
                        PreOrdenServicio.ano = mensaje.get("ano").toString();
                    }else{
                        NuevaSesionFragment.banco = false;
                        DetallesFragment.banco = false;
                    }
                }else if(this.tipoSolicitud == ServicioTaskBancoCliente.DATOS_PLANES){
                    if(mensaje.getBoolean("existe")){
                        PlanesFragment.nombreTitular = mensaje.getString("nombreTitular");
                        PlanesFragment.tarjeta = mensaje.getString("tarjeta");
                        PlanesFragment.mes = mensaje.getString("mes");
                        PlanesFragment.ano = mensaje.getString("ano");
                    }else
                        infoMsg("¡AVISO!", "No tienes datos bancarios registrados", SweetAlert.WARNING_TYPE);
                }
            }else
                infoMsg("¡ERROR", jsonObject.get("mensaje").toString(), SweetAlert.ERROR_TYPE);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    public void infoMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.httpContext, tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

}
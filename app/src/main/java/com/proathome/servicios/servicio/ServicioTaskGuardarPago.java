package com.proathome.servicios.servicio;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.proathome.ui.fragments.NuevaSesionFragment;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.planes.ServicioTaskValidarPlan;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;

public class ServicioTaskGuardarPago extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String token, estatusPago;
    private double costoServicio, costoTE;
    private int idCliente;
    private String linkGuardarToken = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/guardarTokenPagoServicio";
    private Bundle bundle;

    public ServicioTaskGuardarPago(Context contexto, String token, double costoServicio, double costoTE,
                                   String estatusPago, int idCliente){
        this.contexto = contexto;
        this.token = token;
        this.costoServicio = costoServicio;
        this.costoTE = costoTE;
        this.estatusPago = estatusPago;
        this.idCliente = idCliente;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        try{

            URL url = new URL(this.linkGuardarToken);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", this.token);
            jsonObject.put("costoServicio", this.costoServicio);
            jsonObject.put("costoTE", this.costoTE);
            jsonObject.put("estatusPago", this.estatusPago);
            jsonObject.put("idCliente", this.idCliente);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";

                while ((linea = in.readLine()) != null) {
                    sb.append(linea);
                    break;
                }

                in.close();
                result = sb.toString();
            }else{
                result = new String("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void setBundleSesion(Bundle bundle){
        this.bundle = bundle;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        registrarSesion();
    }

    private void registrarSesion(){
        JSONObject parametrosPOST = new JSONObject();
        try {
            parametrosPOST.put("idCliente", this.idCliente);
            parametrosPOST.put("horario", bundle.getString("horario"));
            parametrosPOST.put("lugar", bundle.getString("lugar"));
            parametrosPOST.put("tiempo", bundle.getInt("tiempo"));
            parametrosPOST.put("idSeccion", bundle.getInt("idSeccion"));
            parametrosPOST.put("idNivel", bundle.getInt("idNivel"));
            parametrosPOST.put("idBloque", bundle.getInt("idBloque"));
            parametrosPOST.put("extras", bundle.getString("extras"));
            parametrosPOST.put("tipoServicio", bundle.getString("tipoServicio"));
            parametrosPOST.put("latitud", bundle.getDouble("latitud"));
            parametrosPOST.put("longitud", bundle.getDouble("longitud"));
            parametrosPOST.put("actualizado", bundle.getString("actualizado"));
            parametrosPOST.put("fecha",  bundle.getString("fecha"));
            parametrosPOST.put("sumar", bundle.getBoolean("sumar"));
            parametrosPOST.put("tipoPlan", bundle.getString("tipoPlan"));
            parametrosPOST.put("personas", bundle.getInt("personas"));
            parametrosPOST.put("token", this.token);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta"))
                    actualizarMonedero(jsonObject);
                else
                    showMsg("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
            }, APIEndPoints.REGISTRAR_SESION_CLIENTE, WebServicesAPI.POST, parametrosPOST);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void actualizarMonedero(JSONObject jsonObject) throws JSONException {
        JSONObject parametrosPUT= new JSONObject();
        parametrosPUT.put("idCliente", idCliente);
        parametrosPUT.put("nuevoMonedero", NuevaSesionFragment.nuevoMonedero);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            ServicioTaskValidarPlan validarPlan = new ServicioTaskValidarPlan(this.contexto, idCliente);
            validarPlan.execute();
        }, APIEndPoints.ACTUALIZAR_MONEDERO, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();

        NuevaSesionFragment.nuevoMonedero = 0;
        showMsg("¡GENIAL!", jsonObject.getString("mensaje"), SweetAlert.SUCCESS_TYPE);
    }

    private void showMsg(String titulo, String mensaje, int tipo){
        if(tipo == SweetAlert.ERROR_TYPE){
            new SweetAlert(this.contexto, tipo, SweetAlert.CLIENTE)
                    .setTitleText(titulo)
                    .setContentText(mensaje)
                    .show();
        }else if( tipo == SweetAlert.SUCCESS_TYPE){
            new SweetAlert(this.contexto, tipo, SweetAlert.CLIENTE)
                    .setTitleText(titulo)
                    .setContentText(mensaje)
                    .setConfirmButton("¡VAMOS!", sweetAlertDialog -> {
                        NuevaSesionFragment.dialogFragment.dismiss();
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

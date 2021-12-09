package com.proathome.servicios.servicio;

import android.os.AsyncTask;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioSesionesPagadas extends AsyncTask<Void, Void, String> {

    private int idCliente;
    private String linkSesionesPagadas = Constants.IP + "/ProAtHome/apiProAtHome/cliente/verificarSesionesPagadas/";

    public ServicioSesionesPagadas(int idCliente){
        this.idCliente = idCliente;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = new URL(this.linkSesionesPagadas + this.idCliente);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while ((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();
            }else{
                resultado = new String("Error: " + responseCode);
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONObject jsonObject = new JSONObject(s);
            //TODO FLUJO_PLANES_EJECUTAR: Posible cambio de algortimo para obtener plan_activo, verificar la fecha de inicio si es distinto a PARTICULAR.
            SesionesFragment.PLAN_ACTIVO = jsonObject.getBoolean("plan_activo");
            SesionesFragment.SESIONES_PAGADAS_FINALIZADAS = jsonObject.getBoolean("sesiones_pagadas_finalizadas");
        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

}

package com.proathome.servicios.servicio;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.proathome.servicios.cliente.STRegistroSesionesCliente;
import com.proathome.utils.Constants;

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
        STRegistroSesionesCliente registro = new STRegistroSesionesCliente(contexto,
                bundle.getString("registrarSesionREST"), bundle.getInt("idCliente"), bundle.getString("horario"),
                bundle.getString("lugar"), bundle.getInt("tiempo"), bundle.getInt("idSeccion"), bundle.getInt("idNivel"),
                bundle.getInt("idBloque"), bundle.getString("extras"), bundle.getString("tipoServicio"), bundle.getDouble("latitud"),
                bundle.getDouble("longitud"), bundle.getString("actualizado"), bundle.getString("fecha"), bundle.getBoolean("sumar"),
                bundle.getString("tipoPlan"), bundle.getInt("personas"), this.token);
        registro.execute();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

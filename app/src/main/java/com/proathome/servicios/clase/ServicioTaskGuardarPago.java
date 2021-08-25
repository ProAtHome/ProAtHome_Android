package com.proathome.servicios.clase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.servicios.estudiante.STRegistroSesionesEstudiante;
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

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskGuardarPago extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String token, estatusPago;
    private double costoClase, costoTE;
    private int idEstudiante;
    private String linkGuardarToken = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/guardarTokenPagoClase";
    private Bundle bundle;

    public ServicioTaskGuardarPago(Context contexto, String token, double costoClase, double costoTE,
                                   String estatusPago, int idEstudiante){
        this.contexto = contexto;
        this.token = token;
        this.costoClase = costoClase;
        this.costoTE = costoTE;
        this.estatusPago = estatusPago;
        this.idEstudiante = idEstudiante;
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
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", this.token);
            jsonObject.put("costoClase", this.costoClase);
            jsonObject.put("costoTE", this.costoTE);
            jsonObject.put("estatusPago", this.estatusPago);
            jsonObject.put("idEstudiante", this.idEstudiante);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jsonObject));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpsURLConnection.HTTP_OK) {
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
        STRegistroSesionesEstudiante registro = new STRegistroSesionesEstudiante(contexto,
                bundle.getString("registrarSesionREST"), bundle.getInt("idCliente"), bundle.getString("horario"),
                bundle.getString("lugar"), bundle.getInt("tiempo"), bundle.getInt("idSeccion"), bundle.getInt("idNivel"),
                bundle.getInt("idBloque"), bundle.getString("extras"), bundle.getString("tipoClase"), bundle.getDouble("latitud"),
                bundle.getDouble("longitud"), bundle.getString("actualizado"), bundle.getString("fecha"), bundle.getBoolean("sumar"),
                bundle.getString("tipoPlan"), bundle.getInt("personas"), this.token);
        registro.execute();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class STRegistroSesionesEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public int idCliente;
    public String linkrequestAPI, horario, lugar, tiempo, nivel, extras, tipoClase, actualizado;
    public double latitud,longitud;

    public STRegistroSesionesEstudiante(Context ctx, String linkAPI, int idCliente, String horario, String lugar,
                                        String tiempo, String nivel, String extras, String tipoClase, double latitud, double longitud, String actualizado){

        this.linkrequestAPI = linkAPI;
        this.idCliente = idCliente;
        this.horario = horario;
        this.lugar = lugar;
        this.tiempo = tiempo;
        this.nivel = nivel;
        this.extras = extras;
        this.tipoClase = tipoClase;
        this.longitud = longitud;
        this.latitud = latitud;
        this.actualizado = actualizado;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        String wsURl = linkrequestAPI;

        URL url = null;
        try{

            url = new URL(wsURl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            JSONObject parametrosPOST = new JSONObject();
            parametrosPOST.put("idCliente", idCliente);
            parametrosPOST.put("horario", horario);
            parametrosPOST.put("lugar", lugar);
            parametrosPOST.put("tiempo", tiempo);
            parametrosPOST.put("nivel", nivel);
            parametrosPOST.put("extras", extras);
            parametrosPOST.put("tipoClase", tipoClase);
            parametrosPOST.put("latitud", latitud);
            parametrosPOST.put("longitud", longitud);
            parametrosPOST.put("actualizado", this.actualizado);

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(parametrosPOST));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String linea ="";
                while ((linea = in.readLine()) != null){

                    sb.append(linea);
                    break;

                }

                in.close();
                result = sb.toString();

            }else{

                result = new String("Error: " + responseCode);

            }

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public String getPostDataString(JSONObject params) throws Exception{

        return params.toString();

    }

}

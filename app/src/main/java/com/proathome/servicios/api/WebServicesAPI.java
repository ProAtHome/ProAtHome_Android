package com.proathome.servicios.api;

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


public class WebServicesAPI extends AsyncTask<Void, Void, String> {

    public AsyncResponseAPI delegate = null;
    private String urlApi;
    private JSONObject jsonObject;
    private int tipoPeticion;
    public static int GET = 1;
    public static int POST = 2;
    public static int PUT = 3;

    public WebServicesAPI(AsyncResponseAPI delegate, String urlApi, int tipoPeticion, JSONObject jsonObject){
        this.delegate = delegate;
        this.urlApi = urlApi;
        this.tipoPeticion = tipoPeticion;
        this.jsonObject = jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {
            System.out.println(this.urlApi);
            URL url = new URL(this.urlApi);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            if(this.tipoPeticion == WebServicesAPI.GET){
                urlConnection.setRequestMethod("GET");
            }else if(this.tipoPeticion == WebServicesAPI.POST){
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(this.jsonObject));
                writer.flush();
                writer.close();
                os.close();
            }else if(this.tipoPeticion == WebServicesAPI.PUT){
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(this.jsonObject));
                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode=urlConnection.getResponseCode();
            if(responseCode== HttpURLConnection.HTTP_OK){
                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb= new StringBuffer("");
                String linea="";

                while ((linea=in.readLine())!= null){
                    sb.append(linea);
                    break;
                }

                in.close();
                resultado= sb.toString();
            }
            else
                resultado = new String("Error: "+ responseCode);

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            delegate.processFinish(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }
}

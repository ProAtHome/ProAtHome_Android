package com.proathome.servicios.fastservices;

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
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class ServicioFastServices extends AsyncTask<Void, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output) throws JSONException;
    }

    public AsyncResponse delegate = null;
    private String urlApi;
    private ProgressDialog progressDialog;
    private Context context;
    private JSONObject jsonObject;
    private int tipoPeticion;
    public static int GET = 1;
    public static int POST = 2;
    public static int PUT = 3;

    public ServicioFastServices(AsyncResponse delegate, String urlApi, Context context, int tipoPeticion, JSONObject jsonObject){
        this.delegate = delegate;
        this.urlApi = urlApi;
        this.context = context;
        this.tipoPeticion = tipoPeticion;
        this.jsonObject = jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.context, "Procesando Solicitud", "Espere un momento...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {
            System.out.println(this.urlApi);
            URL url = new URL(this.urlApi);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            if(this.tipoPeticion == ServicioFastServices.GET){
                urlConnection.setRequestMethod("GET");
            }else if(this.tipoPeticion == ServicioFastServices.POST){
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(this.jsonObject));
                writer.flush();
                writer.close();
                os.close();
            }else if(this.tipoPeticion == ServicioFastServices.PUT){
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
            if(responseCode== HttpsURLConnection.HTTP_OK){
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
        progressDialog.dismiss();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }
}

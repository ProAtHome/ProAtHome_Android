package com.proathome.controladores.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
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

public class ServicioTaskUpCuentaEstudiante extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String linkAPI, tipoDePago, banco, numeroCuenta, direccionFacturacion, respuesta, resultadoAPI;
    private int idEstudiante;

    public ServicioTaskUpCuentaEstudiante(Context contexto, String linkAPI, int idEstudiante, String tipoDePago, String banco, String numeroCuenta, String direccionFacturacion){

        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.idEstudiante = idEstudiante;
        this.tipoDePago = tipoDePago;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.direccionFacturacion = direccionFacturacion;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Actualizando Datos", "Por favor, espere...");

    }

    @Override
    protected String doInBackground(Void... voids) {

        String result = null;

        try{

            String urlREST = this.linkAPI;
            URL url = new URL(urlREST);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametrosPOST = new JSONObject();
            parametrosPOST.put("idCliente", this.idEstudiante);
            parametrosPOST.put("tipoDePago", this.tipoDePago);
            parametrosPOST.put("banco", this.banco);
            parametrosPOST.put("numeroCuenta", this.numeroCuenta);
            parametrosPOST.put("direccionFacturacion", this.direccionFacturacion);

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
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
                String linea = "";

                while ((linea = in.readLine()) != null){

                    sb.append(linea);
                    break;

                }

                in.close();
                result = sb.toString();

            }else{

                result = new String("Error: " +responseCode);

            }

        }catch (MalformedURLException ex){
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
        progressDialog.dismiss();
        this.resultadoAPI = s;
        Toast.makeText(this.contexto, this.resultadoAPI, Toast.LENGTH_LONG).show();

    }

    public String getPostDataString(JSONObject params) throws Exception {

        return params.toString();

    }

}

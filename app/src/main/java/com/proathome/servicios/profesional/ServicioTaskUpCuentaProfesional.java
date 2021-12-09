package com.proathome.servicios.profesional;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.proathome.utils.SweetAlert;

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

import java.net.HttpURLConnection;

public class ServicioTaskUpCuentaProfesional extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String linkAPI, titular, banco, clabe, respuesta;
    private int idProfesional;

    public ServicioTaskUpCuentaProfesional(Context contexto, String linkAPI, int idProfesional, String titular, String banco, String clabe){
        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.idProfesional = idProfesional;
        this.titular = titular;
        this.banco = banco;
        this.clabe = clabe;
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
            parametrosPOST.put("idProfesional", this.idProfesional);
            parametrosPOST.put("nombreTitular", this.titular);
            parametrosPOST.put("banco", this.banco);
            parametrosPOST.put("clabe", this.clabe);

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
        try{
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta"))
                msgSweet("¡GENIAL!", jsonObject.getString("mensaje"), SweetAlert.SUCCESS_TYPE);
            else
                msgSweet("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void msgSweet(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.PROFESIONAL)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

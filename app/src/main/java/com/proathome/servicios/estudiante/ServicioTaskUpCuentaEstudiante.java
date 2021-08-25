package com.proathome.servicios.estudiante;

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

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskUpCuentaEstudiante extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String linkAPI, nombreTitular, tarjeta, mes, ano, respuesta, resultadoAPI;
    private int idEstudiante;

    public ServicioTaskUpCuentaEstudiante(Context contexto, String linkAPI, int idEstudiante,
                                          String nombreTitular, String tarjeta, String mes, String ano){
        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.idEstudiante = idEstudiante;
        this.nombreTitular = nombreTitular;
        this.tarjeta = tarjeta;
        this.mes = mes;
        this.ano = ano;
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
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            JSONObject parametrosPOST = new JSONObject();
            parametrosPOST.put("idCliente", this.idEstudiante);
            parametrosPOST.put("nombreTitular", this.nombreTitular);
            parametrosPOST.put("tarjeta", this.tarjeta);
            parametrosPOST.put("mes", this.mes);
            parametrosPOST.put("ano", this.ano);

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
            if(responseCode == HttpsURLConnection.HTTP_OK){
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
        if(this.resultadoAPI.equalsIgnoreCase("Actualización exitosa.")){
            new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡GENIAL!")
                    .setContentText("Datos actualizados correctamente.")
                    .show();
        }else{
            new SweetAlert(this.contexto, SweetAlert.WARNING_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText(this.resultadoAPI)
                    .show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

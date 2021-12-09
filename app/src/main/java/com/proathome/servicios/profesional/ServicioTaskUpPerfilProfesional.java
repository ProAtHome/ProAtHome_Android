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

public class ServicioTaskUpPerfilProfesional extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    private String respuesta;
    private String linkRequestApi;
    private int idProfesional;
    private String celular, descripcion, direccion, telefono;

    public ServicioTaskUpPerfilProfesional(Context httpContext, String linkRequestApi, int idProfesional,
                                           String celular, String telefono, String direccion, String descripcion){
        this.httpContext = httpContext;
        this.linkRequestApi = linkRequestApi;
        this.idProfesional = idProfesional;
        this.celular = celular;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Actualizando.", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
        String wsURL = linkRequestApi;
        URL url = null;

        try{
            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametrosPOST = new JSONObject();
            parametrosPOST.put("idProfesional", this.idProfesional);
            parametrosPOST.put("celular", this.celular);
            parametrosPOST.put("telefonoLocal", this.telefono);
            parametrosPOST.put("direccion", this.direccion);
            parametrosPOST.put("descripcion", this.descripcion);

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

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
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
            if(jsonObject.getBoolean("respuesta")){
                new SweetAlert(this.httpContext, SweetAlert.SUCCESS_TYPE, SweetAlert.PROFESIONAL)
                        .setTitleText("¡GENIAL!")
                        .setContentText(jsonObject.getString("mensaje"))
                        .show();
            }else{
                new SweetAlert(this.httpContext, SweetAlert.WARNING_TYPE, SweetAlert.PROFESIONAL)
                        .setTitleText("¡ERROR!")
                        .setContentText(jsonObject.getString("mensaje"))
                        .show();
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

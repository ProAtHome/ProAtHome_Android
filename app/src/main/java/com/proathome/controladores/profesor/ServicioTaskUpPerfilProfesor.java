package com.proathome.controladores.profesor;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskUpPerfilProfesor extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    private String respuesta;
    private String resultadoApi = "";
    private String linkRequestApi;
    private int idProfesor, edad;
    private String nombre, descripcion, correo;

    public ServicioTaskUpPerfilProfesor(Context httpContext, String linkRequestApi, int idProfesor,
                                        String nombre, String correo, int edad, String descripcion){
        this.httpContext = httpContext;
        this.linkRequestApi = linkRequestApi;
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.edad = edad;
        this.descripcion = descripcion;
        this.correo = correo;
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
            parametrosPOST.put("idProfesor", this.idProfesor);
            parametrosPOST.put("nombre", this.nombre);
            parametrosPOST.put("correo", this.correo);
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
        this.resultadoApi = s;
        if(this.resultadoApi.equalsIgnoreCase("Actualización exitosa")){
            new SweetAlert(this.httpContext, SweetAlert.SUCCESS_TYPE, SweetAlert.PROFESOR)
                    .setTitleText("¡GENIAL!")
                    .setContentText("Datos actualizados correctamente.")
                    .show();
        }else{
            new SweetAlert(this.httpContext, SweetAlert.WARNING_TYPE, SweetAlert.PROFESOR)
                    .setTitleText(this.resultadoApi)
                    .show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

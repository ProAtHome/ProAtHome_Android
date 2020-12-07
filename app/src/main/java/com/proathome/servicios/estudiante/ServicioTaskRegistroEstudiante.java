package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.proathome.MainActivity;
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

public class ServicioTaskRegistroEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi="";
    public String linkrequestAPI="";
    public String fecha="";
    public String nombre="";
    public int edad;
    public String contrasena ="";
    public String correo="";

    public ServicioTaskRegistroEstudiante(Context ctx, String linkAPI, String nombre, String fecha,
                                          int edad, String correo, String contrasena){
        this.httpContext=ctx;
        this.linkrequestAPI=linkAPI;
        this.fecha=fecha;
        this.nombre=nombre;
        this.edad=edad;
        this.contrasena = contrasena;
        this.correo = correo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Registrando Estudiante.", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... params) {

        String result= null;
        String wsURL = linkrequestAPI;
        URL url = null;
        try {

            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("nombre",nombre);
            parametrosPost.put("correo",correo);
            parametrosPost.put("edad",edad);
            parametrosPost.put("contrasena", contrasena);
            parametrosPost.put("fechaNacimiento", fecha);
            parametrosPost.put("fechaRegistro", "2019-04-13");

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //OBTENER EL RESULTADO DEL REQUEST
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(parametrosPost));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){

                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer sb= new StringBuffer("");
                String linea="";
                while ((linea=in.readLine())!= null){
                    sb.append(linea);
                    break;
                }

                in.close();
                result= sb.toString();

            }
            else{
                result= new String("Error: "+ responseCode);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        resultadoapi=s;
        if(resultadoapi != null){
            new SweetAlert(this.httpContext, SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡GENIAL!")
                    .setContentText(resultadoapi)
                    .setConfirmButton("OK", sweetAlertDialog -> {
                        Intent intent = new Intent(this.httpContext, MainActivity.class);
                        this.httpContext.startActivity(intent);
                    })
                    .show();
        }else{
            new SweetAlert(this.httpContext, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡ERROR!")
                    .setContentText("Ocurrió un error inesperado.")
                    .show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

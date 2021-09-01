package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.proathome.MainActivity;
import com.proathome.servicios.fastservices.ServicioFastServices;
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
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskRegistroEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi="", linkAPI, nombre, paterno, materno, fecha, celular, telefono, direccion, correo, genero, contrasena;

    public ServicioTaskRegistroEstudiante(Context ctx, String linkAPI, String nombre, String paterno, String materno,
                                          String fecha, String celular, String telefono, String direccion, String genero, String correo, String contrasena){
        this.httpContext=ctx;
        this.linkAPI = linkAPI;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.fecha=fecha;
        this.celular = celular;
        this.telefono = telefono;
        this.direccion = direccion;
        this.genero = genero;
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
        try {
            URL url = new URL(this.linkAPI);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("nombre",nombre);
            parametrosPost.put("paterno", paterno);
            parametrosPost.put("materno", materno);
            parametrosPost.put("correo", correo);
            parametrosPost.put("celular", celular);
            parametrosPost.put("telefono", telefono);
            parametrosPost.put("direccion", direccion);
            parametrosPost.put("fechaNacimiento", fecha);
            parametrosPost.put("genero", genero);
            parametrosPost.put("contrasena", contrasena);

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
            if(responseCode == HttpsURLConnection.HTTP_OK){
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
            else
                result= new String("Error: "+ responseCode);

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
        try{
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta")){
                JSONObject post = new JSONObject();
                post.put("token", jsonObject.getString("token"));
                post.put("correo", this.correo);
                ServicioFastServices fastServices = new ServicioFastServices(output -> {
                    new SweetAlert(this.httpContext, SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                            .setTitleText("¡GENIAL!")
                            .setContentText(jsonObject.getString("mensaje"))
                            .setConfirmButton("OK", sweetAlertDialog -> {
                                Intent intent = new Intent(this.httpContext, MainActivity.class);
                                this.httpContext.startActivity(intent);
                            })
                            .show();
                }, Constants.IP_80 + "/assets/lib/Verificacion.php?enviar=true", this.httpContext, ServicioFastServices.POST, post);
                fastServices.execute();

            }else{
                new SweetAlert(this.httpContext, SweetAlert.ERROR_TYPE, SweetAlert.PROFESOR)
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

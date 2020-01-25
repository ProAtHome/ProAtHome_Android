package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;
import com.proathome.inicioProfesor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskLoginProfesor extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    ProgressDialog progressDialog;
    public String resultadoapi="";
    public String linkrequestAPI="";
    public String respuesta;
    public String contra = "";
    public String correo = "";
    public String foto = "";
    public String nombre = "";
    public int idProfesor;

    public ServicioTaskLoginProfesor(Context ctx, String linkAPI, String correo, String contrasena){

        this.httpContext=ctx;
        this.correo = correo;
        this.contra = contrasena;
        this.linkrequestAPI=linkAPI + "/" + correo + "/" + contrasena;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Iniciando Sesión.", "Por favor, espere...");

    }

    @Override
    protected String doInBackground(Void... params) {

        String result= null;

        String wsURL = linkrequestAPI;
        URL url = null;
        try {

            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode=urlConnection.getResponseCode();
            if(responseCode== HttpURLConnection.HTTP_OK){

                InputStream responseBody = urlConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {

                    String key = jsonReader.nextName();

                    if (key.equals("foto")) {

                        foto = jsonReader.nextString();

                    }else if(key.equals("nombre")) {

                        nombre = jsonReader.nextString();

                    }else if(key.equals("idProfesor")){

                        idProfesor = jsonReader.nextInt();

                    }else {

                        jsonReader.skipValue();

                    }

                }

                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer sb= new StringBuffer("");
                String linea="";
                while ((linea=in.readLine())!= null){

                    sb.append(linea);
                    break;

                }

                in.close();
                result= sb.toString();
                respuesta = result;

            }
            else{

                result= new String("Error: "+ responseCode);
                respuesta = null;

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

        if(resultadoapi == null){

            Toast.makeText(httpContext, "Error del servidor.", Toast.LENGTH_LONG).show();

        }else {

            if(!resultadoapi.equals("null")){

                AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(httpContext, "sesionProfesor", null, 1);
                SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                String correoS = correo;
                String fotoS = foto;
                String nombreS = nombre;
                ContentValues registro = new ContentValues();
                registro.put("id", "1");
                registro.put("idProfesor", idProfesor);
                registro.put("nombre", nombreS);
                registro.put("correo" , correoS);
                registro.put("foto", fotoS);
                baseDeDatos.insert("sesionProfesor", null, registro);
                baseDeDatos.close();

                Intent intent = new Intent(httpContext, inicioProfesor.class);
                httpContext.startActivity(intent);
                System.out.println(resultadoapi);

            }else{

                Toast.makeText(httpContext, "Usuario no registrado.",Toast.LENGTH_LONG).show();

            }

        }

    }

}
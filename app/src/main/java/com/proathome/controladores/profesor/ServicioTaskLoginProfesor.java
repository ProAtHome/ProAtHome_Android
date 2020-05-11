package com.proathome.controladores.profesor;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.JsonReader;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.inicioProfesor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskLoginProfesor extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String contrasena, correo, nombre, respuesta, linkAPI, resultadoapi;
    private int idProfesor;
    private boolean estado;

    public ServicioTaskLoginProfesor(Context contexto, String linkAPI, String correo, String contrasena){

        this.contexto = contexto;
        this.correo = correo;
        this.contrasena = contrasena;
        this.linkAPI = linkAPI + "/" + correo + "/" + contrasena;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Iniciando Sesión.", "Por favor, espere...");

    }

    @Override
    protected String doInBackground(Void... params) {

        String result= null;

        String wsURL = this.linkAPI;
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

                    if(key.equals("idProfesor")){

                        this.idProfesor = jsonReader.nextInt();

                    }else if(key.equals("estado")){

                        this.estado = jsonReader.nextBoolean();

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

            Toast.makeText(this.contexto, "No estás registrado.", Toast.LENGTH_LONG).show();

        }else {

            if(!resultadoapi.equals("null")){

                if(this.estado){

                    AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(this.contexto, "sesionProfesor", null, 1);
                    SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("id", 1);
                    registro.put("idProfesor", this.idProfesor);
                    registro.put("correo" , this.correo);
                    baseDeDatos.insert("sesionProfesor", null, registro);
                    baseDeDatos.close();

                    Intent intent = new Intent(this.contexto, inicioProfesor.class);
                    this.contexto.startActivity(intent);

                }else{

                    new MaterialAlertDialogBuilder(this.contexto, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                            .setTitle("AVISO")
                            .setMessage("No tienes permiso para iniciar sesión.")
                            .setNegativeButton("Entendido", (dialog, which) -> {
                                Toast.makeText(this.contexto, "Ponte en contacto con soporte técnico.", Toast.LENGTH_LONG).show();
                            })
                            .setOnCancelListener(dialog -> {
                                Toast.makeText(this.contexto, "Ponte en contacto con soporte técnico.", Toast.LENGTH_LONG).show();
                            })
                            .show();
                }

            }else{

                Toast.makeText(this.contexto, "Usuario no registrado.",Toast.LENGTH_LONG).show();

            }

        }

    }

}
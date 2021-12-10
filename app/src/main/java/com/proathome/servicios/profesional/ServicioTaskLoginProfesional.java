package com.proathome.servicios.profesional;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.proathome.PasosActivarCuenta;
import com.proathome.PerfilBloqueado;
import com.proathome.InicioProfesional;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskLoginProfesional extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String contrasena, correo, nombre, respuesta, linkAPI, resultadoapi;
    private int idProfesional;
    private boolean estado;

    public ServicioTaskLoginProfesional(Context contexto, String linkAPI, String correo, String contrasena){
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

        try{
            if(s == null){
                errorMsg("Ocurrió un error inesperado, intenta de nuevo.");
            }else {
                if(!s.equals("null")){
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")) {
                        if(jsonObject.getBoolean("verificado")){
                            AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(this.contexto,
                                    "sesionProfesional", null, 1);
                            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                            ContentValues registro = new ContentValues();
                            registro.put("id", 1);
                            registro.put("idProfesional", jsonObject.getInt("idProfesional"));
                            registro.put("correo" , this.correo);
                            registro.put("rangoServicio", jsonObject.getInt("rangoServicio"));
                            baseDeDatos.insert("sesionProfesional", null, registro);
                            baseDeDatos.close();

                            Intent intent = new Intent(this.contexto, InicioProfesional.class);
                            this.contexto.startActivity(intent);
                        }else
                            errorMsg("Aún no verificas tu cuenta de correo electrónico.");
                    }else if(jsonObject.getString("estado").equalsIgnoreCase("documentacion") ||
                                jsonObject.getString("estado").equalsIgnoreCase("cita") ||
                                    jsonObject.getString("estado").equalsIgnoreCase("registro")){
                        Intent intent = new Intent(this.contexto, PasosActivarCuenta.class);
                        this.contexto.startActivity(intent);
                    }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO")){
                        Bundle bundle = new Bundle();
                        bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_PROFESIONAL);
                        Intent intent = new Intent(this.contexto, PerfilBloqueado.class);
                        intent.putExtras(bundle);
                        this.contexto.startActivity(intent);
                    }
                }else{
                    errorMsg("Usuario no registrado o tus datos están incorrectos.");
                }
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void errorMsg(String mensaje){
        new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

}
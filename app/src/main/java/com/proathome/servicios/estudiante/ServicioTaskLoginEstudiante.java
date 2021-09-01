package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.proathome.PasosActivarCuenta;
import com.proathome.PasosActivarCuentaEstudiante;
import com.proathome.PerfilBloqueado;
import com.proathome.inicioEstudiante;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskLoginEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi, linkrequestAPI, respuesta, contra, correo, foto, nombre;
    public int idEstudiante;

    public ServicioTaskLoginEstudiante(Context ctx, String linkAPI, String correo, String contrasena){
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
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode=urlConnection.getResponseCode();
            if(responseCode== HttpsURLConnection.HTTP_OK){
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
                System.out.println(s);
                if(!s.equals("null")){
                    JSONObject jsonObject = new JSONObject(s);
                    System.out.println(jsonObject);
                    if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")){
                        if(jsonObject.getBoolean("verificado")){
                            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(httpContext, "sesion",
                                    null, 1);
                            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                            ContentValues registro = new ContentValues();
                            registro.put("id", 1);
                            registro.put("idEstudiante", jsonObject.getInt("idCliente"));
                            registro.put("correo", this.correo);
                            baseDeDatos.insert("sesion", null, registro);
                            baseDeDatos.close();

                            Intent intent = new Intent(httpContext, inicioEstudiante.class);
                            httpContext.startActivity(intent);
                        }else
                            errorMsg("Aún no verificas tu cuenta de correo electrónico.");
                    }else if(jsonObject.getString("estado").equalsIgnoreCase("DOCUMENTACION") ||
                            jsonObject.getString("estado").equalsIgnoreCase("REGISTRO")){
                        Intent intent = new Intent(this.httpContext, PasosActivarCuentaEstudiante.class);
                        this.httpContext.startActivity(intent);
                    }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO")){
                        Bundle bundle = new Bundle();
                        bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_ESTUDIANTE);
                        Intent intent = new Intent(this.httpContext, PerfilBloqueado.class);
                        intent.putExtras(bundle);
                        this.httpContext.startActivity(intent);
                    }

                }else{
                    errorMsg("Usuario no registrado o tus datos están incorrectos.");
                }
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    public void errorMsg(String mensaje){
        new SweetAlert(this.httpContext, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

}

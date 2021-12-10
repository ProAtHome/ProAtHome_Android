package com.proathome.servicios.servicio;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;

import java.net.HttpURLConnection;

public class ServicioTaskCambiarEstatusServicio extends AsyncTask<Void, Void, String> {

    public ServicioTaskCambiarEstatusServicio(Context contexto, int idSesion, int idPerfil, int tipoPerfil, int estatus){
        Constants.contexto_CAMBIAR_ESTATUS = contexto;
        Constants.idSesion_CAMBIAR_ESTATUS = idSesion;
        Constants.idPerfil_CAMBIAR_ESTATUS = idPerfil;
        Constants.tipoPerfil_CAMBIAR_ESTATUS = tipoPerfil;
        Constants.estatus_CAMBIAR_ESTATUS = estatus;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(Constants.tipoPerfil_CAMBIAR_ESTATUS == DetallesFragment.CLIENTE)
            Constants.wsURL_CAMBIAR_ESTATUS = Constants.linkCambiarEstatusCliente_CAMBIAR_ESTATUS + Constants.idSesion_CAMBIAR_ESTATUS + "/" + Constants.idPerfil_CAMBIAR_ESTATUS + "/" + Constants.estatus_CAMBIAR_ESTATUS;
        else if(Constants.tipoPerfil_CAMBIAR_ESTATUS == DetallesSesionProfesionalFragment.PROFESIONAL)
            Constants.wsURL_CAMBIAR_ESTATUS = Constants.linkCambiarEstatusProfesional_CAMBIAR_ESTATUS + Constants.idSesion_CAMBIAR_ESTATUS + "/" + Constants.idPerfil_CAMBIAR_ESTATUS + "/" + Constants.estatus_CAMBIAR_ESTATUS;

        try{

            HttpURLConnection urlConnection = (HttpURLConnection) Constants.obtenerURL_CAMBIAR_ESTATUS().openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

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

                Constants.result_CAMBIAR_ESTATUS = sb.toString();
                in = null;
                sb = null;

            }else{
                Constants.result_CAMBIAR_ESTATUS = new String("Error: " +responseCode);
            }

            urlConnection = null;
        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return Constants.result_CAMBIAR_ESTATUS;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}

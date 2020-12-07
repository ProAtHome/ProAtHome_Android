package com.proathome.servicios.clase;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class ServicioTaskCambiarEstatusClase extends AsyncTask<Void, Void, String> {

    public ServicioTaskCambiarEstatusClase(Context contexto, int idSesion, int idPerfil, int tipoPerfil, int estatus){
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
        if(Constants.tipoPerfil_CAMBIAR_ESTATUS == DetallesFragment.ESTUDIANTE)
            Constants.wsURL_CAMBIAR_ESTATUS = Constants.linkCambiarEstatusEstudiante_CAMBIAR_ESTATUS + Constants.idSesion_CAMBIAR_ESTATUS + "/" + Constants.idPerfil_CAMBIAR_ESTATUS + "/" + Constants.estatus_CAMBIAR_ESTATUS;
        else if(Constants.tipoPerfil_CAMBIAR_ESTATUS == DetallesSesionProfesorFragment.PROFESOR)
            Constants.wsURL_CAMBIAR_ESTATUS = Constants.linkCambiarEstatusProfesor_CAMBIAR_ESTATUS + Constants.idSesion_CAMBIAR_ESTATUS + "/" + Constants.idPerfil_CAMBIAR_ESTATUS + "/" + Constants.estatus_CAMBIAR_ESTATUS;

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

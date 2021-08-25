package com.proathome.servicios.clase;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskGuardarProgreso extends AsyncTask<Void, Void, String> {

    public ServicioTaskGuardarProgreso(Context contexto, int idSesion, int idPerfil, int progreso, int progresoSegundos, int tipoDeTiempo){
        Constants.contexto_GUARDAR_PROGRESO = contexto;
        Constants.idSesion_GUARDAR_PROGRESO = idSesion;
        Constants.idPerfil_GUARDAR_PROGRESO = idPerfil;
        Constants.progreso_GUARDAR_PROGRESO = progreso;
        Constants.progresoSegundos_GUARDAR_PROGRESO = progresoSegundos;
        Constants.tipoDeTiempo_GUARDAR_PROGRESO = tipoDeTiempo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        Constants.wsURL_GUARDAR_PROGRESO = Constants.linkActualizarProgreso_GUARDAR_PROGRESO + Constants.idSesion_GUARDAR_PROGRESO + "/" + Constants.idPerfil_GUARDAR_PROGRESO + "/" + Constants.progreso_GUARDAR_PROGRESO + "/" + Constants.progresoSegundos_GUARDAR_PROGRESO + "/" + Constants.tipoDeTiempo_GUARDAR_PROGRESO;
        try{

            HttpsURLConnection urlConnection = (HttpsURLConnection) Constants.obtenerURL_GUARDAR_PROGRESO().openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

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

                Constants.result_GUARDAR_PROGRESO = sb.toString();
                in = null;
                sb = null;

            }else{
                Constants.result_GUARDAR_PROGRESO = new String("Error: " +responseCode);
            }

            urlConnection = null;

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return Constants.result_GUARDAR_PROGRESO;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

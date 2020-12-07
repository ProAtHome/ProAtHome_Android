package com.proathome.servicios.clase;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServicioTaskMasTiempo extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idSesion, idEstudiante, progresoTotal;
    private String linkActivarTE = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/activarTE/";

    public ServicioTaskMasTiempo(Context contexto, int idSesion, int idEstudiante, int progresoTotal){
        this.contexto = contexto;
        this.idSesion = idSesion;
        this.idEstudiante = idEstudiante;
        this.progresoTotal = progresoTotal;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        try{

            URL url = new URL(this.linkActivarTE + this.idSesion + "/" + this.idEstudiante + "/" + this.progresoTotal);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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

                result = sb.toString();
                in = null;
                sb = null;

            }else{
                result = new String("Error: "+ responseCode);
            }

            urlConnection = null;

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}

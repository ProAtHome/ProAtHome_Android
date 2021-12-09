package com.proathome.servicios.ayuda;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.utils.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskTicketSolucion extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idTicket, tipoUsuario;
    private String linkFinalizarTicket = Constants.IP + "/ProAtHome/apiProAtHome/cliente/finalizarTicket/";
    private String linkFinalizarTicketProfesional = Constants.IP + "/ProAtHome/apiProAtHome/profesional/finalizarTicket/";

    public ServicioTaskTicketSolucion(Context contexto, int idTicket, int tipoUsuario){
        this.contexto = contexto;
        this.idTicket = idTicket;
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = null;
            if(this.tipoUsuario == Constants.TIPO_USUARIO_CLIENTE)
                url = new URL(this.linkFinalizarTicket + idTicket);
            else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL)
                url = new URL(this.linkFinalizarTicketProfesional + idTicket);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();

            }else{
                resultado = "Error: " + responseCode;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}

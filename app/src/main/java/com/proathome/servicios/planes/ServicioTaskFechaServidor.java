package com.proathome.servicios.planes;

import android.os.AsyncTask;

import com.proathome.ui.fragments.DetallesGestionarFragment;
import com.proathome.ui.fragments.PlanesFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskFechaServidor extends AsyncTask<Void, Void, String> {

    private String linkFechaServidor = Constants.IP + "/ProAtHome/apiProAtHome/admin/fechaServidor";

    public ServicioTaskFechaServidor(){

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {
            URL url = new URL(this.linkFechaServidor);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";
                while ((linea = bufferedReader.readLine()) != null) {
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();
            }else {
                resultado = new String("Error: "+ responseCode);
            }

        }catch (MalformedURLException | ProtocolException ex){
            ex.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONObject jsonObject = new JSONObject(s);
            PlanesFragment.fechaServidor = jsonObject.getString("fechaServidor");
            DetallesGestionarFragment.fechaServidor = jsonObject.getString("fechaServidor");
        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

}

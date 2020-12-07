package com.proathome.servicios.estudiante;

import android.content.Context;
import android.os.AsyncTask;

import com.proathome.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskIniciarProcesoRuta extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idEstudiante;
    private String linkIniciarProcesoRuta = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/iniciarProcesoRuta";

    public ServicioTaskIniciarProcesoRuta(Context contexto, int idEstudiante){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = new URL(this.linkIniciarProcesoRuta);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametros = new JSONObject();
            parametros.put("idEstudiante", this.idEstudiante);

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(parametros));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer("");
                String linea ="";
                while ((linea = bufferedReader.readLine()) != null){

                    stringBuffer.append(linea);
                    break;

                }

                bufferedReader.close();
                resultado = stringBuffer.toString();

            }else{
                resultado = new String("Error: " + responseCode);
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public String getPostDataString(JSONObject params) throws Exception{
        return params.toString();
    }

}

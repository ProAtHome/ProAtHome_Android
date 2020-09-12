package com.proathome.controladores.estudiante;

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

public class ServicioTaskRutaExamen extends AsyncTask <Void, Void, String> {

    private Context contexto;
    private int idEstudiante, idSeccion, idNivel, idBloque, horas;
    private boolean sumar;
    private String linkSumarClase = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/nuevaRuta";

    public ServicioTaskRutaExamen(Context contexto, int idEstudiante, int idSeccion, int idNivel, int idBloque, int horas, boolean sumar){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
        this.idSeccion = idSeccion;
        this.idNivel = idNivel;
        this.idBloque = idBloque;
        this.horas = horas;
        this.sumar = sumar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = new URL(this.linkSumarClase);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametros = new JSONObject();
            parametros.put("idEstudiante", this.idEstudiante);
            parametros.put("idSeccion", this.idSeccion);
            parametros.put("idNivel", this.idNivel);
            parametros.put("idBloque", this.idBloque);
            parametros.put("horas", this.horas);
            parametros.put("fecha_registro", "Hoy");
            parametros.put("sumar", this.sumar);

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

    public String getPostDataString(JSONObject params) throws Exception{
        return params.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}

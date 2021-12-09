package com.proathome.servicios.profesional;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.fragments.BuscarSesionFragment;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskMatchSesion extends AsyncTask<Void, Void, String> {

    private String respuestaAPI, linkAPI;
    private Context contexto;
    private int idSesion, idProfesional;

    public ServicioTaskMatchSesion(Context contexto, String linkAPI, int idSesion, int idProfesional){
        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.idSesion = idSesion;
        this.idProfesional = idProfesional;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String result = null;
        String wsURL = this.linkAPI;
        URL url = null;

        try{
            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametrosPOST = new JSONObject();
            parametrosPOST.put("idProfesional", this.idProfesional);
            parametrosPOST.put("idSesion", this.idSesion);
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(parametrosPOST));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";

                while ((linea = in.readLine()) != null){
                    sb.append(linea);
                    break;
                }

                in.close();
                result = sb.toString();
            }else{
                result = new String("Error: " +responseCode);
            }

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.respuestaAPI = s;
        if(this.respuestaAPI != null){
            showMsg("¡GENIAL", this.respuestaAPI, SweetAlert.SUCCESS_TYPE);
        }else{
            showMsg("¡ERROR!", "Ocurrió un error inesperado", SweetAlert.ERROR_TYPE);
        }
    }

    public void showMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(BuscarSesionFragment.contexto, tipo, SweetAlert.PROFESIONAL)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}


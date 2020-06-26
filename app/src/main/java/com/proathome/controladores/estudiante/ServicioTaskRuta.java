package com.proathome.controladores.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.proathome.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServicioTaskRuta extends AsyncTask <Void, Void, String> {

    private Context contexto;
    private String respuesta;
    private String linkAPIEstadoRuta = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/estadoRutaAprendizaje/";
    private ProgressDialog progressDialog;
    private int idEstudiante, estado;

    public ServicioTaskRuta(Context contexto, int idEstudiante, int estado){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
        this.estado = estado;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Validando Ruta", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result =  null;

        if(this.estado == Constants.ESTADO_RUTA){
            String wsURL = this.linkAPIEstadoRuta + this.idEstudiante;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        try{
            JSONObject rutaJSON = new JSONObject(s);
            int estado = rutaJSON.getInt("estado");

            if(estado == Constants.INICIO_RUTA){
                Toast.makeText(this.contexto, "RUTA DE 0", Toast.LENGTH_LONG).show();
            }else if(estado == Constants.RUTA_ENCURSO) {
                int idBloque = rutaJSON.getInt("idBloque");
                int idNivel = rutaJSON.getInt("idNivel");
                int idSeccion = rutaJSON.getInt("idSeccion");
                ControladorRutaAprendizaje rutaAprendizaje = new ControladorRutaAprendizaje(this.contexto, idBloque, idNivel, idSeccion);
                rutaAprendizaje.evaluarRuta();
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

}

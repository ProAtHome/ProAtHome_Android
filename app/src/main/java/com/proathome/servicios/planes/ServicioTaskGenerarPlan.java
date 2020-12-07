package com.proathome.servicios.planes;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.servicios.clase.ServicioSesionesPagadas;
import com.proathome.fragments.OrdenCompraPlanFragment;
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

public class ServicioTaskGenerarPlan extends AsyncTask<Void, Void, String> {

    private String tipoPlan, fechaInicio, fechaFin;
    private int monedero, idEstudiante;
    private Context contexto;
    private String linkGenerarPlan = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/generarPlan";

    public ServicioTaskGenerarPlan(Context contexto, String tipoPlan, String fechaInicio, String fechaFin, int monedero, int idEstudiante){
        this.contexto = contexto;
        this.tipoPlan = tipoPlan;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monedero = monedero;
        this.idEstudiante = idEstudiante;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {

            URL url = new URL(this.linkGenerarPlan);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("tipoPlan", this.tipoPlan);
            parametrosPost.put("fechaInicio", this.fechaInicio);
            parametrosPost.put("fechaFin", this.fechaFin);
            parametrosPost.put("monedero", this.monedero);
            parametrosPost.put("idEstudiante", this.idEstudiante);

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //OBTENER EL RESULTADO DEL REQUEST
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(parametrosPost));
            writer.flush();
            writer.close();
            os.close();

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
                resultado = sb.toString();
            }
            else{
                resultado = new String("Error: "+ responseCode);
            }
        } catch (MalformedURLException e) {
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
        ServicioSesionesPagadas servicioSesionesPagadas = new ServicioSesionesPagadas(OrdenCompraPlanFragment.idEstudiante);
        servicioSesionesPagadas.execute();
        ServicioTaskValidarPlan validarPlan = new ServicioTaskValidarPlan(this.contexto, this.idEstudiante);
        validarPlan.execute();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

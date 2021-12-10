package com.proathome.servicios.cliente;

import android.os.AsyncTask;
import com.proathome.fragments.CobroFinalFragment;
import com.proathome.fragments.PreOrdenServicio;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskPreOrden extends AsyncTask<Void, Void, String> {

    public static final int PANTALLA_COBRO_FINAL = 2, PANTALLA_PRE_COBRO = 1;
    private int idCliente, idSesion, pantalla;
    private String linkPreOrden = Constants.IP + "/ProAtHome/apiProAtHome/cliente/obtenerPreOrden/";

    public ServicioTaskPreOrden(int idCliente, int idSesion, int pantalla){
        this.idCliente = idCliente;
        this.idSesion = idSesion;
        this.pantalla = pantalla;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = new URL(this.linkPreOrden + this.idCliente + "/" + this.idSesion);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while ((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();
            }else{
                resultado = new String("Error" + responseCode);
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONObject jsonObject = new JSONObject(s);
            if(this.pantalla == ServicioTaskPreOrden.PANTALLA_PRE_COBRO){
                PreOrdenServicio.nombreTitular = jsonObject.getString("nombreTitular");
                PreOrdenServicio.tarjeta = jsonObject.get("tarjeta").toString();
                PreOrdenServicio.mes = jsonObject.get("mes").toString();
                PreOrdenServicio.ano = jsonObject.get("ano").toString();
                PreOrdenServicio.sesion = "Sesión: " + Component.getSeccion(jsonObject.getInt("idSeccion")) + " / " + Component.getNivel(jsonObject.getInt("idSeccion"), jsonObject.getInt("idNivel")) + " / " + Component.getBloque(jsonObject.getInt("idBloque"));
                PreOrdenServicio.tiempo = "Tiempo: " + obtenerHorario(jsonObject.getInt("tiempo"));
            }else if(this.pantalla == ServicioTaskPreOrden.PANTALLA_COBRO_FINAL){
                CobroFinalFragment.nombreTitular = jsonObject.getString("nombreTitular");
                CobroFinalFragment.mes = jsonObject.get("mes").toString();
                CobroFinalFragment.ano = jsonObject.get("ano").toString();
                CobroFinalFragment.metodoRegistrado = jsonObject.getString("tarjeta");
                CobroFinalFragment.sesion = "Sesión: " + Component.getSeccion(jsonObject.getInt("idSeccion")) + " / " + Component.getNivel(jsonObject.getInt("idSeccion"), jsonObject.getInt("idNivel")) + " / " + Component.getBloque(jsonObject.getInt("idBloque"));
                CobroFinalFragment.tiempo = "Tiempo: " + obtenerHorario(jsonObject.getInt("tiempo"));
                CobroFinalFragment.nombreCliente = jsonObject.get("nombreCliente").toString();
                CobroFinalFragment.correo = jsonObject.get("correo").toString();
            }

        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

}
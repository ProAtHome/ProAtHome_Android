package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.proathome.servicios.planes.ServicioTaskActualizarMonedero;
import com.proathome.fragments.NuevaSesionFragment;

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

public class STRegistroSesionesEstudiante extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    public int idCliente, tiempo, idSeccion, idNivel, idBloque, personas;
    public String linkrequestAPI, horario, lugar, extras, tipoClase, actualizado, fecha, tipoPlan;
    public double latitud,longitud;
    private boolean sumar;

    public STRegistroSesionesEstudiante(Context contexto, String linkAPI, int idCliente, String horario, String lugar,
                                        int tiempo, int idSeccion, int idNivel, int idBloque, String extras, String tipoClase,
                                        double latitud, double longitud, String actualizado, String fecha, boolean sumar, String tipoPlan, int personas){

        this.contexto = contexto;
        this.linkrequestAPI = linkAPI;
        this.idCliente = idCliente;
        this.horario = horario;
        this.lugar = lugar;
        this.tiempo = tiempo;
        this.idSeccion = idSeccion;
        this.idNivel = idNivel;
        this.idBloque = idBloque;
        this.extras = extras;
        this.tipoClase = tipoClase;
        this.longitud = longitud;
        this.latitud = latitud;
        this.actualizado = actualizado;
        this.fecha = fecha;
        this.sumar = sumar;
        this.tipoPlan = tipoPlan;
        this.personas = personas;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Creando sesión", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        String wsURl = linkrequestAPI;

        URL url = null;
        try{

            url = new URL(wsURl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            JSONObject parametrosPOST = new JSONObject();
            parametrosPOST.put("idCliente", this.idCliente);
            parametrosPOST.put("horario", this.horario);
            parametrosPOST.put("lugar", this.lugar);
            parametrosPOST.put("tiempo", this.tiempo);
            parametrosPOST.put("idSeccion", this.idSeccion);
            parametrosPOST.put("idNivel", this.idNivel);
            parametrosPOST.put("idBloque", this.idBloque);
            parametrosPOST.put("extras", this.extras);
            parametrosPOST.put("tipoClase", this.tipoClase);
            parametrosPOST.put("latitud", this.latitud);
            parametrosPOST.put("longitud", this.longitud);
            parametrosPOST.put("actualizado", this.actualizado);
            parametrosPOST.put("fecha", this.fecha);
            parametrosPOST.put("sumar", this.sumar);
            parametrosPOST.put("tipoPlan", this.tipoPlan);
            parametrosPOST.put("personas", this.personas);

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
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
                String linea ="";
                while ((linea = in.readLine()) != null){

                    sb.append(linea);
                    break;

                }

                in.close();
                result = sb.toString();

            }else{

                result = new String("Error: " + responseCode);

            }

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch (IOException ex){
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
        progressDialog.dismiss();
        System.out.println(NuevaSesionFragment.nuevoMonedero);
        ServicioTaskActualizarMonedero actualizarMonedero = new ServicioTaskActualizarMonedero(this.contexto, idCliente, NuevaSesionFragment.nuevoMonedero);
        actualizarMonedero.execute();
        NuevaSesionFragment.nuevoMonedero = 0;
    }

    public String getPostDataString(JSONObject params) throws Exception{
        return params.toString();
    }

}

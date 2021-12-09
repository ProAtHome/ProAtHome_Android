package com.proathome.servicios.servicio;

import android.content.Context;
import android.os.AsyncTask;

import com.proathome.SincronizarServicio;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesionalFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskSincronizarServicios extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private  int idSesion, idPerfil, tipoPerfil, tipoSolicitud;
    private String linkSincronizarCliente =  Constants.IP + "/ProAtHome/apiProAtHome/cliente/sincronizarServicio/";
    private String getLinkSincronizarProfesional = Constants.IP + "/ProAtHome/apiProAtHome/profesional/sincronizarServicio/";
    private String linkdispCliente = Constants.IP + "/ProAtHome/apiProAtHome/cliente/servicioDisponible/";
    private String linkdispProfesional = Constants.IP + "/ProAtHome/apiProAtHome/profesional/servicioDisponible/";
    private String respuesta;
    private boolean disponible;

    public ServicioTaskSincronizarServicios(Context contexto, int idSesion, int idPerfil, int tipoPerfil, int tipoSolicitud, boolean disponible){
        this.contexto = contexto;
        this.idSesion = idSesion;
        this.idPerfil = idPerfil;
        this.tipoPerfil = tipoPerfil;
        this.tipoSolicitud = tipoSolicitud;
        this.disponible = disponible;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        String wsURL = "";

        if(this.tipoSolicitud == Constants.VERIFICAR_DISPONIBLIDAD){
            if(this.tipoPerfil == DetallesFragment.CLIENTE){
                wsURL = linkSincronizarCliente + this.idSesion + "/" + this.idPerfil;
            }else if(this.tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL){
                wsURL = getLinkSincronizarProfesional + this.idSesion + "/" + this.idPerfil;
            }

            try{

                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

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
                    result= sb.toString();
                    respuesta = result;

                }else{

                    result = new String("Error: "+ responseCode);
                    respuesta = null;

                }

            }catch(MalformedURLException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }else if(this.tipoSolicitud == Constants.CAMBIAR_DISPONIBILIDAD){
            if(this.tipoPerfil == DetallesFragment.CLIENTE){
                wsURL = linkdispCliente + this.idSesion + "/" + this.idPerfil + "/" + this.disponible;
            }else if(this.tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL){
                wsURL = linkdispProfesional + this.idSesion + "/" + this.idPerfil + "/" + this.disponible;
                System.out.println(wsURL);
            }

            try{

                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

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
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {

            if(this.tipoSolicitud == Constants.VERIFICAR_DISPONIBLIDAD){
                JSONObject resultado = new JSONObject(s);
                if(this.tipoPerfil == DetallesFragment.CLIENTE){
                    boolean disponibilidad = resultado.getBoolean("dispProfesional");
                    if(disponibilidad) {
                        SincronizarServicio.timer.cancel();
                        ServicioTaskObtenerProgreso servicioTaskObtenerProgreso = new ServicioTaskObtenerProgreso(this.contexto, this.idSesion, this.idPerfil, DetallesFragment.CLIENTE);
                        servicioTaskObtenerProgreso.execute();
                    }
                }else if(this.tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL){
                    boolean disponibilidad = resultado.getBoolean("dispCliente");
                    if(disponibilidad) {
                        SincronizarServicio.timer.cancel();
                        ServicioTaskObtenerProgreso servicioTaskObtenerProgreso = new ServicioTaskObtenerProgreso(this.contexto, this.idSesion, this.idPerfil, DetallesSesionProfesionalFragment.PROFESIONAL);
                        servicioTaskObtenerProgreso.execute();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

package com.proathome.servicios.servicio;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.proathome.ui.ServicioCliente;
import com.proathome.ui.ServicioProfesional;
import com.proathome.ui.SincronizarServicio;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.net.HttpURLConnection;

public class ServicioTaskObtenerProgreso extends AsyncTask<Void, Void, String> {

    public ServicioTaskObtenerProgreso(Context contexto, int idSesion, int idPerfil, int tipoPerfil){
        Constants.contexto_PROGRESO = contexto;
        Constants.idSesion_PROGRESO = idSesion;
        Constants.idPerfil_PROGRESO = idPerfil;
        Constants.tipoPerfil_PROGRESO = tipoPerfil;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        if(Constants.tipoPerfil_PROGRESO == DetallesFragment.CLIENTE){
            Constants.wsURL_PROGRESO = Constants.linkSincronizarCliente_PROGRESO + Constants.idSesion_PROGRESO + "/" + Constants.idPerfil_PROGRESO;
        }else if(Constants.tipoPerfil_PROGRESO == DetallesSesionProfesionalFragment.PROFESIONAL){
            Constants.wsURL_PROGRESO = Constants.linkSincronizarProfesional_PROGRESO + Constants.idSesion_PROGRESO + "/" + Constants.idPerfil_PROGRESO;
        }

        try{
            HttpURLConnection urlConnection = (HttpURLConnection) Constants.obtenerURL_PROGRESO().openConnection();

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

                Constants.result_PROGRESO = sb.toString();
                in = null;
                sb = null;

            }else{
                Constants.result_PROGRESO = new String("Error: "+ responseCode);
            }

            urlConnection = null;
        }catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Constants.result_PROGRESO;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            /*Aquí vamos a obtener el progreso inicial de el servicio dependiendo el perfil PROFESIONAL o CLIENTE para iniciar
             las variables correspondientes antes de entrar en la Actividad de el Servicio.*/
            JSONObject jsonObject = new JSONObject(s);
            if (Constants.tipoPerfil_PROGRESO == DetallesFragment.CLIENTE) {
                if(jsonObject.getBoolean("TE")){
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    ServicioCliente.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ServicioCliente.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idCliente", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                    intent.putExtra("idNivel", SincronizarServicio.idNivel);
                    intent.putExtra("idBloque", SincronizarServicio.idBloque);
                    intent.putExtra("sumar", SincronizarServicio.sumar);
                    intent.putExtra("tiempo", SincronizarServicio.tiempo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }else{
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progreso");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundos");
                    ServicioCliente.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ServicioCliente.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idCliente", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                    intent.putExtra("idNivel", SincronizarServicio.idNivel);
                    intent.putExtra("idBloque", SincronizarServicio.idBloque);
                    intent.putExtra("sumar", SincronizarServicio.sumar);
                    intent.putExtra("tiempo", SincronizarServicio.tiempo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }
            } else if (Constants.tipoPerfil_PROGRESO == DetallesSesionProfesionalFragment.PROFESIONAL) {
                if(jsonObject.getBoolean("TE")){
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    ServicioProfesional.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ServicioProfesional.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idProfesional", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                    intent.putExtra("idNivel", SincronizarServicio.idNivel);
                    intent.putExtra("idBloque", SincronizarServicio.idBloque);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }else{
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progreso");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundos");
                    ServicioProfesional.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ServicioProfesional.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idProfesional", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                    intent.putExtra("idNivel", SincronizarServicio.idNivel);
                    intent.putExtra("idBloque", SincronizarServicio.idBloque);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }
            }
            jsonObject = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

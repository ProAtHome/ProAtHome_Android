package com.proathome.controladores.clase;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.proathome.ClaseEstudiante;
import com.proathome.ClaseProfesor;
import com.proathome.SincronizarClase;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

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

        if(Constants.tipoPerfil_PROGRESO == DetallesFragment.ESTUDIANTE){
            Constants.wsURL_PROGRESO = Constants.linkSincronizarEstudiante_PROGRESO + Constants.idSesion_PROGRESO + "/" + Constants.idPerfil_PROGRESO;
        }else if(Constants.tipoPerfil_PROGRESO == DetallesSesionProfesorFragment.PROFESOR){
            Constants.wsURL_PROGRESO = Constants.linkSincronizarProfesor_PROGRESO + Constants.idSesion_PROGRESO + "/" + Constants.idPerfil_PROGRESO;
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
            /*Aquí vamos a obtener el progreso inicial de la clase dependiendo el perfil PROFESOR o ESTUDIANTE para iniciar
             las variables correspondientes antes de entrar en la Actividad de la Clase.*/
            JSONObject jsonObject = new JSONObject(s);
            if (Constants.tipoPerfil_PROGRESO == DetallesFragment.ESTUDIANTE) {
                if(jsonObject.getBoolean("TE")){
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    ClaseEstudiante.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ClaseEstudiante.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idEstudiante", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarClase.idSeccion);
                    intent.putExtra("idNivel", SincronizarClase.idNivel);
                    intent.putExtra("idBloque", SincronizarClase.idBloque);
                    intent.putExtra("sumar", SincronizarClase.sumar);
                    intent.putExtra("tiempo", SincronizarClase.tiempo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }else{
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progreso");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundos");
                    ClaseEstudiante.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ClaseEstudiante.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idEstudiante", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarClase.idSeccion);
                    intent.putExtra("idNivel", SincronizarClase.idNivel);
                    intent.putExtra("idBloque", SincronizarClase.idBloque);
                    intent.putExtra("sumar", SincronizarClase.sumar);
                    intent.putExtra("tiempo", SincronizarClase.tiempo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }
            } else if (Constants.tipoPerfil_PROGRESO == DetallesSesionProfesorFragment.PROFESOR) {
                if(jsonObject.getBoolean("TE")){
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    ClaseProfesor.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ClaseProfesor.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idProfesor", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarClase.idSeccion);
                    intent.putExtra("idNivel", SincronizarClase.idNivel);
                    intent.putExtra("idBloque", SincronizarClase.idBloque);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Constants.contexto_PROGRESO.startActivity(intent);
                }else{
                    Constants.progresoS_PROGRESO = jsonObject.getInt("progreso");
                    Constants.progresoSegundosS_PROGRESO = jsonObject.getInt("progresoSegundos");
                    ClaseProfesor.mTimeLeftMillis = (Constants.progresoS_PROGRESO * 60 * 1000) + (Constants.progresoSegundosS_PROGRESO * 1000);
                    Intent intent = new Intent(Constants.contexto_PROGRESO, ClaseProfesor.class);
                    intent.putExtra("idSesion", Constants.idSesion_PROGRESO);
                    intent.putExtra("idProfesor", Constants.idPerfil_PROGRESO);
                    intent.putExtra("idSeccion", SincronizarClase.idSeccion);
                    intent.putExtra("idNivel", SincronizarClase.idNivel);
                    intent.putExtra("idBloque", SincronizarClase.idBloque);
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

package com.proathome.controladores;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.net.URL;
import java.sql.SQLOutput;

public class ServicioTaskClase extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idPerfil, tipoPerfil, tipoSolicitud, idSesion, progresoSegundos, progreso;
    private String linkSincronizarEstudiante = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/validarEstatusClase/";
    private String getLinkSincronizarProfesor = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/validarEstatusClase/";
    private String linkActualizarProgreso = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/actualizarProgresoClase/";
    private String respuesta;

    public ServicioTaskClase(Context contexto, int idSesion, int idPerfil, int tipoPerfil, int tipoSolicitud, int progreso_estatus){
        this.contexto = contexto;
        this.idPerfil = idPerfil;
        this.tipoPerfil = tipoPerfil;
        this.tipoSolicitud = tipoSolicitud;
        this.idSesion = idSesion;
    }

    public ServicioTaskClase(Context contexto, int idSesion, int idPerfil, int tipoPerfil, int tipoSolicitud, int progreso_estatus, int progresoSegundos){
        this.contexto = contexto;
        this.idPerfil = idPerfil;
        this.tipoPerfil = tipoPerfil;
        this.tipoSolicitud = tipoSolicitud;
        this.idSesion = idSesion;
        this.progresoSegundos = progresoSegundos;
        this.progreso = progreso_estatus;
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
            //TODO Verificamos que sigan con disponibilidad
            //TODO Validar el estatus y el progreso, junto con la info de la sesión.
            if(this.tipoPerfil == DetallesFragment.ESTUDIANTE){
                wsURL = linkSincronizarEstudiante + this.idSesion + "/" + this.idPerfil;
            }else if(this.tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                wsURL = getLinkSincronizarProfesor + this.idSesion + "/" + this.idPerfil;
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
        }else if(this.tipoSolicitud == Constants.GUARDAR_PROGRESO){
            wsURL = linkActualizarProgreso + this.idSesion + "/" + this.idPerfil + "/" + this.progreso + "/" + this.progresoSegundos;

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
        }else if(this.tipoSolicitud == Constants.CAMBIAR_ESTATUS_CLASE){

        }else if(this.tipoSolicitud == Constants.OBTENER_PROGRESO_INFO){
            if(this.tipoPerfil == DetallesFragment.ESTUDIANTE){
                wsURL = linkSincronizarEstudiante + this.idSesion + "/" + this.idPerfil;
            }else if(this.tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                wsURL = getLinkSincronizarProfesor + this.idSesion + "/" + this.idPerfil;
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
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(this.tipoSolicitud == Constants.VERIFICAR_DISPONIBLIDAD){
            //TODO Verificamos que sigan con disponibilidad
            //TODO Validar el estatus y el progreso, junto con la info de la sesión.
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(this.tipoPerfil == DetallesFragment.ESTUDIANTE){
                    boolean dispProfesor = jsonObject.getBoolean("profDisponible");
                    int progreso = jsonObject.getInt("progreso");

                    if(!dispProfesor){
                        ClaseEstudiante.timer.cancel();
                        Intent intent = new Intent(this.contexto, SincronizarClase.class);
                        intent.putExtra("perfil", DetallesFragment.ESTUDIANTE);
                        intent.putExtra("idSesion", this.idSesion);
                        intent.putExtra("idPerfil", this.idPerfil);
                        intent.putExtra("tiempo", progreso);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.contexto.startActivity(intent);
                    }
                }else if(this.tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                    boolean dispEstudiante = jsonObject.getBoolean("estDisponible");
                    int progreso = jsonObject.getInt("progreso");
                    if(!dispEstudiante){
                        ClaseProfesor.timer.cancel();
                        Intent intent = new Intent(this.contexto, SincronizarClase.class);
                        intent.putExtra("perfil", DetallesSesionProfesorFragment.PROFESOR);
                        intent.putExtra("idSesion", this.idSesion);
                        intent.putExtra("idPerfil", this.idPerfil);
                        intent.putExtra("tiempo", progreso);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.contexto.startActivity(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(this.tipoSolicitud == Constants.GUARDAR_PROGRESO){

        }else if(this.tipoSolicitud == Constants.CAMBIAR_ESTATUS_CLASE){

        }else if(this.tipoSolicitud == Constants.OBTENER_PROGRESO_INFO){
            Toast.makeText(this.contexto, "Validando Clase prro", Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(this.tipoPerfil == DetallesFragment.ESTUDIANTE){
                    int progreso = jsonObject.getInt("progreso");
                    int progresoSegundos = jsonObject.getInt("progresoSegundos");
                    ClaseEstudiante.START_TIME_IN_MILLIS = (progreso * 60 * 1000) + (progresoSegundos * 1000);
                }else if(this.tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                    int progreso = jsonObject.getInt("progreso");
                    int progresoSegundos = jsonObject.getInt("progresoSegundos");
                    ClaseProfesor.START_TIME_IN_MILLIS = (progreso * 60 * 1000) + (progresoSegundos * 1000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}

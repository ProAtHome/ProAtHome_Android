package com.proathome.controladores;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.proathome.ClaseEstudiante;
import com.proathome.ClaseProfesor;
import com.proathome.R;
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


public class ServicioTaskClaseDisponible extends AsyncTask<Void, Void, String> {

    public ServicioTaskClaseDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, Activity activity){
        Constants.contexto_DISPONIBILIDAD_PROGRESO = contexto;
        Constants.idPerfil_DISPONIBILIDAD_PROGRESO = idPerfil;
        Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO = tipoPerfil;
        Constants.idSesion_DISPONIBILIDAD_PROGRESO = idSesion;
        Constants.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

            //TODO Verificamos que sigan con disponibilidad
            //TODO Validar el estatus y el progreso, junto con la info de la sesión.
            if(Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesFragment.ESTUDIANTE){
                Constants.wsURL_DISPONIBILIDAD_PROGRESO = Constants.linkSincronizarEstudiante_DISPONIBILIDAD_PROGRESO + Constants.idSesion_DISPONIBILIDAD_PROGRESO + "/" + Constants.idPerfil_DISPONIBILIDAD_PROGRESO;
            }else if(Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesorFragment.PROFESOR){
                Constants.wsURL_DISPONIBILIDAD_PROGRESO = Constants.linkSincronizarProfesor_DISPONIBILIDAD_PROGRESO + Constants.idSesion_DISPONIBILIDAD_PROGRESO + "/" + Constants.idPerfil_DISPONIBILIDAD_PROGRESO;
            }

            try{

                HttpURLConnection urlConnection = (HttpURLConnection) Constants.obtenerURL_DISPONIBILIDAD_PROGRESO().openConnection();

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

                    Constants.result_DISPONIBILIDAD_PROGRESO = sb.toString();
                    in = null;
                    sb = null;

                }else{
                    Constants.result_DISPONIBILIDAD_PROGRESO = new String("Error: "+ responseCode);
                }

                urlConnection = null;

            }catch(MalformedURLException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }

        return Constants.result_DISPONIBILIDAD_PROGRESO;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {

            JSONObject jsonObject = new JSONObject(s);

                if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesFragment.ESTUDIANTE) {

                    Constants.dispProfesor_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("profDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");

                    if(Constants.dispProfesor_DISPONIBILIDAD_PROGRESO){
                        if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO) {
                              if (ClaseEstudiante.encurso) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.startTimer();
                                    ClaseEstudiante.encurso = false;
                                    ClaseEstudiante.enpausa = true;
                                    ClaseEstudiante.inicio = true;
                              }

                        }else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA) {

                             if (ClaseEstudiante.enpausa) {
                                ClaseEstudiante.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                ClaseEstudiante.updateCountDownText();
                                ClaseEstudiante.pauseTimer();
                                ClaseEstudiante.encurso = true;
                                ClaseEstudiante.enpausa = false;
                                ClaseEstudiante.inicio = true;
                             }

                        }
                    }else{
                        Constants.activity.finish();
                    }

                } else if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesorFragment.PROFESOR) {//PROFESOOOOOOOOOOOOOOOOOOOOOOOOR

                    Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("estDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");

                    if(Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO){
                        if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA) {

                            if (ClaseProfesor.enpausa) {
                                ClaseProfesor.pausa_start.setText("Start");
                                ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.play));
                                ClaseProfesor.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                ClaseProfesor.updateCountDownText();
                                ClaseProfesor.encurso = true;
                                ClaseProfesor.enpausa = false;
                                ClaseProfesor.inicio = true;
                            }

                        } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO) {

                            if (ClaseProfesor.encurso) {
                                ClaseProfesor.pausa_start.setText("Pausar");
                                ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.pause));
                                ClaseProfesor.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                ClaseProfesor.encurso = false;
                                ClaseProfesor.enpausa = true;
                                ClaseProfesor.inicio = true;
                            }

                        }
                    }else{
                        Constants.activity.finish();
                    }


                //TODO Verificamos que sigan con disponibilidad
                //TODO Validar el estatus y el progreso, junto con la info de la sesión.
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
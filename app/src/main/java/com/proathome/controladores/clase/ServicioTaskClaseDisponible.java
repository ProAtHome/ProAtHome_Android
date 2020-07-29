package com.proathome.controladores.clase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.proathome.ClaseEstudiante;
import com.proathome.ClaseProfesor;
import com.proathome.R;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.fragments.MasTiempo;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


public class ServicioTaskClaseDisponible extends AsyncTask<Void, Void, String> {

    public ServicioTaskClaseDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity){
        Constants.contexto_DISPONIBILIDAD_PROGRESO = contexto;
        Constants.idPerfil_DISPONIBILIDAD_PROGRESO = idPerfil;
        Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO = tipoPerfil;
        Constants.idSesion_DISPONIBILIDAD_PROGRESO = idSesion;
        Constants.fragmentActivity = activity;
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
                    Constants.progresoTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    Constants.TE_activado_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("TE");

                    if(Constants.TE_activado_DISPONIBILIDAD_PROGRESO){//TODO Si tenemos tiempo en TE (Si estamos en tiempo extra)

                        //Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "Prueba de TE.", Toast.LENGTH_LONG).show();
                        if(Constants.dispProfesor_DISPONIBILIDAD_PROGRESO){

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA_TE) {

                                Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "En pausa TE.", Toast.LENGTH_LONG).show();
                                if (ClaseEstudiante.enpausa_TE) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.updateCountDownText();
                                    ClaseEstudiante.pauseTimer();
                                    ClaseEstudiante.encurso_TE = true;
                                    ClaseEstudiante.enpausa_TE = false;
                                    ClaseEstudiante.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO_TE) {

                                Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "En curso TE.", Toast.LENGTH_LONG).show();
                                if (ClaseEstudiante.encurso_TE) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.startTimer();
                                    ClaseEstudiante.encurso_TE = false;
                                    ClaseEstudiante.enpausa_TE = true;
                                    ClaseEstudiante.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO_TE) {

                                Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "Terminado TE.", Toast.LENGTH_LONG).show();
                                if(ClaseEstudiante.terminado_TE){
                                    //TODO Sumar a Ruta
                                    ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, Constants.FINALIZAR_CLASE, DetallesFragment.ESTUDIANTE);
                                    finalizarClase.execute();
                                    ServicioTaskSumarClaseRuta sumarClaseRuta = new ServicioTaskSumarClaseRuta(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, ClaseEstudiante.idSeccion, ClaseEstudiante.idNivel, ClaseEstudiante.idBloque, ClaseEstudiante.tiempo, ClaseEstudiante.sumar);
                                    sumarClaseRuta.execute();
                                    ClaseEstudiante.terminado_TE = false;
                                    ClaseEstudiante.timer.cancel();
                                    ClaseEstudiante.terminar.setVisibility(View.VISIBLE);
                                }

                            }

                        }else{
                            Constants.fragmentActivity.finish();
                        }
                        //TODO Validar EN_CURSO_TE

                        //TODO VALIDAR EN_PAUSA_TE

                        //TODO VALIDAR TERMINADO_TE

                    }else{//TODO Todo normal en ESTUDIANTE
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

                            }else if(Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO){
                                if(ClaseEstudiante.terminado){
                                    ClaseEstudiante.terminado = false;
                                    //ClaseEstudiante.timer.cancel();
                                    MasTiempo masTiempo = new MasTiempo();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                                    bundle.putInt("idEstudiante", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                                    masTiempo.setArguments(bundle);
                                    masTiempo.show(ClaseEstudiante.obtenerFargment(Constants.fragmentActivity), "Tiempo Extra");
                                }
                            }
                        }else{
                            Constants.fragmentActivity.finish();
                        }
                    }

                } else if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesorFragment.PROFESOR) {//PROFESOOOOOOOOOOOOOOOOOOOOOOOOR

                    Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("estDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");
                    Constants.progresoTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    Constants.TE_activado_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("TE");

                    if(Constants.TE_activado_DISPONIBILIDAD_PROGRESO){//TODO Si tenemos tiempo en TE (Si estamos en tiempo extra)

                        if(ClaseProfesor.schedule) {
                            ClaseProfesor.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                            ClaseProfesor.timer.cancel();
                            if(!ClaseProfesor.primeraVez){
                                ClaseProfesor.countDownTimer = null;
                                ClaseProfesor.startTimer();
                            }
                            ClaseProfesor.startSchedule();
                            System.out.println("Ya entro vale verga");

                        }
                        ClaseProfesor.schedule = false;

                        ClaseProfesor.pausa_start.setVisibility(View.VISIBLE); //TODO CAMBIAR A TERMINADO PARAR TIMERS.
                        ClaseProfesor.terminar.setVisibility(View.INVISIBLE);

                        if (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO <= 1 && Constants.progresoTE_DISPONIBILIDAD_PROGRESO < 1) {
                            ServicioTaskCambiarEstatusClase cambiarEstatusClase = new ServicioTaskCambiarEstatusClase(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO, Constants.ESTATUS_TERMINADO_TE);
                            cambiarEstatusClase.execute();
                            System.out.println("Terminandooooooooooooooooooooooo");
                            ClaseProfesor.pausa_start.setVisibility(View.INVISIBLE); //TODO CAMBIAR A TERMINADO PARAR TIMERS.
                            ClaseProfesor.terminar.setVisibility(View.VISIBLE);
                        }

                        //Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "Prueba de TE.", Toast.LENGTH_LONG).show();
                        if (Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO) {

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA_TE) {

                                Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "En pausa TE prof.", Toast.LENGTH_LONG).show();
                                if (ClaseProfesor.enpausa_TE) {
                                    ClaseProfesor.pausa_start.setText("Start");
                                    ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.play));
                                    ClaseProfesor.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseProfesor.updateCountDownText();
                                    ClaseProfesor.encurso_TE = true;
                                    ClaseProfesor.enpausa_TE = false;
                                    ClaseProfesor.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO_TE) {

                                if (ClaseProfesor.encurso_TE) {
                                    ClaseProfesor.pausa_start.setText("Pausar");
                                    ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.pause));
                                    ClaseProfesor.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseProfesor.encurso_TE = false;
                                    ClaseProfesor.enpausa_TE = true;
                                    ClaseProfesor.inicio_TE = true;
                                }
                                    Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "En curso TE prof.", Toast.LENGTH_LONG).show();

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO_TE) {

                                if (ClaseProfesor.terminado_TE) {
                                    ClaseProfesor.terminado_TE = false;
                                    ClaseProfesor.timerSchedule.cancel();
                                    ClaseProfesor.timer2.cancel();
                                }
                                Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, "Terminado TE prof.", Toast.LENGTH_LONG).show();

                            }

                        }else{
                            Constants.fragmentActivity.finish();
                        }
                        //TODO Validar EN_CURSO_TE

                        //TODO VALIDAR EN_PAUSA_TE

                        //TODO VALIDAR TERMINADO_TE

                    }else{//TODO Todo normal en PROFESOR

                        ClaseProfesor.primeraVez = false;
                        if (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO <= 1 && Constants.progreso_DISPONIBILIDAD_PROGRESO < 1) {
                            ServicioTaskCambiarEstatusClase cambiarEstatusClase = new ServicioTaskCambiarEstatusClase(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO, Constants.ESTATUS_TERMINADO);
                            cambiarEstatusClase.execute();
                            ClaseProfesor.timer.cancel();
                            ClaseProfesor.pausa_start.setVisibility(View.INVISIBLE); //TODO CAMBIAR A TERMINADO PARAR TIMERS.
                            ClaseProfesor.terminar.setVisibility(View.VISIBLE);
                        }

                        if (Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO) {
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

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO) {
                                if (ClaseProfesor.terminado) {
                                    ClaseProfesor.terminado = false;
                                    //ClaseProfesor.timer.cancel();
                                    //ClaseProfesor.timer2.cancel();
                                }
                            }
                        } else {
                            Constants.fragmentActivity.finish();
                        }
                    }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
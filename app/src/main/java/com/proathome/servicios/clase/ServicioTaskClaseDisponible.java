package com.proathome.servicios.clase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;


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

            /*VALIDAMOS que tipo de perfil, profesor o estudiante está pidiendo ver la disponibilidad de la clase o el progreso de ésta
             y seleccionamos la URI correspondiente a el perfil.*/
            if(Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesFragment.ESTUDIANTE){
                Constants.wsURL_DISPONIBILIDAD_PROGRESO = Constants.linkSincronizarEstudiante_DISPONIBILIDAD_PROGRESO + Constants.idSesion_DISPONIBILIDAD_PROGRESO + "/" + Constants.idPerfil_DISPONIBILIDAD_PROGRESO;
            }else if(Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesorFragment.PROFESOR){
                Constants.wsURL_DISPONIBILIDAD_PROGRESO = Constants.linkSincronizarProfesor_DISPONIBILIDAD_PROGRESO + Constants.idSesion_DISPONIBILIDAD_PROGRESO + "/" + Constants.idPerfil_DISPONIBILIDAD_PROGRESO;
            }

            try{

                HttpsURLConnection urlConnection = (HttpsURLConnection) Constants.obtenerURL_DISPONIBILIDAD_PROGRESO().openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK){

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

                if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesFragment.ESTUDIANTE) {//Si el tipo de PERFIL es = ESTUDIANTE

                    //Iniciamos la variables correspondiente con el profreso de la clase
                    Constants.dispProfesor_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("profDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");
                    Constants.progresoTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    Constants.TE_activado_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("TE");

                    if(Constants.TE_activado_DISPONIBILIDAD_PROGRESO){// Si tenemos tiempo en TE (Si estamos en tiempo extra)

                        if(Constants.dispProfesor_DISPONIBILIDAD_PROGRESO){//Si el profesor está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA_TE) {//Si el estatus de la CLASE - TE = PAUSA.

                                if (ClaseEstudiante.enpausa_TE) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.updateCountDownText();
                                    ClaseEstudiante.pauseTimer();
                                    ClaseEstudiante.encurso_TE = true;
                                    ClaseEstudiante.enpausa_TE = false;
                                    ClaseEstudiante.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO_TE) {//Si el estatus de la CLASE - TE = EN CURSO.

                                if (ClaseEstudiante.encurso_TE) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.startTimer();
                                    ClaseEstudiante.encurso_TE = false;
                                    ClaseEstudiante.enpausa_TE = true;
                                    ClaseEstudiante.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO_TE) {//Si el estatus de la CLASE - TE = FINALLIZADO.

                                if(ClaseEstudiante.terminado_TE){
                                    //Finalizamos la Clase.
                                    DetallesFragment.procedenciaFin = true;
                                    ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, Constants.FINALIZAR_CLASE, DetallesFragment.ESTUDIANTE);
                                    finalizarClase.execute();
                                    //Sumamos la ruta de Aprendizaje.
                                    //TODO FLUJO_EVALUACION: Mostrar modal de evaluación;
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

                    }else{//Si el tiempo es normal en ESTUDIANTE
                        if(Constants.dispProfesor_DISPONIBILIDAD_PROGRESO){//Si el profesor está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO) {//Si el estatus de la CLASE = EN CURSO.

                                if (ClaseEstudiante.encurso) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.startTimer();
                                    ClaseEstudiante.encurso = false;
                                    ClaseEstudiante.enpausa = true;
                                    ClaseEstudiante.inicio = true;
                                }

                            }else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA) {//Si el estatus de la CLASE = PAUSA.

                                if (ClaseEstudiante.enpausa) {
                                    ClaseEstudiante.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseEstudiante.updateCountDownText();
                                    ClaseEstudiante.pauseTimer();
                                    ClaseEstudiante.encurso = true;
                                    ClaseEstudiante.enpausa = false;
                                    ClaseEstudiante.inicio = true;
                                }

                            }else if(Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO){//Si el estatus de la CLASE = FINALIZADO ANTES DE VERIFICAR TE.
                                if(ClaseEstudiante.terminado){
                                    ClaseEstudiante.terminado = false;
                                    //Preguntamos si desea más tiempo Extra.
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

                } else if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesorFragment.PROFESOR) {//Si el tipo de Perfil = PROFESOR.

                    //Iniciamos las variables correspondientes a la info progreso de la clase.
                    Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("estDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");
                    Constants.progresoTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    Constants.TE_activado_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("TE");

                    if(Constants.TE_activado_DISPONIBILIDAD_PROGRESO){//Si tenemos tiempo en TE (Si estamos en tiempo extra)

                        if(ClaseProfesor.schedule) {//Banderas para iniciar una sola vez el contador.
                            ClaseProfesor.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                            ClaseProfesor.timer.cancel();
                            if(!ClaseProfesor.primeraVez){
                                ClaseProfesor.countDownTimer = null;
                                ClaseProfesor.startTimer();
                            }
                            ClaseProfesor.startSchedule();

                        }
                        ClaseProfesor.schedule = false;

                        //Cambiamos los botones de Inicio.
                        ClaseProfesor.pausa_start.setVisibility(View.VISIBLE);
                        ClaseProfesor.terminar.setVisibility(View.INVISIBLE);

                        if (Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO) {//Si el estudiante está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA_TE) {//Si el estatus de la CLASE - TE = PAUSA.

                                if (ClaseProfesor.enpausa_TE) {
                                    ClaseProfesor.pausa_start.setText("Start");
                                    ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.play));
                                    ClaseProfesor.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseProfesor.updateCountDownText();
                                    ClaseProfesor.encurso_TE = true;
                                    ClaseProfesor.enpausa_TE = false;
                                    ClaseProfesor.inicio_TE = true;
                                    DetallesSesionProfesorFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO_TE) {//Si el estatus de la CLASE - TE = EN CURSO.

                                if (ClaseProfesor.encurso_TE) {
                                    ClaseProfesor.pausa_start.setText("Pausar");
                                    ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.pause));
                                    ClaseProfesor.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseProfesor.encurso_TE = false;
                                    ClaseProfesor.enpausa_TE = true;
                                    ClaseProfesor.inicio_TE = true;
                                    DetallesSesionProfesorFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO_TE) {//Si el estatus de la CLASE - TE = FINALIZADO.

                                if (ClaseProfesor.terminado_TE) {
                                    ClaseProfesor.terminado_TE = false;
                                    ClaseProfesor.timerSchedule.cancel();
                                    ClaseProfesor.timer2.cancel();
                                    DetallesSesionProfesorFragment.procedenciaFin = true;
                                }

                            }

                        }else{
                            Constants.fragmentActivity.finish();
                        }

                        //Vamos a verificar el progreso del Tiempo extra acabó para cambiar el estatus de la clase.
                        if (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO <= 1 && Constants.progresoTE_DISPONIBILIDAD_PROGRESO < 1) {
                            ServicioTaskCambiarEstatusClase cambiarEstatusClase = new ServicioTaskCambiarEstatusClase(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO, Constants.ESTATUS_TERMINADO_TE);
                            cambiarEstatusClase.execute();
                            ClaseProfesor.pausa_start.setVisibility(View.INVISIBLE);
                            ClaseProfesor.terminar.setVisibility(View.VISIBLE);
                            DetallesSesionProfesorFragment.procedenciaFin = true;
                        }

                    }else{//Tiempo normal en PROFESOR

                        ClaseProfesor.primeraVez = false;
                        //Vamos a verificar el progreso del Tiempo normal para cambiar el estatus de la clase.
                        if (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO <= 1 && Constants.progreso_DISPONIBILIDAD_PROGRESO < 1) {
                            ServicioTaskCambiarEstatusClase cambiarEstatusClase = new ServicioTaskCambiarEstatusClase(Constants.contexto_DISPONIBILIDAD_PROGRESO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO, Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO, Constants.ESTATUS_TERMINADO);
                            cambiarEstatusClase.execute();
                            ClaseProfesor.timer.cancel();
                            ClaseProfesor.pausa_start.setVisibility(View.INVISIBLE);
                            ClaseProfesor.terminar.setVisibility(View.VISIBLE);
                            DetallesSesionProfesorFragment.procedenciaFin = true;
                        }

                        if (Constants.dispEstudiante_DISPONIBILIDAD_PROGRESO) {// Si el estudiante está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA) {//Si el estatus de la CLASE = PAUSA.

                                if (ClaseProfesor.enpausa) {
                                    ClaseProfesor.pausa_start.setText("Start");
                                    ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.play));
                                    ClaseProfesor.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseProfesor.updateCountDownText();
                                    ClaseProfesor.encurso = true;
                                    ClaseProfesor.enpausa = false;
                                    ClaseProfesor.inicio = true;
                                    DetallesSesionProfesorFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO) {//Si el estatus de la CLASE = EN CURSO.

                                if (ClaseProfesor.encurso) {
                                    ClaseProfesor.pausa_start.setText("Pausar");
                                    ClaseProfesor.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.pause));
                                    ClaseProfesor.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ClaseProfesor.encurso = false;
                                    ClaseProfesor.enpausa = true;
                                    ClaseProfesor.inicio = true;
                                    DetallesSesionProfesorFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO) {//Si el estatus de la CLASE = FINALIZADO.

                                if (ClaseProfesor.terminado) {
                                    ClaseProfesor.terminado = false;
                                    DetallesSesionProfesorFragment.procedenciaFin = true;
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
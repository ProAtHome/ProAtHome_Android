package com.proathome.servicios.sesiones;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.ServicioCliente;
import com.proathome.ui.ServicioProfesional;
import com.proathome.R;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.ui.fragments.MasTiempo;
import com.proathome.utils.Constants;
import com.proathome.utils.FechaActual;
import com.proathome.utils.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;


public class ServicioSesionDisponible extends AsyncTask<Void, Void, String> {

    public ServicioSesionDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity){
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

            /*VALIDAMOS que tipo de perfil, profesional o cliente está pidiendo ver la disponibilidad de el servicio o el progreso de ésta
             y seleccionamos la URI correspondiente a el perfil.*/
            if(Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesFragment.CLIENTE){
                Constants.wsURL_DISPONIBILIDAD_PROGRESO = Constants.linkSincronizarCliente_DISPONIBILIDAD_PROGRESO +
                        Constants.idSesion_DISPONIBILIDAD_PROGRESO + "/" + Constants.idPerfil_DISPONIBILIDAD_PROGRESO + "/" + SharedPreferencesManager.getInstance(Constants.contexto_DISPONIBILIDAD_PROGRESO).getTokenCliente();
            }else if(Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesionalFragment.PROFESIONAL){
                Constants.wsURL_DISPONIBILIDAD_PROGRESO = Constants.linkSincronizarProfesional_DISPONIBILIDAD_PROGRESO +
                        Constants.idSesion_DISPONIBILIDAD_PROGRESO + "/" + Constants.idPerfil_DISPONIBILIDAD_PROGRESO + "/" + SharedPreferencesManager.getInstance(Constants.contexto_DISPONIBILIDAD_PROGRESO).getTokenProfesional();
            }

        System.out.println(Constants.wsURL_DISPONIBILIDAD_PROGRESO);
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
            JSONObject data = new JSONObject(s);
            if(data.getBoolean("respuesta")){
                JSONObject jsonObject = data.getJSONObject("mensaje");

                if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesFragment.CLIENTE) {//Si el tipo de PERFIL es = CLIENTE

                    //Iniciamos la variables correspondiente con el profreso de el servicio
                    Constants.dispProfesional_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("profDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");
                    Constants.progresoTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    Constants.TE_activado_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("TE");

                    if(Constants.TE_activado_DISPONIBILIDAD_PROGRESO){// Si tenemos tiempo en TE (Si estamos en tiempo extra)

                        if(Constants.dispProfesional_DISPONIBILIDAD_PROGRESO){//Si el profesional está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA_TE) {//Si el estatus de la SERVICIO - TE = PAUSA.

                                if (ServicioCliente.enpausa_TE) {
                                    ServicioCliente.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioCliente.updateCountDownText();
                                    ServicioCliente.pauseTimer();
                                    ServicioCliente.encurso_TE = true;
                                    ServicioCliente.enpausa_TE = false;
                                    ServicioCliente.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO_TE) {//Si el estatus de la SERVICIO - TE = EN CURSO.

                                if (ServicioCliente.encurso_TE) {
                                    ServicioCliente.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioCliente.startTimer();
                                    ServicioCliente.encurso_TE = false;
                                    ServicioCliente.enpausa_TE = true;
                                    ServicioCliente.inicio_TE = true;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO_TE) {//Si el estatus de la SERVICIO - TE = FINALLIZADO.

                                if(ServicioCliente.terminado_TE){
                                    //Finalizamos el Servicio.
                                    DetallesFragment.procedenciaFin = true;
                                    ServiciosSesion.finalizar(Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                                    //Sumamos la ruta de Aprendizaje.
                                    //TODO FLUJO_EVALUACION: Mostrar modal de evaluación;
                                    sumarSevicioRuta();
                                    ServicioCliente.terminado_TE = false;
                                    ServicioCliente.timer.cancel();
                                    ServicioCliente.terminar.setVisibility(View.VISIBLE);
                                }

                            }

                        }else{
                            Constants.fragmentActivity.finish();
                        }

                    }else{//Si el tiempo es normal en CLIENTE
                        if(Constants.dispProfesional_DISPONIBILIDAD_PROGRESO){//Si el profesional está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO) {//Si el estatus de la SERVICIO = EN CURSO.

                                if (ServicioCliente.encurso) {
                                    ServicioCliente.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioCliente.startTimer();
                                    ServicioCliente.encurso = false;
                                    ServicioCliente.enpausa = true;
                                    ServicioCliente.inicio = true;
                                }

                            }else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA) {//Si el estatus de la SERVICIO = PAUSA.

                                if (ServicioCliente.enpausa) {
                                    ServicioCliente.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioCliente.updateCountDownText();
                                    ServicioCliente.pauseTimer();
                                    ServicioCliente.encurso = true;
                                    ServicioCliente.enpausa = false;
                                    ServicioCliente.inicio = true;
                                }

                            }else if(Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO){//Si el estatus de la SERVICIO = FINALIZADO ANTES DE VERIFICAR TE.
                                if(ServicioCliente.terminado){
                                    ServicioCliente.terminado = false;
                                    //Preguntamos si desea más tiempo Extra.
                                    MasTiempo masTiempo = new MasTiempo();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                                    bundle.putInt("idCliente", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                                    masTiempo.setArguments(bundle);
                                    masTiempo.show(ServicioCliente.obtenerFargment(Constants.fragmentActivity), "Tiempo Extra");
                                }
                            }
                        }else{
                            Constants.fragmentActivity.finish();
                        }
                    }

                } else if (Constants.tipoPerfil_DISPONIBILIDAD_PROGRESO == DetallesSesionProfesionalFragment.PROFESIONAL) {//Si el tipo de Perfil = PROFESIONAL.

                    //Iniciamos las variables correspondientes a la info progreso de el servicio.
                    Constants.dispCliente_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("estDisponible");
                    Constants.progreso_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progreso");
                    Constants.estatus_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("estatus");
                    Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundos");
                    Constants.progresoTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoTE");
                    Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO = jsonObject.getInt("progresoSegundosTE");
                    Constants.TE_activado_DISPONIBILIDAD_PROGRESO = jsonObject.getBoolean("TE");

                    if(Constants.TE_activado_DISPONIBILIDAD_PROGRESO){//Si tenemos tiempo en TE (Si estamos en tiempo extra)

                        if(ServicioProfesional.schedule) {//Banderas para iniciar una sola vez el contador.
                            ServicioProfesional.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                            ServicioProfesional.timer.cancel();
                            if(!ServicioProfesional.primeraVez){
                                ServicioProfesional.countDownTimer = null;
                                ServicioProfesional.startTimer();
                            }
                            ServicioProfesional.startSchedule();

                        }
                        ServicioProfesional.schedule = false;

                        //Cambiamos los botones de Inicio.
                        ServicioProfesional.pausa_start.setVisibility(View.VISIBLE);
                        ServicioProfesional.terminar.setVisibility(View.INVISIBLE);

                        if (Constants.dispCliente_DISPONIBILIDAD_PROGRESO) {//Si el cliente está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA_TE) {//Si el estatus de la SERVICIO - TE = PAUSA.

                                if (ServicioProfesional.enpausa_TE) {
                                    ServicioProfesional.pausa_start.setText("Start");
                                    ServicioProfesional.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.play));
                                    ServicioProfesional.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioProfesional.updateCountDownText();
                                    ServicioProfesional.encurso_TE = true;
                                    ServicioProfesional.enpausa_TE = false;
                                    ServicioProfesional.inicio_TE = true;
                                    DetallesSesionProfesionalFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO_TE) {//Si el estatus de la SERVICIO - TE = EN CURSO.

                                if (ServicioProfesional.encurso_TE) {
                                    ServicioProfesional.pausa_start.setText("Pausar");
                                    ServicioProfesional.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.pause));
                                    ServicioProfesional.mTimeLeftMillis = (Constants.progresoTE_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioProfesional.encurso_TE = false;
                                    ServicioProfesional.enpausa_TE = true;
                                    ServicioProfesional.inicio_TE = true;
                                    DetallesSesionProfesionalFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO_TE) {//Si el estatus de la SERVICIO - TE = FINALIZADO.

                                if (ServicioProfesional.terminado_TE) {
                                    ServicioProfesional.terminado_TE = false;
                                    ServicioProfesional.timerSchedule.cancel();
                                    ServicioProfesional.timer2.cancel();
                                    DetallesSesionProfesionalFragment.procedenciaFin = true;
                                }

                            }

                        }else{
                            Constants.fragmentActivity.finish();
                        }

                        //Vamos a verificar el progreso del Tiempo extra acabó para cambiar el estatus de el servicio.
                        if (Constants.progresoSegundosTE_DISPONIBILIDAD_PROGRESO <= 1 && Constants.progresoTE_DISPONIBILIDAD_PROGRESO < 1) {
                            ServiciosSesion.cambiarEstatusServicio(Constants.ESTATUS_TERMINADO_TE,  Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                            ServicioProfesional.pausa_start.setVisibility(View.INVISIBLE);
                            ServicioProfesional.terminar.setVisibility(View.VISIBLE);
                            DetallesSesionProfesionalFragment.procedenciaFin = true;
                        }

                    }else{//Tiempo normal en PROFESIONAL

                        ServicioProfesional.primeraVez = false;
                        //Vamos a verificar el progreso del Tiempo normal para cambiar el estatus de el servicio.
                        if (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO <= 1 && Constants.progreso_DISPONIBILIDAD_PROGRESO < 1) {
                            ServiciosSesion.cambiarEstatusServicio(Constants.ESTATUS_TERMINADO, Constants.idSesion_DISPONIBILIDAD_PROGRESO, Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                            ServicioProfesional.timer.cancel();
                            ServicioProfesional.pausa_start.setVisibility(View.INVISIBLE);
                            ServicioProfesional.terminar.setVisibility(View.VISIBLE);
                            DetallesSesionProfesionalFragment.procedenciaFin = true;
                        }

                        if (Constants.dispCliente_DISPONIBILIDAD_PROGRESO) {// Si el cliente está en conexión.

                            if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENPAUSA) {//Si el estatus de la SERVICIO = PAUSA.

                                if (ServicioProfesional.enpausa) {
                                    ServicioProfesional.pausa_start.setText("Start");
                                    ServicioProfesional.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.play));
                                    ServicioProfesional.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioProfesional.updateCountDownText();
                                    ServicioProfesional.encurso = true;
                                    ServicioProfesional.enpausa = false;
                                    ServicioProfesional.inicio = true;
                                    DetallesSesionProfesionalFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_ENCURSO) {//Si el estatus de la SERVICIO = EN CURSO.

                                if (ServicioProfesional.encurso) {
                                    ServicioProfesional.pausa_start.setText("Pausar");
                                    ServicioProfesional.pausa_start.setIcon(Constants.contexto_DISPONIBILIDAD_PROGRESO.getDrawable(R.drawable.pause));
                                    ServicioProfesional.mTimeLeftMillis = (Constants.progreso_DISPONIBILIDAD_PROGRESO * 60 * 1000) + (Constants.progresoSegundos_DISPONIBILIDAD_PROGRESO * 1000);
                                    ServicioProfesional.encurso = false;
                                    ServicioProfesional.enpausa = true;
                                    ServicioProfesional.inicio = true;
                                    DetallesSesionProfesionalFragment.procedenciaFin = false;
                                }

                            } else if (Constants.estatus_DISPONIBILIDAD_PROGRESO == Constants.ESTATUS_TERMINADO) {//Si el estatus de la SERVICIO = FINALIZADO.

                                if (ServicioProfesional.terminado) {
                                    ServicioProfesional.terminado = false;
                                    DetallesSesionProfesionalFragment.procedenciaFin = true;
                                }

                            }
                        } else {
                            Constants.fragmentActivity.finish();
                        }
                    }

                }
            }else
                Toast.makeText(Constants.contexto_DISPONIBILIDAD_PROGRESO, data.getString("mensaje"), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sumarSevicioRuta(){
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("idCliente", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
            parametros.put("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
            parametros.put("idSeccion", ServicioCliente.idSeccion);
            parametros.put("idNivel",  ServicioCliente.idNivel);
            parametros.put("idBloque", ServicioCliente.idBloque);
            parametros.put("horasA_sumar", ServicioCliente.tiempo);
            parametros.put("fecha_registro", FechaActual.getFechaActual());
            parametros.put("sumar", ServicioCliente.sumar);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                //TODO AQUI PODEMOS PONER UN ANUNCIO DE FIN DE RUTA CUANDO LA VAR ultimaSesion SEA TRUE.
                DetallesFragment.procedenciaFin = true;
            }, APIEndPoints.SUMAR_SERVICIO_RUTA, WebServicesAPI.POST, parametros);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
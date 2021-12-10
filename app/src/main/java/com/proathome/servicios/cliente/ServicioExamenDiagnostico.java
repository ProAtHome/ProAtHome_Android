package com.proathome.servicios.cliente;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.ui.examen.Diagnostico1;
import com.proathome.ui.examen.Diagnostico2;
import com.proathome.ui.examen.Diagnostico3;
import com.proathome.ui.examen.Diagnostico4;
import com.proathome.ui.examen.Diagnostico5;
import com.proathome.ui.examen.Diagnostico6;
import com.proathome.ui.examen.Diagnostico7;
import com.proathome.ui.examen.EvaluarRuta;
import com.proathome.ui.fragments.FragmentRutaGenerada;
import com.proathome.ui.ruta.RutaFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioExamenDiagnostico extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int estatus;
    public String respuesta;
    private String linkAPIestatus = Constants.IP + "/ProAtHome/apiProAtHome/cliente/estatusExamenDiagnostico/";
    private String linkAPIinicio_cancelar = Constants.IP + "/ProAtHome/apiProAtHome/cliente/examenDiagnostico";
    private String linkAPIenCurso = Constants.IP + "/ProAtHome/apiProAtHome/cliente/enCursoExamenDiagnostico";
    private String linkAPIinfoExamenFinal = Constants.IP + "/ProAtHome/apiProAtHome/cliente/infoExamenDiagnosticoFinal/";
    private String linkAPIinfoExamen = Constants.IP + "/ProAtHome/apiProAtHome/cliente/infoExamenDiagnostico/";
    private String linkAPIreiniciar = Constants.IP + "/ProAtHome/apiProAtHome/cliente/reiniciarExamenDiagnostico";
    private int idCliente;
    private int puntacionAsumar;
    private int preguntaActual;
    private ProgressDialog progressDialog;
    private Activity activity;
    private Class activitySiguiente;

    public ServicioExamenDiagnostico(Context contexto, int idCliente, int estatus){
        this.contexto = contexto;
        this.idCliente = idCliente;
        this.estatus = estatus;
    }

    public ServicioExamenDiagnostico(Context contexto, int idCliente, int estatus, int puntuacionAsumar){
        this.contexto = contexto;
        this.idCliente = idCliente;
        this.estatus = estatus;
        this.puntacionAsumar = puntuacionAsumar;
    }

    public ServicioExamenDiagnostico(Context contexto, int idCliente, Activity activity, Class activitySiguiente, int estatus, int puntuacionAsumar, int preguntaActual){
        this.contexto = contexto;
        this.activity = activity;
        this.activitySiguiente = activitySiguiente;
        this.idCliente = idCliente;
        this.estatus = estatus;
        this.puntacionAsumar = puntuacionAsumar;
        this.preguntaActual = preguntaActual;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Validando Examen", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result =  null;

        if(this.estatus == Constants.ESTATUS_EXAMEN){
            String wsURL = this.linkAPIestatus + this.idCliente;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.CANCELADO_EXAMEN){
            String wsURL = this.linkAPIinicio_cancelar;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject cancelar = new JSONObject();
                cancelar.put("idCliente", this.idCliente);
                cancelar.put("aciertos", 0);
                cancelar.put("preguntaActual", 0);
                cancelar.put("estatus", Constants.CANCELADO_EXAMEN);

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //OBTENER EL RESULTADO DEL REQUEST
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(cancelar));
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.INFO_EXAMEN_FINAL){
            String wsURL = this.linkAPIinfoExamenFinal + this.idCliente;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.INICIO_EXAMEN){
            String wsURL = this.linkAPIinicio_cancelar;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject cancelar = new JSONObject();
                cancelar.put("idCliente", this.idCliente);
                cancelar.put("aciertos", this.puntacionAsumar);
                cancelar.put("preguntaActual", this.preguntaActual);
                cancelar.put("estatus", Constants.INICIO_EXAMEN);

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //OBTENER EL RESULTADO DEL REQUEST
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(cancelar));
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.ENCURSO_EXAMEN){
            String wsURL = this.linkAPIenCurso;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject cancelar = new JSONObject();
                cancelar.put("idCliente", this.idCliente);
                cancelar.put("aciertos", this.puntacionAsumar);
                cancelar.put("preguntaActual", this.preguntaActual);
                cancelar.put("estatus", Constants.ENCURSO_EXAMEN);

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //OBTENER EL RESULTADO DEL REQUEST
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(cancelar));
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.EXAMEN_FINALIZADO){
            String wsURL = this.linkAPIenCurso;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject cancelar = new JSONObject();
                cancelar.put("idCliente", this.idCliente);
                cancelar.put("aciertos", this.puntacionAsumar);
                cancelar.put("preguntaActual", this.preguntaActual);
                cancelar.put("estatus", Constants.EXAMEN_FINALIZADO);

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //OBTENER EL RESULTADO DEL REQUEST
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(cancelar));
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.CONTINUAR_EXAMEN){
            String wsURL = this.linkAPIinfoExamen + this.idCliente;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(this.estatus == Constants.REINICIAR_EXAMEN){
            String wsURL = this.linkAPIreiniciar;
            try{
                URL url = new URL(wsURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject cancelar = new JSONObject();
                cancelar.put("idCliente", this.idCliente);
                cancelar.put("aciertos", 0);
                cancelar.put("preguntaActual", 0);
                cancelar.put("estatus", Constants.REINICIAR_EXAMEN);

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //OBTENER EL RESULTADO DEL REQUEST
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(getPostDataString(cancelar));
                writer.flush();
                writer.close();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    result= stringBuffer.toString();
                    this.respuesta = result;
                }else{
                    result = new String("Error: "+ responseCode);
                    this.respuesta = null;
                }

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        try{
            JSONObject jsonObject = new JSONObject(s);
            int estatus = jsonObject.getInt("estatus");
            if(estatus == Constants.INICIO_EXAMEN){
                new MaterialAlertDialogBuilder(this.contexto, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon_CenterStacked)
                        .setTitle("EXÁMEN DIAGNÓSTICO")
                        .setMessage("Tenemos un exámen para evaluar tus habilidades y colocarte en la ruta de aprendizaje de" +
                                " acuerdo a tus conocimientos, si no deseas realizar el exámen sigue tu camino desde un inicio.")
                        .setNegativeButton("Cerrar", (dialog, which) -> {
                            ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this.contexto, this.idCliente, Constants.CANCELADO_EXAMEN);
                            examen.execute();
                        })
                        .setPositiveButton("EVALUAR", ((dialog, which) -> {
                            Intent intent = new Intent(this.contexto, Diagnostico1.class);
                            this.contexto.startActivity(intent);
                        }))
                        .setOnCancelListener(dialog -> {

                        })
                        .show();
            }else if(estatus == Constants.ENCURSO_EXAMEN){
                ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this.contexto, this.idCliente, Constants.CONTINUAR_EXAMEN);
                examen.execute();
            }else if(estatus == Constants.CANCELADO_EXAMEN){
                RutaFragment.imgExamen.setVisibility(View.VISIBLE);
            }else if(estatus == Constants.INFO_EXAMEN_FINAL){
                int aciertos = jsonObject.getInt("aciertos");
                int puntuacionTotal = this.puntacionAsumar + aciertos;
                EvaluarRuta evaluarRuta = new EvaluarRuta(puntuacionTotal);
                FragmentRutaGenerada.ruta.setText(evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                FragmentRutaGenerada.nivel.setText("Nivel: " + evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                FragmentRutaGenerada.aciertos = puntuacionTotal;
            }else if(estatus == Constants.EXAMEN_GUARDADO){
                new SweetAlert(this.contexto, SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                        .setTitleText("Puntuación guardada.")
                        .setConfirmButton("Continuar", sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            if(!Diagnostico7.ultimaPagina){
                                Intent intent = new Intent(this.contexto, this.activitySiguiente);
                                activity.startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this.activity)
                                        .toBundle());
                                activity.finish();
                            }else{
                                Diagnostico7.ultimaPagina = false;
                            }
                        })
                        .show();
            }else if(estatus == Constants.CONTINUAR_EXAMEN){
                Intent intent;
                int pregunta = jsonObject.getInt("preguntaActual");
                if(pregunta == 10){
                    intent = new Intent(this.contexto, Diagnostico2.class);
                    this.contexto.startActivity(intent);
                }else if(pregunta == 20){
                    intent = new Intent(this.contexto, Diagnostico3.class);
                    this.contexto.startActivity(intent);
                }else if(pregunta == 30){
                    intent = new Intent(this.contexto, Diagnostico4.class);
                    this.contexto.startActivity(intent);
                }else if(pregunta == 40){
                    intent = new Intent(this.contexto, Diagnostico5.class);
                    this.contexto.startActivity(intent);
                }else if(pregunta == 50){
                    intent = new Intent(this.contexto, Diagnostico6.class);
                    this.contexto.startActivity(intent);
                }else if(pregunta == 60){
                    intent = new Intent(this.contexto, Diagnostico7.class);
                    this.contexto.startActivity(intent);
                }
            }else if(estatus == Constants.EXAMEN_FINALIZADO) {
                new SweetAlert(this.contexto, SweetAlert.NORMAL_TYPE, SweetAlert.CLIENTE)
                        .setTitleText("Ya tenemos tu diagnóstico.")
                        .show();
            }else if(estatus == Constants.REINICIAR_EXAMEN) {
                new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, SweetAlert.CLIENTE)
                        .setTitleText("¡GENIAL!")
                        .setContentText("Examen reiniciado, suerte.")
                        .show();
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

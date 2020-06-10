package com.proathome.controladores.estudiante;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.examen.Diagnostico1;
import com.proathome.examen.Diagnostico2;
import com.proathome.examen.Diagnostico3;
import com.proathome.examen.Diagnostico4;
import com.proathome.examen.Diagnostico5;
import com.proathome.examen.Diagnostico6;
import com.proathome.examen.Diagnostico7;
import com.proathome.examen.EvaluarRuta;
import com.proathome.fragments.FragmentRutaGenerada;
import com.proathome.utils.Constants;
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
import java.net.ProtocolException;
import java.net.URL;

public class ServicioExamenDiagnostico extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int estatus;
    public String respuesta;
    private String linkAPIestatus = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/estatusExamenDiagnostico/";
    private String linkAPIinicio_cancelar = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/examenDiagnostico";
    private String linkAPIenCurso = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/enCursoExamenDiagnostico";
    private String linkAPIinfoExamenFinal = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/infoExamenDiagnosticoFinal/";
    private String linkAPIinfoExamen = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/infoExamenDiagnostico/";
    private int idEstudiante;
    private int puntacionAsumar;
    private int preguntaActual;
    private ProgressDialog progressDialog;
    private Intent intent;

    public ServicioExamenDiagnostico(Context contexto, int idEstudiante, int estatus){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
        this.estatus = estatus;
    }

    public ServicioExamenDiagnostico(Context contexto, int idEstudiante, int estatus, int puntuacionAsumar){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
        this.estatus = estatus;
        this.puntacionAsumar = puntuacionAsumar;
    }

    public ServicioExamenDiagnostico(Context contexto, int idEstudiante, int estatus, int puntuacionAsumar, int preguntaActual){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
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
            String wsURL = this.linkAPIestatus + this.idEstudiante;
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
                cancelar.put("idEstudiante", this.idEstudiante);
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
            String wsURL = this.linkAPIinfoExamenFinal + this.idEstudiante;
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
                cancelar.put("idEstudiante", this.idEstudiante);
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
                cancelar.put("idEstudiante", this.idEstudiante);
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
        }else if(this.estatus == Constants.CONTINUAR_EXAMEN){
            String wsURL = this.linkAPIinfoExamen + this.idEstudiante;
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
                            ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this.contexto, this.idEstudiante, Constants.CANCELADO_EXAMEN);
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
                ServicioExamenDiagnostico examen = new ServicioExamenDiagnostico(this.contexto, this.idEstudiante, Constants.CONTINUAR_EXAMEN);
                examen.execute();
            }else if(estatus == Constants.CANCELADO_EXAMEN){
                Toast.makeText(this.contexto, "Exámen cancelado...", Toast.LENGTH_LONG).show();
            }else if(estatus == Constants.INFO_EXAMEN_FINAL){
                int aciertos = jsonObject.getInt("aciertos");
                int puntuacionTotal = this.puntacionAsumar + aciertos;
                Toast.makeText(this.contexto, "Puntuación Total: " + puntuacionTotal, Toast.LENGTH_LONG).show();
                EvaluarRuta evaluarRuta = new EvaluarRuta(puntuacionTotal);
                FragmentRutaGenerada.ruta.setText(evaluarRuta.getRutaString(evaluarRuta.getRuta()));
                FragmentRutaGenerada.nivel.setText("Nivel: " + evaluarRuta.getRutaString(evaluarRuta.getRuta()));
            }else if(estatus == Constants.EXAMEN_GUARDADO){
                Toast.makeText(this.contexto, "Puntuación guardada.", Toast.LENGTH_LONG).show();
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
            }

            //Toast.makeText(this.contexto, "Estado: " + jsonObject.getString("estatus"), Toast.LENGTH_LONG).show();

        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

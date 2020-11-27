package com.proathome.controladores.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.SweetAlert;
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
import java.net.URL;

public class ServicioTaskUpSesion extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String linkAPI, resultadoAPI, horario, lugar, tipo, observaciones, actualizado, fecha;
    private int idSesion, tiempo, idSeccion, idNivel, idBloque;
    private double latitud, longitud;
    private boolean cambioFecha;

    public ServicioTaskUpSesion(Context contexto, String linkAPI, int idSesion, String horario, String lugar, int tiempo, String tipo, String observaciones, double latitud, double longitud, String actualizado, int idSeccion, int idNivel, int idBloque, String fecha, boolean cambioFecha){
        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.idSesion = idSesion;
        this.horario = horario;
        this.lugar = lugar;
        this.tiempo = tiempo;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.latitud = latitud;
        this.longitud = longitud;
        this.actualizado = actualizado;
        this.idSeccion = idSeccion;
        this.idNivel = idNivel;
        this.idBloque = idBloque;
        this.fecha = fecha;
        this.cambioFecha = cambioFecha;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto,"Actualizando Sesión", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String result = null;
        String urlREST = this.linkAPI;

        try{
            URL url = new URL(urlREST);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            JSONObject jsonDatos = new JSONObject();
            jsonDatos.put("idSesion", this.idSesion);
            jsonDatos.put("horario", this.horario);
            jsonDatos.put("lugar", this.lugar);
            jsonDatos.put("tiempo", this.tiempo);
            jsonDatos.put("tipoClase", this.tipo);
            jsonDatos.put("observaciones", this.observaciones);
            jsonDatos.put("latitud", this.latitud);
            jsonDatos.put("longitud", this.longitud);
            jsonDatos.put("actualizado", this.actualizado);
            jsonDatos.put("fecha", this.fecha);
            jsonDatos.put("cambioFecha", this.cambioFecha);
            jsonDatos.put("idSeccion", this.idSeccion);
            jsonDatos.put("idNivel", this.idNivel);
            jsonDatos.put("idBloque", this.idBloque);

            //PARAMETROS DE CONEXIÓN.
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoInput(true);

            //OBTENER RESULTADO DEL REQUEST
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(getPostDataString(jsonDatos));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while ((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                result = stringBuffer.toString();

            }else{
                result = new String("Error: " + responseCode);
                this.resultadoAPI = result;
            }

        }catch (MalformedURLException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        this.resultadoAPI = s;
        if(this.resultadoAPI != null){
            new SweetAlert(SesionesFragment.contexto, SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡GENIAL!")
                    .setContentText(this.resultadoAPI)
                    .show();
        }else{
            new SweetAlert(SesionesFragment.contexto, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡ERROR!")
                    .setContentText("Error al actualizar la clase.")
                    .show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

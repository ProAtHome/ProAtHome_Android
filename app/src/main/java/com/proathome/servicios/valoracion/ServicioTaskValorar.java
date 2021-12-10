package com.proathome.servicios.valoracion;

import android.os.AsyncTask;
import com.proathome.ui.fragments.EvaluarFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskValorar extends AsyncTask<Void, Void, String> {

    private String linkValorarProfesional = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/valorarProfesional";
    private String linkValorarCliente = Constants.IP +
            "/ProAtHome/apiProAtHome/profesional/valorarCliente";
    private int idCliente, idProfesional, procedencia, idSesion;
    private float valoracion;
    private String comentario;

    public ServicioTaskValorar(int idCliente, int idProfesional, float valoracion, String comentario,
                               int procedencia, int idSesion){
        this.idCliente = idCliente;
        this.idProfesional = idProfesional;
        this.valoracion = valoracion;
        this.comentario = comentario;
        this.procedencia = procedencia;
        this.idSesion = idSesion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        if(this.procedencia == EvaluarFragment.PROCEDENCIA_CLIENTE){
            try{
                URL url = new URL(this.linkValorarProfesional);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                JSONObject jsonDatos = new JSONObject();
                jsonDatos.put("idCliente", this.idCliente);
                jsonDatos.put("idProfesional", this.idProfesional);
                jsonDatos.put("valoracion", this.valoracion);
                jsonDatos.put("comentario", this.comentario);
                jsonDatos.put("idSesion", this.idSesion);

                //PARAMETROS DE CONEXIÓN.
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
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

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    resultado = stringBuffer.toString();

                }else{
                    resultado = new String("Error: " + responseCode);
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
        }else if(this.procedencia == EvaluarFragment.PROCEDENCIA_PROFESIONAL){
            try{
                URL url = new URL(this.linkValorarCliente);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                JSONObject jsonDatos = new JSONObject();
                jsonDatos.put("idCliente", this.idCliente);
                jsonDatos.put("idProfesional", this.idProfesional);
                jsonDatos.put("valoracion", this.valoracion);
                jsonDatos.put("comentario", this.comentario);
                jsonDatos.put("idSesion", this.idSesion);

                //PARAMETROS DE CONEXIÓN.
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
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

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    resultado = stringBuffer.toString();

                }else{
                    resultado = new String("Error: " + responseCode);
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
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

package com.proathome.controladores.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
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

public class ServicioTaskEliminarSesion extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    private Context contexto;
    private String linkAPI, respuesta, resultadoAPI;
    private int idSesion;

    public ServicioTaskEliminarSesion(Context contexto, String linkAPI, int idSesion){

        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.idSesion = idSesion;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Eliminando Sesión", "Por favor, espere...");

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

            //PARAMETROS DE CONEXIÓN.
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            //OBTENER RESULTADO DEL REQUEST
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter(outputStream));
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
        Toast.makeText(this.contexto, this.resultadoAPI, Toast.LENGTH_LONG).show();

    }

    public String getPostDataString(JSONObject params) throws Exception {

        return params.toString();

    }

}

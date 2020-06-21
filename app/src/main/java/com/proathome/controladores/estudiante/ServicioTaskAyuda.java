package com.proathome.controladores.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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

public class ServicioTaskAyuda extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    private Context contexto;
    private int idCliente, tipo;
    private String respuesta, mensaje;
    private String linkAPIAyuda = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/admin/enviarMensaje";

    public ServicioTaskAyuda(Context contexto, int idCliente, String mensaje, int tipo){
        this.contexto = contexto;
        this.idCliente = idCliente;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Enviando comentario...", "Por favor, espere.");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
        String wsURL = this.linkAPIAyuda;
        try{
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject cancelar = new JSONObject();
            cancelar.put("idCliente", this.idCliente);
            if (this.tipo == Constants.TIPO_ESTUDIANTE)
                cancelar.put("tipoCliente", Constants.TIPO_ESTUDIANTE);
            else if (this.tipo == Constants.TIPO_PROFESOR)
                cancelar.put("tipoCliente", Constants.TIPO_PROFESOR);
            cancelar.put("mensaje", this.mensaje);

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

        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

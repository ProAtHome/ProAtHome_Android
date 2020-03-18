package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.FileUtils;

import com.proathome.ui.editarPerfil.EditarPerfilFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskUpFotoPerfil extends AsyncTask<Void, Void, String> {

    private File foto;
    private String linkAPI;
    private Context contexto;
    private ProgressDialog progressDialog;

    public ServicioTaskUpFotoPerfil(Context contexto, String linkAPI, File foto){

        this.contexto = contexto;
        this.foto = foto;
        this.linkAPI = linkAPI;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Actualizando Foto", "Por favor, espere..");

    }

    @Override
    protected String doInBackground(Void... voids) {

        String result = null;

        try {

            String urlREST = this.linkAPI;
            URL url = new URL(urlREST);
            String boundary =  "*****";
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            DataOutputStream request = new DataOutputStream(
                    urlConnection.getOutputStream());

            String crlf = "\r\n";
            String twoHyphens = "--";

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
            request.writeBytes(foto.getAbsolutePath() + "\r\n");

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + foto.getName() + "\"\r\n\r\n");
            request.writeBytes("\r\n");

            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();

            int responseCode=urlConnection.getResponseCode();
            if(responseCode== HttpURLConnection.HTTP_OK){

                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuffer sb= new StringBuffer("");
                String linea="";
                while ((linea=in.readLine())!= null){

                    sb.append(linea);
                    break;

                }

                in.close();
                result= sb.toString();

            }
            else{

                result= new String("Error: "+ responseCode);

            }

        } catch (MalformedURLException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
        progressDialog.dismiss();

    }

}

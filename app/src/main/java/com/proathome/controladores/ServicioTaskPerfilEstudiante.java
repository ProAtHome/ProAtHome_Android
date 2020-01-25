package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;
import com.proathome.ui.editarPerfil.EditarPerfilFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskPerfilEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    private String resultadoApi = "";
    private String linkrequestAPI;
    private String respuesta;
    private int idEstudiante;
    private Bitmap loadedImage;
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";

    public ServicioTaskPerfilEstudiante(Context ctx, String linkAPI, int idEstudiante){

        this.httpContext=ctx;
        this.idEstudiante = idEstudiante;
        this.linkrequestAPI=linkAPI + "/" + this.idEstudiante;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Cargando Perfil.", "Por favor, espere...");

    }

    @Override
    protected String doInBackground(Void... params) {

        String result= null;

        String wsURL = linkrequestAPI;
        URL url = null;
        try {

            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

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
                respuesta = result;

            }
            else{

                result= new String("Error: "+ responseCode);
                respuesta = null;

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        resultadoApi=s;

        if(resultadoApi == null){

            Toast.makeText(httpContext, "Error del servidor.", Toast.LENGTH_LONG).show();

        }else {

            if(!resultadoApi.equals("null")){

                try{

                    JSONObject jsonObject = new JSONObject(resultadoApi);
                    EditarPerfilFragment.etNombre.setText(jsonObject.getString("nombre"));
                    EditarPerfilFragment.etEdad.setText(jsonObject.getString("edad"));
                    EditarPerfilFragment.etDesc.setText(jsonObject.getString("descripcion"));
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                downloadFile(imageHttpAddress + jsonObject.getString("foto"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }catch(JSONException ex){

                    ex.printStackTrace();

                }

            }else{

                Toast.makeText(httpContext, "Error en el perfil.",Toast.LENGTH_LONG).show();

            }

        }

    }

    public void downloadFile(String imageHttpAddress) {

        URL imageUrl = null;
        try {
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
            EditarPerfilFragment.ivFoto.setImageBitmap(loadedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
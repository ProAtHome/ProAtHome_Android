package com.proathome.controladores.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.proathome.inicioEstudiante;
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
    private String resultadoApi = "", linkFoto;
    private String linkrequestAPI;
    private String respuesta;
    private int idEstudiante, tipo;
    private Bitmap loadedImage;

    public ServicioTaskPerfilEstudiante(Context ctx, String linkAPI, String linkFoto, int idEstudiante, int tipo){

        this.httpContext=ctx;
        this.idEstudiante = idEstudiante;
        this.linkrequestAPI=linkAPI + "/" + this.idEstudiante;
        this.tipo = tipo;
        this.linkFoto = linkFoto;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
                result= sb.toString();
                respuesta = result;

            }
            else{

                result= new String("Error: "+ responseCode);
                respuesta = null;

            }

            URL imageUrl = null;
            try {

                JSONObject json = new JSONObject(result);
                imageUrl = new URL(this.linkFoto + json.getString("foto"));
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                loadedImage = BitmapFactory.decodeStream(conn.getInputStream());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
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
        resultadoApi = s;

        if(resultadoApi == null){

            Toast.makeText(httpContext, "Error del servidor.", Toast.LENGTH_LONG).show();

        }else {

            if(!resultadoApi.equals("null")){

                try{

                    JSONObject jsonObject = new JSONObject(resultadoApi);

                    if(this.tipo == Constants.FOTO_EDITAR_PERFIL)
                        EditarPerfilFragment.ivFoto.setImageBitmap(loadedImage);
                    else if (this.tipo == Constants.FOTO_PERFIL)
                        inicioEstudiante.foto.setImageBitmap(loadedImage);

                    if(this.tipo == Constants.INFO_PERFIl_EDITAR){

                        EditarPerfilFragment.etNombre.setText(jsonObject.getString("nombre"));
                        EditarPerfilFragment.etEdad.setText(jsonObject.getString("edad"));
                        EditarPerfilFragment.etDesc.setText(jsonObject.getString("descripcion"));

                    }else if(this.tipo == Constants.INFO_PERFIL){

                        inicioEstudiante.nombreTV.setText(jsonObject.getString("nombre"));
                        inicioEstudiante.correoTV.setText(jsonObject.getString("correo"));

                    }



                }catch(JSONException ex){

                    ex.printStackTrace();

                }

            }else{

                Toast.makeText(httpContext, "Error en el perfil.",Toast.LENGTH_LONG).show();

            }

        }

    }

}
package com.proathome.servicios.profesor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.inicioProfesor;
import com.proathome.ui.editarPerfilProfesor.EditarPerfilProfesorFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskPerfilProfesor extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private String resultadoApi = "", linkFoto;
    private String linkrequestAPI;
    private String respuesta;
    private int idProfesor, tipo;
    private Bitmap loadedImage;

    public ServicioTaskPerfilProfesor(Context ctx, String linkAPI, String linkFoto, int idProfesor, int tipo){
        this.httpContext=ctx;
        this.idProfesor = idProfesor;
        this.linkrequestAPI=linkAPI + "/" + this.idProfesor;
        this.tipo = tipo;
        this.linkFoto = linkFoto;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;
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

        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        resultadoApi = s;

        if(resultadoApi == null){
            errorMsg("Error del servidor, intente ingresar más tarde.");
        }else {
            if(!resultadoApi.equals("null")){
                try{
                    JSONObject jsonObject = new JSONObject(resultadoApi);

                    if(this.tipo == Constants.FOTO_EDITAR_PERFIL)
                        EditarPerfilProfesorFragment.ivFoto.setImageBitmap(loadedImage);
                    else if (this.tipo == Constants.FOTO_PERFIL)
                        inicioProfesor.foto.setImageBitmap(loadedImage);

                    if(this.tipo == Constants.INFO_PERFIl_EDITAR){
                        EditarPerfilProfesorFragment.etNombre.setText(jsonObject.getString("nombre"));
                        EditarPerfilProfesorFragment.etEdad.setText(jsonObject.getString("edad"));
                        EditarPerfilProfesorFragment.etDesc.setText(jsonObject.getString("descripcion"));
                    }else if(this.tipo == Constants.INFO_PERFIL){
                        inicioProfesor.nombreTV.setText(jsonObject.getString("nombre"));
                        inicioProfesor.correoTV.setText(jsonObject.getString("correo"));
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else{
                errorMsg("Error en el perfil, intente ingresar más tarde.");
            }
        }
    }

    public void errorMsg(String mensaje){
        new SweetAlert(this.httpContext, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

}

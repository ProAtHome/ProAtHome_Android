package com.proathome.servicios.profesional;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.InicioProfesional;
import com.proathome.ui.editarPerfilProfesional.EditarPerfilProfesionalFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskPerfilProfesional extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private String resultadoApi = "", linkFoto;
    private String linkrequestAPI;
    private String respuesta;
    private int idProfesional, tipo;
    private Bitmap loadedImage;

    public ServicioTaskPerfilProfesional(Context ctx, String linkAPI, String linkFoto, int idProfesional, int tipo){
        this.httpContext=ctx;
        this.idProfesional = idProfesional;
        this.linkrequestAPI=linkAPI + "/" + this.idProfesional;
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
                        EditarPerfilProfesionalFragment.ivFoto.setImageBitmap(loadedImage);
                    else if (this.tipo == Constants.FOTO_PERFIL)
                        InicioProfesional.foto.setImageBitmap(loadedImage);

                    if(this.tipo == Constants.INFO_PERFIl_EDITAR){
                        EditarPerfilProfesionalFragment.tvNombre.setText("Nombre: " + jsonObject.getString("nombre"));
                        EditarPerfilProfesionalFragment.tvCorreo.setText("Correo: " + jsonObject.getString("correo"));
                        EditarPerfilProfesionalFragment.etCelular.setText(jsonObject.getString("celular"));
                        EditarPerfilProfesionalFragment.etTelefono.setText(jsonObject.getString("telefonoLocal"));
                        EditarPerfilProfesionalFragment.etDireccion.setText(jsonObject.getString("direccion"));
                        EditarPerfilProfesionalFragment.etDesc.setText(jsonObject.getString("descripcion"));
                    }else if(this.tipo == Constants.INFO_PERFIL){
                        InicioProfesional.nombreTV.setText(jsonObject.getString("nombre"));
                        InicioProfesional.correoTV.setText(jsonObject.getString("correo"));
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
        new SweetAlert(this.httpContext, SweetAlert.ERROR_TYPE, SweetAlert.CLIENTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

}

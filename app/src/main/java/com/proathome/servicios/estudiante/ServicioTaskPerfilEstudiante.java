package com.proathome.servicios.estudiante;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.servicios.planes.ServicioTaskValidarPlan;
import com.proathome.fragments.PlanesFragment;
import com.proathome.inicioEstudiante;
import com.proathome.ui.editarPerfil.EditarPerfilFragment;
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

public class ServicioTaskPerfilEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private String resultadoApi, linkFoto, linkrequestAPI, respuesta;
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
        System.out.println(s);
        if(resultadoApi == null){
            errorMsg("Error del servidor, intente ingresar más tarde.");
        }else {
            if(!resultadoApi.equals("null")){
                try{
                    JSONObject jsonObject = new JSONObject(resultadoApi);

                    if(this.tipo == Constants.FOTO_EDITAR_PERFIL)
                        EditarPerfilFragment.ivFoto.setImageBitmap(loadedImage);
                    else if (this.tipo == Constants.FOTO_PERFIL)
                        inicioEstudiante.foto.setImageBitmap(loadedImage);

                    if(this.tipo == Constants.INFO_PERFIl_EDITAR){

                        EditarPerfilFragment.tvNombre.setText("Nombre: " + jsonObject.getString("nombre"));
                        EditarPerfilFragment.tvCorreo.setText("Correo: " + jsonObject.getString("correo"));
                        EditarPerfilFragment.etCelular.setText(jsonObject.getString("celular"));
                        EditarPerfilFragment.etTelefono.setText(jsonObject.getString("telefonoLocal"));
                        EditarPerfilFragment.etDireccion.setText(jsonObject.getString("direccion"));
                        EditarPerfilFragment.etDesc.setText(jsonObject.getString("descripcion"));

                    }else if(this.tipo == Constants.INFO_PERFIL){

                        inicioEstudiante.nombreTV.setText(jsonObject.getString("nombre"));
                        inicioEstudiante.correoTV.setText(jsonObject.getString("correo"));
                        //inicioEstudiante.tipoPlan.setText("PLAN ACTUAL: " + jsonObject.getString("tipoPlan"));
                        //inicioEstudiante.monedero.setText("HORAS DISPONIBLES:                      " + obtenerHorario(jsonObject.getInt("monedero")));
                        PlanesFragment.nombreEstudiante = jsonObject.getString("nombre");
                        PlanesFragment.correoEstudiante = jsonObject.getString("correo");
                        /*TODO FLUJO_EJECUTAR_PLAN: Verificar si hay PLAN distinto a PARTICULAR
                            -Si, entonces, verificar la expiracion y el monedero (En el servidor verificamos el monedero y  la expiración,
                                y ahí decidimos si finalizamos el plan activo, en cualquier caso regresamos un mensaje validando o avisando que valió verga.
                            -No, entonces, todo sigue el flujo.*/
                        ServicioTaskValidarPlan validarPlan = new ServicioTaskValidarPlan(this.httpContext, this.idEstudiante);
                        validarPlan.execute();
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

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

}
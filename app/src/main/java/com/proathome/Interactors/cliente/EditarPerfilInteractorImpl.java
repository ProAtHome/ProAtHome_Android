package com.proathome.Interactors.cliente;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilInteractor;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class EditarPerfilInteractorImpl implements EditarPerfilInteractor {

    private EditarPerfilPresenter editarPerfilPresenter;
    private Bitmap bitmap;
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    private String linkFoto = Constants.IP_80 + "/assets/lib/ActualizarFotoAndroid.php";

    public EditarPerfilInteractorImpl(EditarPerfilPresenter editarPerfilPresenter){
        this.editarPerfilPresenter = editarPerfilPresenter;
    }

    @Override
    public void getReportes(int idCliente, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                        if(mensaje.getInt("reportes") == 0){
                            //ocultamos avisos
                            editarPerfilPresenter.setVisibilityReportes(false, "");
                        }else if(mensaje.getInt("reportes") > 0){
                            int numReportes = mensaje.getInt("reportes");
                            String descripcion = mensaje.getString("aviso");
                            //mostramos aviso
                            editarPerfilPresenter.setVisibilityReportes(true, "Aviso No. " + numReportes + ": " + descripcion);
                        }
                    }else
                        editarPerfilPresenter.showError("Error al obtener reportes.");
                }catch(JSONException ex){
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_REPORTES_CLIENTE + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getDatosPerfil(int idCliente, String token) {
        editarPerfilPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if(!response.equals("null")){
                    JSONObject jsonObject = new JSONObject(response);
                    editarPerfilPresenter.setDatosPerfil(jsonObject);
                }else
                    editarPerfilPresenter.showError("Error en el perfil, intente ingresar más tarde.");
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_PERFIL_CLIENTE + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getDatosBanco(int idCliente, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                        if(mensaje.getBoolean("existe"))
                            editarPerfilPresenter.setDatosBanco(mensaje);
                        else
                            editarPerfilPresenter.showErrorBanco("No tienes datos bancarios registrados");
                    }else
                        editarPerfilPresenter.showError(jsonObject.get("mensaje").toString());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_DATOS_BANCO_CLIENTE + idCliente + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void updateCuentaCliente(JSONObject parametrosPUT) {
        editarPerfilPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            editarPerfilPresenter.hideProgress();
            if(response != null){
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta"))
                    editarPerfilPresenter.successUpdate("Datos actualizados correctamente.");
                else
                    editarPerfilPresenter.showError(jsonObject.getString("mensaje"));
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.UPDATE_CUENTA_CLIENTE, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();
    }

    @Override
    public void updatePerfil(JSONObject parametros, Context context, int idCliente) {
        editarPerfilPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            editarPerfilPresenter.hideProgress();
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta"))
                        editarPerfilPresenter.successUpdate(jsonObject.getString("mensaje"));
                    else
                        editarPerfilPresenter.showError(jsonObject.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.ACTUALIZAR_PERFIL, WebServicesAPI.PUT, parametros);
        webServicesAPI.execute();

        uploadImage(context, idCliente);
    }

    @Override
    public void getBitmapMedia(Intent data, ContentResolver contentResolver) {
        Uri filePath = data.getData();
        try {
            //Cómo obtener el mapa de bits de la Galería
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath);
            //Configuración del mapa de bits en ImageView
            editarPerfilPresenter.setFotoBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }
    }

    @Override
    public void getFotoPerfil(String foto) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            editarPerfilPresenter.hideProgress();
            if(response != null)
                editarPerfilPresenter.setFotoBitmap(response);
            else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

    private void uploadImage(Context context, int idCliente){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, linkFoto,
                response -> {
                },
                error -> {
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);
                //Obtener el nombre de la imagen
                String nombre = idCliente + "_perfil";
                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
                params.put("idCliente", String.valueOf(idCliente));
                //Parámetros de retorno
                return params;
            }
        };
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}

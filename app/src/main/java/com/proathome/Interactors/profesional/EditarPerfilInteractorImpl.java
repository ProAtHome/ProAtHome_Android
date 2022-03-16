package com.proathome.Interactors.profesional;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilInteractor;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
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

    public EditarPerfilInteractorImpl(EditarPerfilPresenter editarPerfilPresenter){
        this.editarPerfilPresenter = editarPerfilPresenter;
    }

    @Override
    public void getReportes(int idProfesional, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                        if(mensaje.getInt("reportes") == 0){
                            //ocultamos avisos
                            editarPerfilPresenter.setVisibilityAvisos(false, null);
                        }else if(mensaje.getInt("reportes") > 0){
                            int numReportes = mensaje.getInt("reportes");
                            String descripcion = mensaje.getString("aviso");
                            //mostramos aviso
                            editarPerfilPresenter.setVisibilityAvisos(true, "Aviso No. " + numReportes + ": " + descripcion);
                        }
                    }else
                        editarPerfilPresenter.showError( "Error al obtener Reportes.");
                }catch(JSONException ex){
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_REPORTES_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getDatosPerfil(int idProfesional, String token) {
        editarPerfilPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if (!response.equals("null")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        editarPerfilPresenter.setDatosPerfil(jsonObject);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                    }
                } else
                    editarPerfilPresenter.showError("Error en el perfil, intente ingresar más tarde.");
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_PERFIL_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET,  null);
        webServicesAPI.execute();
    }

    @Override
    public void getDatosBanco(int idProfesional, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject jsonDatos = jsonObject.getJSONObject("mensaje");
                        if(jsonDatos.getBoolean("existe"))
                            editarPerfilPresenter.setDatosBanco(jsonDatos);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.DATOS_BANCARIOS_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
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

    @Override
    public void actualizarPerfil(JSONObject jsonObject) {
        editarPerfilPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            editarPerfilPresenter.hideProgress();
            if(response != null){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    if(jsonResponse.getBoolean("respuesta"))
                        editarPerfilPresenter.showSuccess(jsonResponse.getString("mensaje"));
                    else
                        editarPerfilPresenter.showError(jsonResponse.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.ACTUALIZAR_PERFIL_PROFESIONAL, WebServicesAPI.PUT, jsonObject);
        webServicesAPI.execute();
    }

    @Override
    public void uploadImage(int idProfesional, Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIEndPoints.UP_FOTO_PROFESIONAL,
                response -> {
                },
                error -> {
                }){
            @Override
            protected Map<String, String> getParams() {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);
                //Obtener el nombre de la imagen
                String nombre = idProfesional + "_perfilProfesional";
                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
                params.put("idProfesional", String.valueOf(idProfesional));
                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    @Override
    public void getMediaGalery(ContentResolver contentResolver, Intent data) {
        Uri filePath = data.getData();
        try {
            //Cómo obtener el mapa de bits de la Galería
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath);
            //Configuración del mapa de bits en ImageView
            editarPerfilPresenter.setFotoBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBancoService(JSONObject parametrosPUT) {
        editarPerfilPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            editarPerfilPresenter.hideProgress();
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta"))
                        editarPerfilPresenter.showSuccess(jsonObject.getString("mensaje"));
                    else
                        editarPerfilPresenter.showError(jsonObject.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }
            }else
                editarPerfilPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.UPDATE_CUENTA_PROFESIONAL, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}

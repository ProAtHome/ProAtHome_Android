package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.Registrarse.RegistrarseInteractor;
import com.proathome.Interfaces.cliente.Registrarse.RegistrarsePresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarseInteractorImpl implements RegistrarseInteractor {

    private RegistrarsePresenter registrarsePresenter;

    public RegistrarseInteractorImpl(RegistrarsePresenter registrarsePresenter){
        this.registrarsePresenter = registrarsePresenter;
    }

    @Override
    public void registrar(String nombre, String paterno, String materno, String correo, String celular, String telefono, String direccion, String fecha, String genero, String pass) {
        JSONObject parametrosPost= new JSONObject();
        try{
            parametrosPost.put("nombre", nombre);
            parametrosPost.put("paterno", paterno);
            parametrosPost.put("materno", materno);
            parametrosPost.put("correo", correo);
            parametrosPost.put("celular", celular);
            parametrosPost.put("telefono", telefono);
            parametrosPost.put("direccion", direccion);
            parametrosPost.put("fechaNacimiento", fecha);
            parametrosPost.put("genero", genero);
            parametrosPost.put("contrasena", pass);

            registrarsePresenter.showProgress();

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject post = new JSONObject();
                        post.put("token", jsonObject.getString("token"));
                        post.put("correo", correo);
                        WebServicesAPI servicesCorreo = new WebServicesAPI(responseVerficacion -> {
                            registrarsePresenter.hideProgress();
                            registrarsePresenter.success(jsonObject.getString("mensaje"));
                        }, APIEndPoints.VERIFICACION_CORREO, WebServicesAPI.POST, post);
                        servicesCorreo.execute();
                    }else{
                        registrarsePresenter.hideProgress();
                        registrarsePresenter.showError(jsonObject.getString("mensaje"));
                    }
                }else
                    registrarsePresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }, APIEndPoints.REGISTRAR_CLIENTE, WebServicesAPI.POST, parametrosPost);
            webServicesAPI.execute();
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

}

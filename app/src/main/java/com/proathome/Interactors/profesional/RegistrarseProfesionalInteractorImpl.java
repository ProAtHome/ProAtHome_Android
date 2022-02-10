package com.proathome.Interactors.profesional;

import com.proathome.Interfaces.profesional.Registrarse.RegistrarseProfesionalInteractor;
import com.proathome.Interfaces.profesional.Registrarse.RegistrarseProfesionalPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarseProfesionalInteractorImpl implements RegistrarseProfesionalInteractor {

    private RegistrarseProfesionalPresenter registrarseProfesionalPresenter;

    public RegistrarseProfesionalInteractorImpl(RegistrarseProfesionalPresenter registrarseProfesionalPresenter){
        this.registrarseProfesionalPresenter = registrarseProfesionalPresenter;
    }

    @Override
    public void registrar(String nombre, String paterno, String materno, String fecha, String celular, String telefono, String direccion, String correo, String pass, String genero) {
        JSONObject parametrosPost= new JSONObject();
        try {
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

            registrarseProfesionalPresenter.showProgress();
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("respuesta")){
                            JSONObject post = new JSONObject();
                            post.put("token", jsonObject.getString("token"));
                            post.put("correo", correo);
                            WebServicesAPI fastServices = new WebServicesAPI(output -> {
                                registrarseProfesionalPresenter.hideProgress();
                                registrarseProfesionalPresenter.toLogin(jsonObject.getString("mensaje"));
                            }, Constants.IP_80 + "/assets/lib/Verificacion.php?enviarPro=true", WebServicesAPI.POST, post);
                            fastServices.execute();
                        }else{
                            registrarseProfesionalPresenter.hideProgress();
                            registrarseProfesionalPresenter.showError(jsonObject.getString("mensaje"));
                        }

                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    registrarseProfesionalPresenter.showError("Ocurrio un error, intenta de nuevo mas tarde.");
            }, APIEndPoints.REGISTRAR_PROFESIONAL, WebServicesAPI.POST, parametrosPost);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package com.proathome.Interactors.profesional.inicio;

import com.proathome.Interfaces.profesional.Inicio.InicioInteractor;
import com.proathome.Interfaces.profesional.Inicio.InicioPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioInteractorImpl implements InicioInteractor {

    private InicioPresenter inicioPresenter;

    public InicioInteractorImpl(InicioPresenter inicioPresenter){
        this.inicioPresenter = inicioPresenter;
    }

    @Override
    public void validarTokenSesion(int idProfesional, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            JSONObject data = new JSONObject(response);
            if(data.getBoolean("respuesta"))
                cargarPerfil(idProfesional, token);
            else
                inicioPresenter.sesionExpirada();
        }, APIEndPoints.VALIDAR_TOKEN_SESION_PROFESIONAL + idProfesional + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void cargarPerfil(int idProfesional, String token){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if (!response.equals("null")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        inicioPresenter.setInfoPerfil(jsonObject);
                        setImageBitmap(jsonObject.getString("foto"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                        inicioPresenter.showError("Ocurrio un error, intente ingresar mas tarde.");
                    }
                } else
                    inicioPresenter.showError("Error en el perfil, intente ingresar más tarde.");
            }else
                inicioPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_PERFIL_PROFESIONAL + idProfesional+ "/" + token, WebServicesAPI.GET,  null);
        webServicesAPI.execute();
    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            if(response != null)
                inicioPresenter.setFoto(response);
            else
                inicioPresenter.showError("Error la cargar la foto de perfil");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

}

package com.proathome.Interactors.profesional;

import com.proathome.Interfaces.profesional.Login.LoginProfesionalInteractor;
import com.proathome.Interfaces.profesional.Login.LoginProfesionalPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginProfesionalInteractorImpl implements LoginProfesionalInteractor {

    private LoginProfesionalPresenter loginProfesionalPresenter;

    public LoginProfesionalInteractorImpl(LoginProfesionalPresenter loginProfesionalPresenter){
        this.loginProfesionalPresenter = loginProfesionalPresenter;
    }

    @Override
    public void login(String correo, String pass) {
        loginProfesionalPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            loginProfesionalPresenter.hideProgress();
            try{
                if(response == null){
                    loginProfesionalPresenter.showError("Ocurrió un error inesperado, intenta de nuevo.");
                }else {
                    if(!response.equals("null")){
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")) {
                            if(jsonObject.getBoolean("verificado"))
                                loginProfesionalPresenter.toInicio(jsonObject, correo);
                            else
                                loginProfesionalPresenter.showError("Aún no verificas tu cuenta de correo electrónico.");
                        }else if(jsonObject.getString("estado").equalsIgnoreCase("documentacion") ||
                                jsonObject.getString("estado").equalsIgnoreCase("cita") ||
                                jsonObject.getString("estado").equalsIgnoreCase("registro")){
                            loginProfesionalPresenter.toActivarCuenta();
                        }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO"))
                            loginProfesionalPresenter.toBloquearPerfil();
                    }else
                        loginProfesionalPresenter.showError("Usuario no registrado o tus datos están incorrectos.");
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.INICIAR_SESION_PROFESIONAL + correo + "/" + pass, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}

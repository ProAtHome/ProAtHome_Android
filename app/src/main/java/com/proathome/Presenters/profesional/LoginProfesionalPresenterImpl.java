package com.proathome.Presenters.profesional;

import com.proathome.Interactors.profesional.LoginProfesionalInteractorImpl;
import com.proathome.Interfaces.profesional.Login.LoginProfesionalInteractor;
import com.proathome.Interfaces.profesional.Login.LoginProfesionalPresenter;
import com.proathome.Interfaces.profesional.Login.LoginProfesionalView;
import org.json.JSONObject;

public class LoginProfesionalPresenterImpl implements LoginProfesionalPresenter {

    private LoginProfesionalView loginProfesionalView;
    private LoginProfesionalInteractor loginProfesionalInteractor;

    public LoginProfesionalPresenterImpl(LoginProfesionalView loginProfesionalView){
        this.loginProfesionalView = loginProfesionalView;
        loginProfesionalInteractor = new LoginProfesionalInteractorImpl(this);
    }

    @Override
    public void login(String correo, String pass) {
        loginProfesionalInteractor.login(correo, pass);
    }

    @Override
    public void showProgress() {
        if(loginProfesionalView != null)
            loginProfesionalView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(loginProfesionalView != null)
            loginProfesionalView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(loginProfesionalView != null)
            loginProfesionalView.showErrorLogin(error);
    }

    @Override
    public void toInicio(JSONObject jsonObject, String correo) {
        if(loginProfesionalView != null)
            loginProfesionalView.toInicio(jsonObject, correo);
    }

    @Override
    public void toActivarCuenta() {
        if(loginProfesionalView != null)
            loginProfesionalView.toActivarCuenta();
    }

    @Override
    public void toBloquearPerfil() {
        if(loginProfesionalView != null)
            loginProfesionalView.toBloquearPerfil();
    }

}

package com.proathome.Presenters.activitys_compartidos;

import com.proathome.Interactors.activitys_compartidos.EmailPasswordInteractorImpl;
import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordInteractor;
import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordPresenter;
import com.proathome.Interfaces.activitys_compartidos.EmailPassword.EmailPasswordView;

public class EmailPasswordPresenterImpl implements EmailPasswordPresenter {

    private EmailPasswordView emailPasswordView;
    private EmailPasswordInteractor emailPasswordInteractor;

    public EmailPasswordPresenterImpl(EmailPasswordView emailPasswordView){
        this.emailPasswordView = emailPasswordView;
        emailPasswordInteractor = new EmailPasswordInteractorImpl(this);
    }

    @Override
    public void enviarCodigo(String correo, int tipoPerfil) {
        emailPasswordInteractor.enviarCodigo(correo, tipoPerfil);
    }

    @Override
    public void showProgress() {
        if(emailPasswordView != null)
            emailPasswordView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(emailPasswordView != null)
            emailPasswordView.hideProgress();
    }

    @Override
    public void successEmail(String token, String correo, String mensaje) {
        if(emailPasswordView != null)
            emailPasswordView.successEmail(token, correo, mensaje);
    }

    @Override
    public void showError(String error) {
        if(emailPasswordView != null)
            emailPasswordView.showError(error);
    }

}

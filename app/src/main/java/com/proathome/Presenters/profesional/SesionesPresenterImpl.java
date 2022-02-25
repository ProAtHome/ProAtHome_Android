package com.proathome.Presenters.profesional;

import com.proathome.Interactors.profesional.SesionesInteractorImpl;
import com.proathome.Interfaces.profesional.Sesiones.SesionesInteractor;
import com.proathome.Interfaces.profesional.Sesiones.SesionesPresenter;
import com.proathome.Interfaces.profesional.Sesiones.SesionesView;
import org.json.JSONObject;

public class SesionesPresenterImpl implements SesionesPresenter {

    private SesionesView sesionesView;
    private SesionesInteractor sesionesInteractor;

    public SesionesPresenterImpl(SesionesView sesionesView){
        this.sesionesView = sesionesView;
        sesionesInteractor = new SesionesInteractorImpl(this);
    }

    @Override
    public void getSesiones(int idProfesional, String token) {
        sesionesInteractor.getSesiones(idProfesional, token);
    }

    @Override
    public void showError(String error) {
        if(sesionesView != null)
            sesionesView.showError(error);
    }

    @Override
    public void showProgress() {
        if(sesionesView != null)
            sesionesView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(sesionesView != null)
            sesionesView.hideProgress();
    }

    @Override
    public void setVisibilityLottie(int visibilityLottie) {
        if(sesionesView != null)
            sesionesView.setVisibilityLottie(visibilityLottie);
    }

    @Override
    public void setAdapterSesiones(JSONObject object) {
        if(sesionesView != null)
            sesionesView.setAdapterSesiones(object);
    }

}

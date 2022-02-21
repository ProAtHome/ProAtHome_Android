package com.proathome.Presenters.profesional.inicio;

import com.proathome.Interactors.profesional.inicio.InicioFragmentInteractorImpl;
import com.proathome.Interfaces.profesional.Inicio.InicioFragmentInteractor;
import com.proathome.Interfaces.profesional.Inicio.InicioFragmentPresenter;
import com.proathome.Interfaces.profesional.Inicio.InicioFragmentView;
import org.json.JSONObject;

public class InicioFragmentPresenterImpl implements InicioFragmentPresenter {

    private InicioFragmentView inicioFragmentView;
    private InicioFragmentInteractor inicioFragmentInteractor;

    public InicioFragmentPresenterImpl(InicioFragmentView inicioFragmentView){
        this.inicioFragmentView = inicioFragmentView;
        inicioFragmentInteractor = new InicioFragmentInteractorImpl(this);
    }

    @Override
    public void getSesiones(int idProfesional, String token) {
        inicioFragmentInteractor.getSesiones(idProfesional, token);
    }

    @Override
    public void showProgress() {
        if(inicioFragmentView != null)
            inicioFragmentView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(inicioFragmentView != null)
            inicioFragmentView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(inicioFragmentView != null)
            inicioFragmentView.showError(error);
    }

    @Override
    public void setLottieVisible() {
        if(inicioFragmentView != null)
            inicioFragmentView.setLottieVisible();
    }

    @Override
    public void setAdapterSesiones(JSONObject object) {
        if(inicioFragmentView != null)
            inicioFragmentView.setAdapterSesiones(object);
    }

}

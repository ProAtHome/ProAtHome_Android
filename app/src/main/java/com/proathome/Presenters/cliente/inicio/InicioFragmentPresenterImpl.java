package com.proathome.Presenters.cliente.inicio;

import android.content.Context;
import com.proathome.Interactors.cliente.inicio.InicioFragmentInteractorImpl;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentInteractor;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentPresenter;
import com.proathome.Interfaces.cliente.Inicio.InicioFragmentView;
import org.json.JSONObject;

public class InicioFragmentPresenterImpl implements InicioFragmentPresenter {

    private InicioFragmentView inicioFragmentView;
    private InicioFragmentInteractor inicioFragmentInteractor;

    public InicioFragmentPresenterImpl(InicioFragmentView inicioFragmentView){
        this.inicioFragmentView = inicioFragmentView;
        inicioFragmentInteractor = new InicioFragmentInteractorImpl(this);
    }

    @Override
    public void getSesiones(int idCliente, Context context) {
        inicioFragmentInteractor.getSesiones(idCliente, context);
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
    public void setAdapter(JSONObject object) {
        if(inicioFragmentView != null)
            inicioFragmentView.setAdapter(object);
    }

}

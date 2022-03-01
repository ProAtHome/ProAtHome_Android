package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.MasTiempoInteractorImpl;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoInteractor;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoPresenter;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoView;

public class MasTiempoPresenterImpl implements MasTiempoPresenter {

    private MasTiempoView masTiempoView;
    private MasTiempoInteractor masTiempoInteractor;

    public MasTiempoPresenterImpl(MasTiempoView masTiempoView){
        this.masTiempoView = masTiempoView;
        masTiempoInteractor = new MasTiempoInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(masTiempoView != null)
            masTiempoView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(masTiempoView != null)
            masTiempoView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(masTiempoView != null)
            masTiempoView.showError(error);
    }

    @Override
    public void getPreOrden(int idCliente, int idSesion, String token) {
        masTiempoInteractor.getPreOrden(idCliente, idSesion, token);
    }

    @Override
    public void finalizar(int idCliente, int idSesion) {
        masTiempoInteractor.finalizar(idCliente, idSesion);
    }

    @Override
    public void cerrarFragment() {
        if(masTiempoView != null)
            masTiempoView.cerrarFragment();
    }

}

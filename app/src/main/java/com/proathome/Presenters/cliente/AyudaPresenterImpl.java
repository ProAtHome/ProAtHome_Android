package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.AyudaInteractorImpl;
import com.proathome.Interfaces.cliente.Ayuda.AyudaInteractor;
import com.proathome.Interfaces.cliente.Ayuda.AyudaPresenter;
import com.proathome.Interfaces.cliente.Ayuda.AyudaView;
import org.json.JSONObject;

public class AyudaPresenterImpl implements AyudaPresenter {

    private AyudaView ayudaView;
    private AyudaInteractor ayudaInteractor;

    public AyudaPresenterImpl(AyudaView ayudaView){
        this.ayudaView = ayudaView;
        ayudaInteractor = new AyudaInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(ayudaView != null)
            ayudaView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(ayudaView != null)
            ayudaView.hideProgress();
    }

    @Override
    public void obtenerTickets(int idCliente, String token) {
        ayudaInteractor.obtenerTickets(idCliente, token);
    }

    @Override
    public void showError(String error) {
        if(ayudaView != null)
            ayudaView.showError(error);
    }

    @Override
    public void setVisibilityLottie(int visibilityLottie) {
        if(ayudaView != null)
            ayudaView.setVisibilityLottie(visibilityLottie);
    }

    @Override
    public void setAdapterTickets(JSONObject jsonObject) {
        if(ayudaView != null)
            ayudaView.setAdapterTickets(jsonObject);
    }

}

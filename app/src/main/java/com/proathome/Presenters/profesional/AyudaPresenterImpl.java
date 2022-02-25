package com.proathome.Presenters.profesional;

import com.proathome.Interactors.profesional.AyudaInteractorImpl;
import com.proathome.Interfaces.profesional.Ayuda.AyudaInteractor;
import com.proathome.Interfaces.profesional.Ayuda.AyudaPresenter;
import com.proathome.Interfaces.profesional.Ayuda.AyudaView;
import org.json.JSONObject;

public class AyudaPresenterImpl implements AyudaPresenter {

    private AyudaView ayudaView;
    private AyudaInteractor ayudaInteractor;

    public AyudaPresenterImpl(AyudaView ayudaView){
        this.ayudaView = ayudaView;
        ayudaInteractor = new AyudaInteractorImpl(this);
    }

    @Override
    public void obtenerTickets(int idProfesional, String token) {
        ayudaInteractor.obtenerTickets(idProfesional, token);
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

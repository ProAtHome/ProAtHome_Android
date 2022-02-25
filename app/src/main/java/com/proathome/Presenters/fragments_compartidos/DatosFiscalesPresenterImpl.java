package com.proathome.Presenters.fragments_compartidos;

import android.content.Context;
import com.proathome.Interactors.fragments_compartidos.DatosFiscalesInteractorImpl;
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesInteractor;
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesPresenter;
import com.proathome.Interfaces.fragments_compartidos.DatosFiscales.DatosFiscalesView;
import org.json.JSONObject;

public class DatosFiscalesPresenterImpl implements DatosFiscalesPresenter {

    private DatosFiscalesView datosFiscalesView;
    private DatosFiscalesInteractor datosFiscalesInteractor;

    public DatosFiscalesPresenterImpl(DatosFiscalesView datosFiscalesView){
        this.datosFiscalesView = datosFiscalesView;
        datosFiscalesInteractor = new DatosFiscalesInteractorImpl(this);
    }

    @Override
    public void getDatosFiscales(int tipoPerfil, int idUsuario, Context context) {
        datosFiscalesInteractor.getDatosFiscales(tipoPerfil, idUsuario, context);
    }

    @Override
    public void showProgress() {
        if(datosFiscalesView != null)
            datosFiscalesView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(datosFiscalesView != null)
            datosFiscalesView.hideProgress();
    }

    @Override
    public void showError(String error) {
        if(datosFiscalesView != null)
            datosFiscalesView.showError(error);
    }

    @Override
    public void setInfoDatos(JSONObject datos) {
        if(datosFiscalesView != null)
            datosFiscalesView.setInfoDatos(datos);
    }

    @Override
    public void upDatosFiscales(int tipoPerfil, int idUsuario, String tipoPersona, String razonSocial, String rfc, String cfdi) {
        datosFiscalesInteractor.upDatosFiscales(tipoPerfil, idUsuario, tipoPersona, razonSocial, rfc, cfdi);
    }

    @Override
    public void updateSuccess(String mensaje) {
        if(datosFiscalesView != null)
            datosFiscalesView.updateSuccess(mensaje);
    }

}

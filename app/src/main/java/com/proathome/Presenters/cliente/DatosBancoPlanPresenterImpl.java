package com.proathome.Presenters.cliente;

import android.content.Context;
import com.proathome.Interactors.cliente.DatosBancoPlanInteractorImpl;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanInteractor;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanPresenter;
import com.proathome.Interfaces.cliente.DatosBancoPlan.DatosBancoPlanView;
import org.json.JSONObject;

public class DatosBancoPlanPresenterImpl implements DatosBancoPlanPresenter {

    private DatosBancoPlanView datosBancoPlanView;
    private DatosBancoPlanInteractor datosBancoPlanInteractor;

    public DatosBancoPlanPresenterImpl(DatosBancoPlanView datosBancoPlanView){
        this.datosBancoPlanView = datosBancoPlanView;
        datosBancoPlanInteractor = new DatosBancoPlanInteractorImpl(this);
    }

    @Override
    public void showProgress() {
        if(datosBancoPlanView != null)
            datosBancoPlanView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(datosBancoPlanView != null)
            datosBancoPlanView.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(datosBancoPlanView != null)
            datosBancoPlanView.showError(mensaje);
    }

    @Override
    public void validarDatos(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                             int procedencia, Context context, JSONObject datosPago, int idSesion) {
        datosBancoPlanInteractor.validarDatos(nombreTitular, tarjeta, mes, ano, cvv, procedencia, context, datosPago, idSesion);
    }

    @Override
    public void setEstatusButtonValidarDatos(boolean estatus) {
        if(datosBancoPlanView != null)
            datosBancoPlanView.setEstatusButtonValidarDatos(estatus);
    }

    @Override
    public void cerrarFragment() {
        if(datosBancoPlanView != null)
            datosBancoPlanView.cerrarFragment();
    }

    @Override
    public void successOperation(String mensaje, String titulo, int tipo) {
        if(datosBancoPlanView != null)
            datosBancoPlanView.successOperation(mensaje, titulo, tipo);
    }

}

package com.proathome.Presenters.cliente;

import android.content.Context;
import android.os.Bundle;
import com.proathome.Interactors.cliente.NuevaSesionFrInteractorImpl;
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionInteractor;
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionPresenter;
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionView;

public class NuevaSesionFrPresenterImpl implements NuevaSesionPresenter {

    private NuevaSesionView nuevaSesionView;
    private NuevaSesionInteractor nuevaSesionInteractor;

    public NuevaSesionFrPresenterImpl(NuevaSesionView nuevaSesionView){
        this.nuevaSesionView = nuevaSesionView;
        nuevaSesionInteractor = new NuevaSesionFrInteractorImpl(this);
    }

    @Override
    public void getSesionActual(int idCliente, Context context) {
        nuevaSesionInteractor.getSesionActual(idCliente, context);
    }

    @Override
    public void setAdapters(int seccion, int nivel, int bloque, int minutos_horas) {
        if(nuevaSesionView != null)
            nuevaSesionView.setAdapters(seccion, nivel, bloque, minutos_horas);
    }

    @Override
    public void setSeccionesListener(int seccion, int nivel, int bloque, int minutos_horas) {
        if(nuevaSesionView != null)
            nuevaSesionView.setSeccionesListener(seccion, nivel, bloque, minutos_horas);
    }

    @Override
    public void setNivelesListener(int seccion, int nivel, int bloque, int minutos_horas) {
        if(nuevaSesionView != null)
            nuevaSesionView.setNivelesListener(seccion, nivel, bloque, minutos_horas);
    }

    @Override
    public void setBloquesListener(int seccion, int nivel, int bloque, int minutos_horas) {
        if(nuevaSesionView != null)
            nuevaSesionView.setBloquesListener(seccion, nivel, bloque, minutos_horas);
    }

    @Override
    public void setEstatusRutaFinalizada() {
        if(nuevaSesionView != null)
            nuevaSesionView.setEstatusRutaFinalizada();
    }

    @Override
    public void validarPlan(int idCliente, Context context) {
        nuevaSesionInteractor.validarPlan(idCliente, context);
    }

    @Override
    public void validarBanco(int idCliente, Context context) {
        nuevaSesionInteractor.validarBanco(idCliente, context);
    }

    @Override
    public void setBanco(boolean valor) {
        if(nuevaSesionView != null)
            nuevaSesionView.setBanco(valor);
    }

    @Override
    public void guardarPago(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, Context context) {
        nuevaSesionInteractor.guardarPago(idCliente, token, bundle, rutaFinalizada, context);
    }

    @Override
    public void showError(String error) {
        if(nuevaSesionView != null)
            nuevaSesionView.showError(error);
    }

    @Override
    public void finishFragment() {
        if(nuevaSesionView != null)
            nuevaSesionView.finishFragment();
    }

}

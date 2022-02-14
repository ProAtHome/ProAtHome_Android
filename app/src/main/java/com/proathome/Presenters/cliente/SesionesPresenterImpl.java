package com.proathome.Presenters.cliente;

import android.content.Context;
import com.proathome.Interactors.cliente.SesionesInteractorImpl;
import com.proathome.Interfaces.cliente.Sesiones.SesionesInteractor;
import com.proathome.Interfaces.cliente.Sesiones.SesionesPresenter;
import com.proathome.Interfaces.cliente.Sesiones.SesionesView;
import org.json.JSONObject;

public class SesionesPresenterImpl implements SesionesPresenter {

    private SesionesView sesionesView;
    private SesionesInteractor sesionesInteractor;

    public SesionesPresenterImpl(SesionesView sesionesView){
        this.sesionesView = sesionesView;
        sesionesInteractor = new SesionesInteractorImpl(this);
    }

    @Override
    public void getSesiones(int idCliente, Context context) {
        sesionesInteractor.getSesiones(idCliente, context);
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
    public void setVisibilityLottie() {
        if(sesionesView != null)
            sesionesView.setVisibilityLottie();
    }

    @Override
    public void setMyAdapter(JSONObject object) {
        if(sesionesView != null)
            sesionesView.setMyAdapter(object);
    }

    @Override
    public void showError(String error) {
        if(sesionesView != null)
            sesionesView.showError(error);
    }

    @Override
    public void setEstatusSesionesPagadas(boolean planActivo, boolean sesionesPagadas) {
        if(sesionesView != null)
            sesionesView.setEstatusSesionesPagadas(planActivo, sesionesPagadas);
    }

    @Override
    public void getDisponibilidadServicio(int idCliente, Context context) {
        //sesionesInteractor.getDisponibilidadServicio(idCliente, context);
    }

    @Override
    public void setDisponibilidadEstatus(boolean disponibilidad, int horas) {
        if(sesionesView != null)
            sesionesView.setDisponibilidadEstatus(disponibilidad, horas);
    }

    @Override
    public void getInfoInicioSesiones(int idCliente) {
        sesionesInteractor.getInfoInicioSesiones(idCliente);
    }

    @Override
    public void setInfoPlan(JSONObject jsonObject) {
        if(sesionesView != null)
            sesionesView.setInfoPlan(jsonObject);
    }

    @Override
    public void setRutaActual(JSONObject jsonObject) {
        if(sesionesView != null)
            sesionesView.setRutaActual(jsonObject);
    }

    @Override
    public void setDatosBanco(JSONObject jsonObject) {
        if(sesionesView != null)
            sesionesView.setDatosBanco(jsonObject);
    }

}

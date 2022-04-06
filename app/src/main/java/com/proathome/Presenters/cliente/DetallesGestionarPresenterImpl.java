package com.proathome.Presenters.cliente;

import android.content.Context;
import com.proathome.Interactors.cliente.DetallesGestionarInteractorImpl;
import com.proathome.Interfaces.cliente.DetallesGestionar.DetallesGestionarInteractor;
import com.proathome.Interfaces.cliente.DetallesGestionar.DetallesGestionarPresenter;
import com.proathome.Interfaces.cliente.DetallesGestionar.DetallesGestionarView;
import org.json.JSONObject;

public class DetallesGestionarPresenterImpl implements DetallesGestionarPresenter {

    private DetallesGestionarView detallesGestionarView;
    private DetallesGestionarInteractor detallesGestionarInteractor;

    public DetallesGestionarPresenterImpl(DetallesGestionarView detallesGestionarView){
        this.detallesGestionarView = detallesGestionarView;
        detallesGestionarInteractor = new DetallesGestionarInteractorImpl(this);
    }

    @Override
    public void getFechaServidor(String fechaSesion, Context context) {
        detallesGestionarInteractor.getFechaServidor(fechaSesion, context);
    }

    @Override
    public void setFechaServidor(String fecha) {
        if(detallesGestionarView != null)
            detallesGestionarView.setFechaServidor(fecha);
    }

    @Override
    public void actualizarSesion(JSONObject jsonObject) {
        detallesGestionarInteractor.actualizarSesion(jsonObject);
    }

    @Override
    public void eliminarSesion(JSONObject jsonObject) {
        detallesGestionarInteractor.eliminarSesion(jsonObject);
    }

    @Override
    public void showMsg(int tipo, String titulo, String mensaje){
        if(detallesGestionarView != null)
            detallesGestionarView.showMsg(tipo, titulo, mensaje);
    }

    @Override
    public void successDelete(String mensaje) {
        if(detallesGestionarView != null)
            detallesGestionarView.successDelete(mensaje);
    }

    @Override
    public void successUpdate(String mensaje) {
        if(detallesGestionarView != null)
            detallesGestionarView.successUpdate(mensaje);
    }

    @Override
    public void notificarProfesional(JSONObject jsonObject) {
        detallesGestionarInteractor.notificarProfesional(jsonObject);
    }

    @Override
    public void closeFragment() {
        if(detallesGestionarView != null)
            detallesGestionarView.closeFragment();
    }

    @Override
    public void validarEmpalme(int idCliente, String fecha, String horario, boolean cambioFecha, int idSesion) {
        detallesGestionarInteractor.validarEmpalme(idCliente, fecha, horario, cambioFecha, idSesion);
    }

    @Override
    public void update(boolean cambioFecha) {
        if(detallesGestionarView != null)
            detallesGestionarView.update(cambioFecha);
    }

}

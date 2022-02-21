package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.RutaInteractorImpl;
import com.proathome.Interfaces.cliente.Ruta.RutaInteractor;
import com.proathome.Interfaces.cliente.Ruta.RutaPresenter;
import com.proathome.Interfaces.cliente.Ruta.RutaView;

public class RutaPresenterImpl implements RutaPresenter {

    private RutaView rutaView;
    private RutaInteractor rutaInteractor;

    public RutaPresenterImpl(RutaView rutaView){
        this.rutaView = rutaView;
        rutaInteractor = new RutaInteractorImpl(this);
    }

    @Override
    public void getEstatusExamen(int idCliente, String token) {
        rutaInteractor.getEstatusExamen(idCliente, token);
    }

    @Override
    public void getEstadoRuta(int idCliente, int secciones, String token) {
        rutaInteractor.getEstadoRuta(idCliente, secciones, token);
    }

    @Override
    public void reiniciarExamen(int idCliente) {
        rutaInteractor.reiniciarExamen(idCliente);
    }

    @Override
    public void showMsg(String titulo, String mensaje, int tipo) {
        if(rutaView != null)
            rutaView.showMsg(titulo, mensaje, tipo);
    }

    @Override
    public void successReinicio() {
        if(rutaView != null)
            rutaView.successReinicio();
    }

    @Override
    public void setRutaActual(int idSeccion, int idNivel, int idBloque) {
        if(rutaView != null)
            rutaView.setRutaActual(idSeccion, idNivel, idBloque);
    }

    @Override
    public void setVisibilityButtonExamen(int visibility) {
        if(rutaView != null)
            rutaView.setVisibilityButtonExamen(visibility);
    }

    @Override
    public void showDialogExamen() {
        if(rutaView != null)
            rutaView.showDialogExamen();
    }

    @Override
    public void continuarExamen(int pregunta) {
        if(rutaView != null)
            rutaView.continuarExamen(pregunta);
    }

    @Override
    public void cancelarExamen(int idCliente) {
        rutaInteractor.cancelarExamen(idCliente);
    }

}

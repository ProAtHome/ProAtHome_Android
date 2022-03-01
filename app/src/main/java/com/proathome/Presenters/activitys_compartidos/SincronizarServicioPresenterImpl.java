package com.proathome.Presenters.activitys_compartidos;

import android.content.Context;
import com.proathome.Interactors.activitys_compartidos.SincronizarServicioInteractorImpl;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioInteractor;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioPresenter;
import com.proathome.Interfaces.activitys_compartidos.SincronizarServicio.SincronizarServicioView;

public class SincronizarServicioPresenterImpl implements SincronizarServicioPresenter {

    private SincronizarServicioView sincronizarServicioView;
    private SincronizarServicioInteractor sincronizarServicioInteractor;

    public SincronizarServicioPresenterImpl(SincronizarServicioView sincronizarServicioView){
        this.sincronizarServicioView = sincronizarServicioView;
        sincronizarServicioInteractor = new SincronizarServicioInteractorImpl(this);
    }

    @Override
    public void verificarDisponibilidadProfesional(int idSesion, int idPerfil, Context context) {
        sincronizarServicioInteractor.verificarDisponibilidadProfesional(idSesion, idPerfil, context);
    }

    @Override
    public void verificarDisponibilidadCliente(int idSesion, int idPerfil, Context context) {
        sincronizarServicioInteractor.verificarDisponibilidadCliente(idSesion, idPerfil, context);
    }

    @Override
    public void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible) {
        sincronizarServicioInteractor.cambiarDisponibilidadCliente(idSesion, idPerfil, disponible);
    }

    @Override
    public void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible) {
        sincronizarServicioInteractor.cambiarDisponibilidadProfesional(idSesion, idPerfil, disponible);
    }

    @Override
    public void cancelTime() {
        if(sincronizarServicioView != null)
            sincronizarServicioView.cancelTime();
    }

}

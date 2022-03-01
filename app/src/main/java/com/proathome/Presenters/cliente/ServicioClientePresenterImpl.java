package com.proathome.Presenters.cliente;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.proathome.Interactors.cliente.ServicioClienteInteractorImpl;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClienteInteractor;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClientePresenter;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClienteView;

public class ServicioClientePresenterImpl implements ServicioClientePresenter {

    private ServicioClienteView servicioClienteView;
    private ServicioClienteInteractor servicioClienteInteractor;

    public ServicioClientePresenterImpl(ServicioClienteView servicioClienteView){
        this.servicioClienteView = servicioClienteView;
        servicioClienteInteractor = new ServicioClienteInteractorImpl(this);
    }

    @Override
    public void servicioDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity) {
        servicioClienteInteractor.servicioDisponible(contexto, idSesion, idPerfil, tipoPerfil, activity);
    }

    @Override
    public void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible) {
        servicioClienteInteractor.cambiarDisponibilidadCliente(idSesion, idPerfil, disponible);
    }

}

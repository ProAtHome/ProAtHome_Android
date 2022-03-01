package com.proathome.Interactors.cliente;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClienteInteractor;
import com.proathome.Interfaces.cliente.ServicioCliente.ServicioClientePresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.sesiones.ServicioSesionDisponible;
import com.proathome.Views.cliente.fragments.DetallesFragment;

public class ServicioClienteInteractorImpl implements ServicioClienteInteractor {

    private ServicioClientePresenter servicioClientePresenter;

    public ServicioClienteInteractorImpl(ServicioClientePresenter servicioClientePresenter){
        this.servicioClientePresenter = servicioClientePresenter;
    }

    @Override
    public void servicioDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity) {
        ServicioSesionDisponible servicioSesionDisponible =
                new ServicioSesionDisponible(contexto, idSesion,
                        idPerfil, DetallesFragment.CLIENTE, activity);
        servicioSesionDisponible.execute();
    }

    @Override
    public void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_CLIENTE + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

}

package com.proathome.Presenters.cliente;

import android.content.Context;
import android.os.Bundle;
import com.proathome.Interactors.cliente.PreOrdenServicioInteractorImpl;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioInteractor;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioPresenter;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioView;

public class PreOrdenServicioPresenterImpl implements PreOrdenServicioPresenter {

    private PreOrdenServicioView preOrdenServicioView;
    private PreOrdenServicioInteractor preOrdenServicioInteractor;

    public PreOrdenServicioPresenterImpl(PreOrdenServicioView preOrdenServicioView){
        this.preOrdenServicioView = preOrdenServicioView;
        preOrdenServicioInteractor = new PreOrdenServicioInteractorImpl(this);
    }

    @Override
    public void pagar(Context contexto, String nombreTitular, String tarjeta, int mes, int ano, String cvv, int idCliente, Bundle bundle) {
        preOrdenServicioInteractor.pagar(contexto, nombreTitular, tarjeta, mes, ano, cvv, idCliente, bundle);
    }
}

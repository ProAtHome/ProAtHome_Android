package com.proathome.Interactors.cliente;

import android.content.Context;
import android.os.Bundle;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioInteractor;
import com.proathome.Interfaces.cliente.PreOrdenServicio.PreOrdenServicioPresenter;
import com.proathome.Servicios.api.openpay.TokenCardPagoServicio;

public class PreOrdenServicioInteractorImpl implements PreOrdenServicioInteractor {

    private PreOrdenServicioPresenter preOrdenServicioPresenter;

    public PreOrdenServicioInteractorImpl(PreOrdenServicioPresenter preOrdenServicioPresenter){
        this.preOrdenServicioPresenter = preOrdenServicioPresenter;
    }

    @Override
    public void pagar(Context contexto, String nombreTitular, String tarjeta, int mes, int ano, String cvv, int idCliente, Bundle bundle) {
        TokenCardPagoServicio servicioTaskCard = new TokenCardPagoServicio(contexto, nombreTitular, tarjeta, mes, ano, cvv, idCliente);
        servicioTaskCard.setBundleSesion(bundle);
        servicioTaskCard.execute();
    }
}

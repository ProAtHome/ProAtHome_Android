package com.proathome.Interfaces.cliente.PreOrdenServicio;

import android.content.Context;
import android.os.Bundle;

public interface PreOrdenServicioPresenter {

    void pagar(Context contexto, String nombreTitular, String tarjeta, int mes, int ano,
               String cvv, int idCliente, Bundle bundle);

}

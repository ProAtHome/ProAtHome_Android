package com.proathome.Interfaces.cliente.NuevaSesionFragment;

import android.content.Context;
import android.os.Bundle;

public interface NuevaSesionInteractor {

    void getSesionActual(int idCliente, Context context);

    void validarPlan(int idCliente, Context context);

    void validarBanco(int idCliente, Context context);

    void guardarPago(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, Context context);

}

package com.proathome.Interfaces.cliente.NuevaSesionFragment;

import android.content.Context;
import android.os.Bundle;

public interface NuevaSesionInteractor {

    //void getSesionActual(int idCliente, Context context);

    //void validarPlan(int idCliente, Context context);

    //void validarBanco(int idCliente, Context context);

    //void guardarPago(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, Context context);

    void registrarServicio(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, int nuevoMonedero, Context context);

    void validarEmpalme(int idCliente, String fecha, String horario);

}

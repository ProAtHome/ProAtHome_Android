package com.proathome.Interfaces.cliente.DatosBancoPlan;

import android.content.Context;
import org.json.JSONObject;

public interface DatosBancoPlanInteractor {

    void validarDatos(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                      int procedencia, Context context, JSONObject datosPago, int idSesion);

}

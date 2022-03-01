package com.proathome.Interfaces.cliente.Examen.Diagnostico7;

import android.content.Context;

public interface Diagnostico7Interactor {

    void getInfoExamenFinal(int idCliente, int puntacionAsumar, Context context);

    void actualizarEstatusExamen(int estatus, int idCliente, int puntacionAsumar, int preguntaActual);

}

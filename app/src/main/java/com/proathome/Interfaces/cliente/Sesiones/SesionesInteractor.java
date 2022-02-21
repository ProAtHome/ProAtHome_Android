package com.proathome.Interfaces.cliente.Sesiones;

import android.content.Context;

public interface SesionesInteractor {

    void getSesiones(int idCliente, Context context);

    //void getDisponibilidadServicio(int idCliente, Context context);

    void getInfoInicioSesiones(int idCliente, Context context);

}

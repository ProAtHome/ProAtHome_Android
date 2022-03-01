package com.proathome.Interfaces.activitys_compartidos.SincronizarServicio;

import android.content.Context;

public interface SincronizarServicioPresenter {

    void verificarDisponibilidadProfesional(int idSesion, int idPerfil, Context context);

    void verificarDisponibilidadCliente(int idSesion, int idPerfil, Context context);

    void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible);

    void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible);

    void cancelTime();

}

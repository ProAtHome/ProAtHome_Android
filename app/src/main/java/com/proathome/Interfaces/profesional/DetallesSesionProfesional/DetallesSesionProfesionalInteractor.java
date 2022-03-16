package com.proathome.Interfaces.profesional.DetallesSesionProfesional;

import android.content.Context;

public interface DetallesSesionProfesionalInteractor {

    void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible);

    void validarServicioFinalizadoProfesional(int idSesion, int idPerfil, Context context);

    void validarvaloracionCliente(int idSesion, int idCliente);

    void getFotoPerfil(String foto);

}

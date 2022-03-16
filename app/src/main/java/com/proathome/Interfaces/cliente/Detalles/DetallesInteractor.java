package com.proathome.Interfaces.cliente.Detalles;

import android.content.Context;

public interface DetallesInteractor {

    void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible);

    void validarServicioFinalizadoCliente(int idSesion, int idPerfil, Context context);

    void validarValoracionProfesional(int idSesion, int idPerfil, Context context);

    void validarBloqueoPerfil(int idCliente, Context context);

    void getFotoPerfil(String foto);

}

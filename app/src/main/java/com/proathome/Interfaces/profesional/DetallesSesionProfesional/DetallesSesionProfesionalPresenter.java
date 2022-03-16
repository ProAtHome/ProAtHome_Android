package com.proathome.Interfaces.profesional.DetallesSesionProfesional;

import android.content.Context;
import android.graphics.Bitmap;

public interface DetallesSesionProfesionalPresenter {

    void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible);

    void validarServicioFinalizadoProfesional(int idSesion, int idPerfil, Context context);

    void validarvaloracionCliente(int idSesion, int idCliente);

    void getFotoPerfil(String foto);

    void setFotoBitmap(Bitmap bitmap);

    void showError(String mensaje);

}

package com.proathome.Interfaces.profesional.DetallesGestionar;

public interface DetallesGestionarInteractor {

    void cancelarSesion(int idProfesional, int idSesion);

    void solicitudEliminarSesion(int idProfesional, int idSesion, String token);

    void getFotoPerfil(String mensaje);

}

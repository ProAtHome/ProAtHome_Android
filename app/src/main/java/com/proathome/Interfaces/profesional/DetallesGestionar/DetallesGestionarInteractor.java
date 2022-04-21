package com.proathome.Interfaces.profesional.DetallesGestionar;

import org.json.JSONObject;

public interface DetallesGestionarInteractor {

    void cancelarSesion(int idProfesional, int idSesion);

    void solicitudEliminarSesion(int idProfesional, int idSesion, String token);

    void getFotoPerfil(String mensaje);

    void notificarCliente(JSONObject jsonObject);

}

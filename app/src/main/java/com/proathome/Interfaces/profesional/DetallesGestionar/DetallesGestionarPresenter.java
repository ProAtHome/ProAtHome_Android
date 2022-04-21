package com.proathome.Interfaces.profesional.DetallesGestionar;

import android.graphics.Bitmap;

import org.json.JSONObject;

public interface DetallesGestionarPresenter {

    void cancelarSesion(int idProfesional, int idSesion);

    void showError(String mensaje);

    void successCancel(String mensaje);

    void solicitudEliminarSesion(int idProfesional, int idSesion, String token);

    void showAlertCancel(String mensaje);

    void getFotoPerfil(String mensaje);

    void setFotoBitmap(Bitmap bitmap);

    void showProgress();

    void hideProgress();

    void notificarCliente(JSONObject jsonObject);

    void closeFragment();

}

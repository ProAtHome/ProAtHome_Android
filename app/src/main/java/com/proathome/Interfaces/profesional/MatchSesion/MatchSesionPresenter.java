package com.proathome.Interfaces.profesional.MatchSesion;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface MatchSesionPresenter {

    void getInfoSesion(int idSesion);

    void showError(String error);

    void setInfoSesion(JSONObject jsonObject);

    void matchSesion(int idProfesional, int idSesion, JSONObject serviciosDisponibles,
                     JSONObject serviciosPendientes, String fechaActual, String fechaServicio,
                        String horario, int rango);

    void successMatch(String mensaje);

    void getImage(String foto);

    void setImageBitmap(Bitmap bitmap);

    void showProgress();

    void hideProgress();

    void notificarCliente(JSONObject jsonObject);

    void closeActivity();

}

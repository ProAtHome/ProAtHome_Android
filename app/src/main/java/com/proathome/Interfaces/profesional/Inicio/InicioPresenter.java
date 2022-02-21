package com.proathome.Interfaces.profesional.Inicio;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface InicioPresenter {

    void validarTokenSesion(int idProfesional, String token);

    void cargarPerfil(int idProfesional, String token);

    void showError(String error);

    void setFoto(Bitmap bitmap);

    void sesionExpirada();

    void setInfoPerfil(JSONObject jsonObject);

}

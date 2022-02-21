package com.proathome.Interfaces.profesional.Inicio;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface InicioView {

    void showError(String error);

    void setFoto(Bitmap bitmap);

    void sesionExpirada();

    void setInfoPerfil(JSONObject jsonObject);

}

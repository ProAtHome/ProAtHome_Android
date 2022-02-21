package com.proathome.Interfaces.cliente.Inicio;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface InicioView {

    void setFoto(Bitmap bitmap);

    void toMainActivity();

    void setInfoPerfil(JSONObject jsonObject);

    void showError(String error);

}

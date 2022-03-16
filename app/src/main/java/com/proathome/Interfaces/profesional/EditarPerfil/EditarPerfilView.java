package com.proathome.Interfaces.profesional.EditarPerfil;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface EditarPerfilView {

    void showProgress();

    void hideProgress();

    void showSuccess(String mensaje);

    void showError(String mensaje);

    void setVisibilityAvisos(boolean visivilityAvisos, String mensaje);

    void setDatosPerfil(JSONObject jsonObject);

    void setDatosBanco(JSONObject jsonObject);

    void setFotoBitmap(Bitmap bitmap);

}

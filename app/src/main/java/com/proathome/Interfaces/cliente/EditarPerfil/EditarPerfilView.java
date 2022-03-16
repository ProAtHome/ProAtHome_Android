package com.proathome.Interfaces.cliente.EditarPerfil;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface EditarPerfilView {

    void showError(String mensaje);

    void setVisibilityReportes(boolean visibilityReportes, String mensaje);

    void showProgress();

    void hideProgress();

    void setDatosPerfil(JSONObject jsonObject);

    void setDatosBanco(JSONObject jsonObject);

    void showErrorBanco(String mensaje);

    void successUpdate(String mensaje);

    void setFotoBitmap(Bitmap bitmap);

}

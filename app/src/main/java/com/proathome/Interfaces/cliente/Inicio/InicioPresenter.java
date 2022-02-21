package com.proathome.Interfaces.cliente.Inicio;

import android.content.Context;
import android.graphics.Bitmap;
import org.json.JSONObject;

public interface InicioPresenter {

    void validarTokenSesion(int idCliente, Context context);

    void setFoto(Bitmap bitmap);

    void cargarPerfil(int idCliente, Context context);

    void toMainActivity();

    void setInfoPerfil(JSONObject jsonObject);

    void showError(String error);

}

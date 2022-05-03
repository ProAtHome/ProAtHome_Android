package com.proathome.Interfaces.cliente.EditarPerfil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import org.json.JSONObject;

public interface EditarPerfilPresenter {

    void getReportes(int idCliente, String token);

    void getDatosPerfil(int idCliente, String token);

    void getDatosBanco(int idCliente, String token);

    void showError(String mensaje);

    void setVisibilityReportes(boolean visibilityReportes, String mensaje);

    void showProgress();

    void hideProgress();

    void setDatosPerfil(JSONObject jsonObject);

    void setDatosBanco(JSONObject jsonObject);

    void showErrorBanco(String mensaje);

    void updateCuentaCliente(JSONObject jsonObject);

    void successUpdate(String mensaje);

    void updatePerfil(JSONObject jsonObject, Context context, int idCliente);

    void getBitmapMedia(Intent data, ContentResolver contentResolver);

    void setFotoMedia(Bitmap bitmap);

    void getFotoPerfil(String foto);

}

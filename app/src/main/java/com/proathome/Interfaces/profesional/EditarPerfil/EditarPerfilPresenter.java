package com.proathome.Interfaces.profesional.EditarPerfil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import org.json.JSONObject;

public interface EditarPerfilPresenter {

    void showProgress();

    void hideProgress();

    void getReportes(int idProfesional, String token);

    void showError(String mensaje);

    void showSuccess(String mensaje);

    void setVisibilityAvisos(boolean visivilityAvisos, String mensaje);

    void getDatosPerfil(int idProfesional, String token);

    void setDatosPerfil(JSONObject jsonObject);

    void getDatosBanco(int idProfesional, String token);

    void setDatosBanco(JSONObject jsonObject);

    void getFotoPerfil(String foto);

    void setFotoBitmap(Bitmap bitmap);

    void actualizarPerfil(JSONObject jsonObject);

    void uploadImage(int idProfesional, Context context);

    void getMediaGalery(ContentResolver contentResolver, Intent data);

    void updateBancoService(JSONObject jsonObject);

}

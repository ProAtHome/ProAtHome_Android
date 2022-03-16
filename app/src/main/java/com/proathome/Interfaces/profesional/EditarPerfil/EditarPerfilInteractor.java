package com.proathome.Interfaces.profesional.EditarPerfil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONObject;

public interface EditarPerfilInteractor {

    void getReportes(int idProfesional, String token);

    void getDatosPerfil(int idProfesional, String token);

    void getDatosBanco(int idProfesional, String token);

    void getFotoPerfil(String foto);

    void actualizarPerfil(JSONObject jsonObject);

    void uploadImage(int idProfesional, Context context);

    void getMediaGalery(ContentResolver contentResolver, Intent data);

    void updateBancoService(JSONObject jsonObject);

}

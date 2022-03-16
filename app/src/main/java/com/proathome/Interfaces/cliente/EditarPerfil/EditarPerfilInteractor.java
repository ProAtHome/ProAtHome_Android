package com.proathome.Interfaces.cliente.EditarPerfil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONObject;

public interface EditarPerfilInteractor {

    void getReportes(int idCliente, String token);

    void getDatosPerfil(int idCliente, String token);

    void getDatosBanco(int idCliente, String token);

    void updateCuentaCliente(JSONObject jsonObject);

    void updatePerfil(JSONObject jsonObject, Context context, int idCliente);

    void getBitmapMedia(Intent data, ContentResolver contentResolver);

    void getFotoPerfil(String foto);

}

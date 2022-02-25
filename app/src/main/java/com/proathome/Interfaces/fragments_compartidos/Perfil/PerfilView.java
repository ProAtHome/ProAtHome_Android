package com.proathome.Interfaces.fragments_compartidos.Perfil;

import android.graphics.Bitmap;

public interface PerfilView {

    void showError(String error);

    void setAdapter(String comentario, float valoracion);

    void setInfoPerfil(String nombre, String correo, String descripcion);

    void setFoto(Bitmap bitmap);

    void showProgress();

    void hideProgress();

}

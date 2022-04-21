package com.proathome.Interfaces.profesional.DetallesGestionar;

import android.graphics.Bitmap;

public interface DetallesGestionarView {

    void showError(String mensaje);

    void successCancel(String mensaje);

    void showAlertCancel(String mensaje);

    void setFotoBitmap(Bitmap bitmap);

    void showProgress();

    void hideProgress();

    void closeFragment();

}

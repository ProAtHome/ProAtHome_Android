package com.proathome.Interfaces.cliente.Detalles;

import android.graphics.Bitmap;

public interface DetallesView {

    void showProgress();

    void hideProgress();

    void setFotoBitmap(Bitmap bitmap);

    void showError(String mensaje);

}

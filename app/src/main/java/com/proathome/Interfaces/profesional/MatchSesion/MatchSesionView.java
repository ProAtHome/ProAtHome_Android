package com.proathome.Interfaces.profesional.MatchSesion;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface MatchSesionView {

    void showError(String error);

    void setInfoSesion(JSONObject jsonObject);

    void successMatch(String mensaje);

    void setImageBitmap(Bitmap bitmap);

    void showProgress();

    void hideProgress();

}

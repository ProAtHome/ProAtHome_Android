package com.proathome.Interfaces.profesional.MatchSesion;

import android.graphics.Bitmap;
import org.json.JSONObject;

public interface MatchSesionPresenter {

    void getInfoSesion(int idSesion);

    void showError(String error);

    void setInfoSesion(JSONObject jsonObject);

    void matchSesion(int idProfesional, int idSesion);

    void successMatch(String mensaje);

    void getImage(String foto);

    void setImageBitmap(Bitmap bitmap);

    void showProgress();

    void hideProgress();

}

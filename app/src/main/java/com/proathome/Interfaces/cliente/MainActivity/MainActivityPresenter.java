package com.proathome.Interfaces.cliente.MainActivity;

import android.content.Context;

public interface MainActivityPresenter {

    void latidoSQL();

    void login(String correo, String pass, Context context);

    void showProgress();

    void hideProgress();

    void showErrorLogin(String error);

    void sesionExistente(Context context);

    void finishActivity();

}

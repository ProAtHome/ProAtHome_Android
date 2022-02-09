package com.proathome.Interfaces.cliente.MainActivity;

public interface MainActivityView {

    void showProgress();

    void hideProgress();

    void showErrorLogin(String error);

    void finishActivity();

}

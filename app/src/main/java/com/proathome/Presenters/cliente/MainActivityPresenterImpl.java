package com.proathome.Presenters.cliente;

import android.content.Context;
import com.proathome.Interactors.cliente.MainActivityInteractorImpl;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityInteractor;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityPresenter;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityView;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityView mainActivityView;
    private MainActivityInteractor mainActivityInteractor;

    public MainActivityPresenterImpl(MainActivityView mainActivityView){
        this.mainActivityView = mainActivityView;
        mainActivityInteractor = new MainActivityInteractorImpl(this);
    }

    @Override
    public void latidoSQL() {
        mainActivityInteractor.latidoSQL();
    }

    @Override
    public void login(String correo, String pass, Context context) {
        mainActivityInteractor.login(correo, pass, context);
    }

    @Override
    public void showProgress() {
        if(mainActivityView != null)
            mainActivityView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(mainActivityView != null)
            mainActivityView.hideProgress();
    }

    @Override
    public void showErrorLogin(String error) {
        if(mainActivityView != null)
            mainActivityView.showErrorLogin(error);
    }

    @Override
    public void sesionExistente(Context context) {
        mainActivityInteractor.sesionExistente(context);
    }

    @Override
    public void finishActivity() {
        if(mainActivityView != null)
            mainActivityView.finishActivity();
    }

}

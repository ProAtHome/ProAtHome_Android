package com.proathome.Presenters.cliente;

import com.proathome.Interactors.cliente.OrdenCompraPlanInteractorImpl;
import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanInteractor;
import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanPresenter;
import com.proathome.Interfaces.cliente.OrdenCompraPlan.OrdenCompraPlanView;
import org.json.JSONObject;

public class OrdenCompraPlanPresenterImpl implements OrdenCompraPlanPresenter {

    private OrdenCompraPlanView ordenCompraPlanView;
    private OrdenCompraPlanInteractor ordenCompraPlanInteractor;

    public OrdenCompraPlanPresenterImpl(OrdenCompraPlanView ordenCompraPlanView){
        this.ordenCompraPlanView = ordenCompraPlanView;
        ordenCompraPlanInteractor = new OrdenCompraPlanInteractorImpl(this);
    }

    @Override
    public void comprar(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                        int idCliente, String token, JSONObject jsonDatosPago) {
        ordenCompraPlanInteractor.comprar(nombreTitular, tarjeta, mes, ano, cvv, idCliente, token, jsonDatosPago);
    }

    @Override
    public void showProgress() {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.hideProgress();
    }

    @Override
    public void showError(String mensaje) {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.showError(mensaje);
    }

    @Override
    public void btnComprarEnabled(boolean enabled) {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.btnComprarEnabled(enabled);
    }

    @Override
    public void successPlan() {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.successPlan();
    }

    @Override
    public void errorPlan(String error) {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.errorPlan(error);
    }

    @Override
    public void validarPlanError(String mensaje) {
        if(ordenCompraPlanView != null)
            ordenCompraPlanView.validarPlanError(mensaje);
    }

}

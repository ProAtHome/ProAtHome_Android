package com.proathome.Interfaces.cliente.OrdenCompraPlan;

public interface OrdenCompraPlanView {

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void btnComprarEnabled(boolean enabled);

    void successPlan();

    void errorPlan(String error);

    void validarPlanError(String mensaje);

}

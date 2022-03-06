package com.proathome.Interfaces.cliente.OrdenCompraPlan;

import org.json.JSONObject;

public interface OrdenCompraPlanPresenter {

    void comprar(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                 int idCliente, String token, JSONObject jsonDatosPago);

    void showProgress();

    void hideProgress();

    void showError(String mensaje);

    void btnComprarEnabled(boolean enabled);

    void successPlan();

    void errorPlan(String error);

    void validarPlanError(String mensaje);

}

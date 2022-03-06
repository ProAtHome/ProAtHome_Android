package com.proathome.Interfaces.cliente.OrdenCompraPlan;

import org.json.JSONObject;

public interface OrdenCompraPlanInteractor {

    void comprar(String nombreTitular, String tarjeta, String mes, String ano, String cvv,
                 int idCliente, String token, JSONObject jsonDatosPago);

}

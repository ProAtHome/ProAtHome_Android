package com.proathome.Interfaces.cliente.Inicio;

import android.content.Context;

public interface InicioInteractor {

    void validarTokenSesion(int idCliente, Context context);

    void cargarPerfil(int idCliente, Context context);

}

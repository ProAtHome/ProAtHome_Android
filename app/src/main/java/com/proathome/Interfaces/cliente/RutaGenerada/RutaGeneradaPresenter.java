package com.proathome.Interfaces.cliente.RutaGenerada;

import com.proathome.Views.cliente.examen.EvaluarRuta;

public interface RutaGeneradaPresenter {

    void rutaExamen(EvaluarRuta evaluarRuta, int idCliente);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void cerrarFragment();

}

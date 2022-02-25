package com.proathome.Interfaces.fragments_compartidos.Evaluar;

import android.os.Bundle;

public interface EvaluarPresenter {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void enviarEvaluacion(String comentario, float evaluacion, int procedencia, String token);

    void toPagoPendiente(Bundle bundle);

    void cerrarFragment();

}

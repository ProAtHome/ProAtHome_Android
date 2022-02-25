package com.proathome.Interfaces.fragments_compartidos.Evaluar;

import android.os.Bundle;

public interface EvaluarView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void toPagoPendiente(Bundle bundle);

    void cerrarFragment();

}

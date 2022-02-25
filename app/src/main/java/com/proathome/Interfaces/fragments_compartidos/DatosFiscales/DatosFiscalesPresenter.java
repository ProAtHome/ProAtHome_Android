package com.proathome.Interfaces.fragments_compartidos.DatosFiscales;

import android.content.Context;
import org.json.JSONObject;

public interface DatosFiscalesPresenter {

    void getDatosFiscales(int tipoPerfil, int idUsuario, Context context);

    void showProgress();

    void hideProgress();

    void showError(String error);

    void setInfoDatos(JSONObject datos);

    void upDatosFiscales(int tipoPerfil, int idUsuario, String tipoPersona, String razonSocial,
                         String rfc, String cfdi);

    void updateSuccess(String mensaje);

}

package com.proathome.Interfaces.fragments_compartidos.DatosFiscales;

import android.content.Context;

public interface DatosFiscalesInteractor {

    void getDatosFiscales(int tipoPerfil, int idUsuario, Context context);

    void upDatosFiscales(int tipoPerfil, int idUsuario, String tipoPersona, String razonSocial,
                         String rfc, String cfdi);

}

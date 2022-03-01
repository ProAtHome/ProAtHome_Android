package com.proathome.Interfaces.cliente.ServicioCliente;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

public interface ServicioClientePresenter {

    void servicioDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity);

    void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible);

}

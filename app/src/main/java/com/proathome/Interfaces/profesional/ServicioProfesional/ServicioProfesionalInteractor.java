package com.proathome.Interfaces.profesional.ServicioProfesional;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

public interface ServicioProfesionalInteractor {

    void cambiarEstatusServicio(int estatus, int idSesion, int idProfesional);

    void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible);

    void guardarProgreso(int idSesion, int idPerfil, int progreso, int progresoSegundos, int tipoDeTiempo);

    void validarServicioFinalizadoEnClase(int idSesion, int idPerfil, Context context);

    void validarSesionDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity);

}

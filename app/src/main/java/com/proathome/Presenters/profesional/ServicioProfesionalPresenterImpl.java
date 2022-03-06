package com.proathome.Presenters.profesional;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.proathome.Interactors.profesional.ServicioProfesionalInteractorImpl;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalInteractor;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalPresenter;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalView;

public class ServicioProfesionalPresenterImpl implements ServicioProfesionalPresenter {

    private ServicioProfesionalView servicioProfesionalView;
    private ServicioProfesionalInteractor servicioProfesionalInteractor;

    public ServicioProfesionalPresenterImpl(ServicioProfesionalView servicioProfesionalView){
        this.servicioProfesionalView = servicioProfesionalView;
        servicioProfesionalInteractor = new ServicioProfesionalInteractorImpl(this);
    }

    @Override
    public void cambiarEstatusServicio(int estatus, int idSesion, int idProfesional) {
        servicioProfesionalInteractor.cambiarEstatusServicio(estatus, idSesion, idProfesional);
    }

    @Override
    public void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible) {
        servicioProfesionalInteractor.cambiarDisponibilidadProfesional(idSesion, idPerfil, disponible);
    }

    @Override
    public void guardarProgreso(int idSesion, int idPerfil, int progreso, int progresoSegundos, int tipoDeTiempo) {
        servicioProfesionalInteractor.guardarProgreso(idSesion, idPerfil, progreso, progresoSegundos, tipoDeTiempo);
    }

    @Override
    public void validarServicioFinalizadoEnClase(int idSesion, int idPerfil, Context context) {
        servicioProfesionalInteractor.validarServicioFinalizadoEnClase(idSesion, idPerfil, context);
    }

    @Override
    public void validarSesionDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity) {
        servicioProfesionalInteractor.validarSesionDisponible(contexto, idSesion, idPerfil, tipoPerfil, activity);
    }

    @Override
    public void showError(String mensaje) {
        if(servicioProfesionalView != null)
            servicioProfesionalView.showError(mensaje);
    }

}

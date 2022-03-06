package com.proathome.Interactors.profesional;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalInteractor;
import com.proathome.Interfaces.profesional.ServicioProfesional.ServicioProfesionalPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.sesiones.ServicioSesionDisponible;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class ServicioProfesionalInteractorImpl implements ServicioProfesionalInteractor {

    private ServicioProfesionalPresenter servicioProfesionalPresenter;

    public ServicioProfesionalInteractorImpl(ServicioProfesionalPresenter servicioProfesionalPresenter){
        this.servicioProfesionalPresenter = servicioProfesionalPresenter;
    }

    @Override
    public void cambiarEstatusServicio(int estatus, int idSesion, int idProfesional) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_ESTATUS_SERVICIO_PROFESIONAL + idSesion + "/" + idProfesional + "/" + estatus, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    @Override
    public void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_PROFESIONAL + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    @Override
    public void guardarProgreso(int idSesion, int idPerfil, int progreso, int progresoSegundos, int tipoDeTiempo) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.GUARDAR_PROGRESO_SERVICIO_PROFESIONAL + idSesion + "/" + idPerfil + "/" + progreso + "/" + progresoSegundos + "/" + tipoDeTiempo, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarServicioFinalizadoEnClase(int idSesion, int idPerfil, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        boolean finalizado = jsonObject.getBoolean("finalizado");
                        if(finalizado){
                            Constants.fragmentActivity.finish();
                            DetallesSesionProfesionalFragment.procedenciaFin = true;
                        }else
                            servicioProfesionalPresenter.showError("El cliente todavia no decide si tomar tiempo extra o finalizar el servicio.");
                    }else
                        servicioProfesionalPresenter.showError(data.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    servicioProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                servicioProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_PROFESIONAL + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarSesionDisponible(Context contexto, int idSesion, int idPerfil, int tipoPerfil, FragmentActivity activity) {
        ServicioSesionDisponible servicioSesionDisponible =
                new ServicioSesionDisponible(contexto, idSesion, idPerfil, tipoPerfil, activity);
        servicioSesionDisponible.execute();
    }

}

package com.proathome.Interactors.profesional;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalInteractor;
import com.proathome.Interfaces.profesional.DetallesSesionProfesional.DetallesSesionProfesionalPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.fragments_compartidos.EvaluarFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class DetallesSesionProfesionalInteractorImpl implements DetallesSesionProfesionalInteractor {

    private DetallesSesionProfesionalPresenter detallesSesionProfesionalPresenter;

    public DetallesSesionProfesionalInteractorImpl(DetallesSesionProfesionalPresenter detallesSesionProfesionalPresenter){
        this.detallesSesionProfesionalPresenter = detallesSesionProfesionalPresenter;
    }

    @Override
    public void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_PROFESIONAL + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarServicioFinalizadoProfesional(int idSesion, int idPerfil, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        boolean finalizado = jsonObject.getBoolean("finalizado");

                        if(finalizado){
                            DetallesSesionProfesionalFragment.iniciar.setEnabled(false);
                            DetallesSesionProfesionalFragment.iniciar.setText("Servicio finalizado");
                        }
                    }else
                        detallesSesionProfesionalPresenter.showError(data.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    detallesSesionProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                detallesSesionProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_PROFESIONAL + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarvaloracionCliente(int idSesion, int idCliente) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("valorado")){
                        FragmentTransaction fragmentTransaction = DetallesSesionProfesionalFragment
                                .fragmentDetallesProf.getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_PROFESIONAL);
                        EvaluarFragment evaluarFragment = new EvaluarFragment();
                        evaluarFragment.setArguments(bundle);
                        evaluarFragment.show(fragmentTransaction, "Evaluación");
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                    detallesSesionProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                detallesSesionProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_VALORACION_CLIENTE + idSesion + "/" + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void getFotoPerfil(String foto) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            if(response != null)
                detallesSesionProfesionalPresenter.setFotoBitmap(response);
            else
                detallesSesionProfesionalPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

}

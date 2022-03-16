package com.proathome.Interactors.cliente;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.Interfaces.cliente.Detalles.DetallesInteractor;
import com.proathome.Interfaces.cliente.Detalles.DetallesPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.pojos.Component;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.cliente.fragments.PagoPendienteFragment;
import com.proathome.Views.fragments_compartidos.EvaluarFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class DetallesInteractorImpl implements DetallesInteractor {

    private DetallesPresenter detallesPresenter;

    public DetallesInteractorImpl(DetallesPresenter detallesPresenter){
        this.detallesPresenter = detallesPresenter;
    }

    @Override
    public void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_CLIENTE + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarServicioFinalizadoCliente(int idSesion, int idPerfil, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        boolean finalizado = jsonObject.getBoolean("finalizado");

                        if(finalizado){
                            DetallesFragment.iniciar.setEnabled(false);
                            DetallesFragment.iniciar.setText("Servicio finalizado");
                        }
                    }else
                        detallesPresenter.showError(data.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    detallesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                detallesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_CLIENTE + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarValoracionProfesional(int idSesion, int idPerfil, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("valorado")){
                        FragmentTransaction fragmentTransaction = DetallesFragment.detallesFragment
                                .getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_CLIENTE);
                        EvaluarFragment evaluarFragment = new EvaluarFragment();
                        evaluarFragment.setArguments(bundle);
                        evaluarFragment.show(fragmentTransaction, "Evaluación");
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                    detallesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                detallesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_VALORACION_PROFESIONAL + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void validarBloqueoPerfil(int idCliente, Context context) {
        WebServicesAPI bloquearPerfil = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        PagoPendienteFragment pagoPendienteFragment = new PagoPendienteFragment();
                        Bundle bundle = new Bundle();
                        if(jsonObject.getBoolean("bloquear")){
                            FragmentTransaction fragmentTransaction = null;
                            bundle.putDouble("deuda", jsonObject.getDouble("deuda"));
                            bundle.putString("sesion", Component.getSeccion(jsonObject.getInt("idSeccion")) +
                                    " / " + Component.getNivel(jsonObject.getInt("idSeccion"),
                                    jsonObject.getInt("idNivel")) + " / " + jsonObject.getInt("idBloque"));
                            bundle.putString("lugar", jsonObject.getString("lugar"));
                            bundle.putString("nombre", jsonObject.getString("nombre"));
                            bundle.putString("correo", jsonObject.getString("correo"));
                            bundle.putInt("idSesion", jsonObject.getInt("idSesion"));
                            fragmentTransaction = DetallesFragment.detallesFragment.getFragmentManager().beginTransaction();
                            pagoPendienteFragment.setArguments(bundle);
                            pagoPendienteFragment.show(fragmentTransaction, "Pago pendiente");
                        }
                    }else
                        detallesPresenter.showError("Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.");
                }else
                    detallesPresenter.showError("Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.");
            }catch(JSONException ex){
                ex.printStackTrace();
                detallesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            }
        }, APIEndPoints.BLOQUEAR_PERFIL + "/" + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        bloquearPerfil.execute();
    }

    @Override
    public void getFotoPerfil(String foto) {
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            if(response != null)
                detallesPresenter.setFotoBitmap(response);
            else
                detallesPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

}

package com.proathome.Interactors.fragments_compartidos;

import android.view.View;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketInteractor;
import com.proathome.Interfaces.fragments_compartidos.NuevoTicket.NuevoTicketPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.pojos.ComponentTicket;
import com.proathome.Views.cliente.navigator.ayuda.AyudaFragment;
import com.proathome.Views.fragments_compartidos.FragmentTicketAyuda;
import com.proathome.Views.profesional.navigator.ayudaProfesional.AyudaProfesionalFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NuevoTicketInteractorImpl implements NuevoTicketInteractor {

    private NuevoTicketPresenter nuevoTicketPresenter;

    public NuevoTicketInteractorImpl(NuevoTicketPresenter nuevoTicketPresenter){
        this.nuevoTicketPresenter = nuevoTicketPresenter;
    }

    @Override
    public void nuevoTicket(JSONObject jsonObject, int tipoUsuario) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null)
                nuevoTicketPresenter.successTicket("Tu solicitud será revisada y en breve te contestará soporte.");
            else
                nuevoTicketPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, tipoUsuario == Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.NUEVO_TICKET_CLIENTE : APIEndPoints.NUEVO_TICKET_PROFESIONAL, WebServicesAPI.POST, jsonObject);
        webServicesAPI.execute();
    }

    @Override
    public void obtenerTickets(int tipoUsuario, int idUsuario, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONArray jsonArray = data.getJSONArray("mensaje");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(tipoUsuario == Constants.TIPO_USUARIO_CLIENTE){
                                if (jsonObject.getBoolean("sinTickets")){
                                    AyudaFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                                } else{
                                    AyudaFragment.lottieAnimationView.setVisibility(View.INVISIBLE);
                                    AyudaFragment.componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                            ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                            jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                            jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                            jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario"), jsonObject.getString("categoria")));
                                }
                            }else if(tipoUsuario == Constants.TIPO_USUARIO_PROFESIONAL){
                                if (jsonObject.getBoolean("sinTickets")){
                                    AyudaProfesionalFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                                } else{
                                    AyudaProfesionalFragment.lottieAnimationView.setVisibility(View.INVISIBLE);
                                    AyudaProfesionalFragment.componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                            ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                            jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                            jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                            jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario"), jsonObject.getString("categoria")));
                                }
                            }

                        }
                    }else
                        nuevoTicketPresenter.showError(data.getString("mensaje"));
                }catch (JSONException ex){
                    ex.printStackTrace();
                    nuevoTicketPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                nuevoTicketPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, tipoUsuario ==  Constants.TIPO_USUARIO_CLIENTE ? APIEndPoints.GET_TICKETS_CLIENTE + idUsuario + "/" + token :
                APIEndPoints.GET_TICKETS_PROFESIONAL + idUsuario + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

}

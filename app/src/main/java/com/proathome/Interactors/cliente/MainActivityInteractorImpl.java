package com.proathome.Interactors.cliente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityInteractor;
import com.proathome.Interfaces.cliente.MainActivity.MainActivityPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.activitys_compartidos.PerfilBloqueado;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.MainActivity;
import com.proathome.Views.cliente.PasosActivarCuentaCliente;
import com.proathome.Views.profesional.InicioProfesional;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityInteractorImpl implements MainActivityInteractor {

    private MainActivityPresenter mainActivityPresenter;

    public MainActivityInteractorImpl(MainActivityPresenter mainActivityPresenter){
        this.mainActivityPresenter = mainActivityPresenter;
    }

    @Override
    public void latidoSQL() {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.LATIDO_SQL, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void login(String correo, String pass, Context context) {
        mainActivityPresenter.showProgress();
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            mainActivityPresenter.hideProgress();
            try{
                if(response == null){
                    mainActivityPresenter.showErrorLogin("Ocurrió un error inesperado, intenta de nuevo.");
                }else {
                    if(!response.equals("null")){
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("estado").equalsIgnoreCase("ACTIVO")){
                            if(jsonObject.getBoolean("verificado")){
                                //TODO PRUBAS SHARED PREFERENCES
                                SharedPreferencesManager.getInstance(context).logout();
                                SharedPreferencesManager.getInstance(context).guardarSesionCliente(jsonObject.getInt("idCliente"), correo, jsonObject.getString("token"));

                                context.startActivity(new Intent(context, InicioCliente.class));
                            }else
                                mainActivityPresenter.showErrorLogin("Aún no verificas tu cuenta de correo electrónico.");
                        }else if(jsonObject.getString("estado").equalsIgnoreCase("DOCUMENTACION") ||
                                jsonObject.getString("estado").equalsIgnoreCase("REGISTRO")){
                            context.startActivity(new Intent(context, PasosActivarCuentaCliente.class));
                        }else if(jsonObject.getString("estado").equalsIgnoreCase("BLOQUEADO")){
                            Bundle bundle = new Bundle();
                            bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_CLIENTE);
                            Intent intent = new Intent(context, PerfilBloqueado.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }else
                        mainActivityPresenter.showErrorLogin("Usuario no registrado o tus datos están incorrectos.");
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.INICIAR_SESION_CLIENTE + "/" + correo + "/" + pass, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void sesionExistente(Context context) {
        if(SharedPreferencesManager.getInstance(context).getIDCliente() != 0){
            context.startActivity(new Intent(context, InicioCliente.class));
            mainActivityPresenter.finishActivity();
        }else if(SharedPreferencesManager.getInstance(context).getIDProfesional() != 0){
            context.startActivity(new Intent(context, InicioProfesional.class));
            mainActivityPresenter.finishActivity();
        }
    }

}

package com.proathome.Interactors.cliente.inicio;

import android.content.Context;
import android.util.Log;
import com.proathome.Interfaces.cliente.Inicio.InicioInteractor;
import com.proathome.Interfaces.cliente.Inicio.InicioPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.api.assets.WebServiceAPIAssets;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.cliente.fragments.PlanesFragment;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class InicioInteractorImpl implements InicioInteractor {

    private InicioPresenter inicioPresenter;

    public InicioInteractorImpl(InicioPresenter inicioPresenter){
        this.inicioPresenter = inicioPresenter;
    }

    @Override
    public void validarTokenSesion(int idCliente, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                JSONObject data = new JSONObject(response);
                if(!data.getBoolean("respuesta"))
                    inicioPresenter.toMainActivity();
                else
                    cargarPerfil(idCliente, context);
            }else
                inicioPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.VALIDAR_TOKEN_SESION + SharedPreferencesManager.getInstance(context).getIDCliente() + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void cargarPerfil(int idCliente, Context context){
        //TODO FLUJO_PLANES: Crear PLAN al iniciar sesión si no existe registro en la BD.
        /*TODO FLUJO_PANES: Cargamos la info de PLAN, MONEDERO Y PERFIL.*/
        DetallesFragment.procedenciaFin = false;

        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    Log.d("TAG2", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        jsonObject = jsonObject.getJSONObject("mensaje");

                        inicioPresenter.setInfoPerfil(jsonObject);

                        PlanesFragment.nombreCliente = jsonObject.getString("nombre");
                        PlanesFragment.correoCliente = jsonObject.getString("correo");
                /*TODO FLUJO_EJECUTAR_PLAN: Verificar si hay PLAN distinto a PARTICULAR
                    -Si, entonces, verificar la expiracion y el monedero (En el servidor verificamos el monedero y  la expiración,
                        y ahí decidimos si finalizamos el plan activo, en cualquier caso regresamos un mensaje validando o avisando que valió verga.
                    -No, entonces, todo sigue el flujo.*/
                        SesionesFragment.PLAN =  jsonObject.getString("tipoPlan");
                        SesionesFragment.MONEDERO = jsonObject.getInt("monedero");
                        SesionesFragment.FECHA_INICIO = jsonObject.getString("fechaInicio");
                        SesionesFragment.FECHA_FIN = jsonObject.getString("fechaFin");

                        setImageBitmap(jsonObject.getString("foto"));
                    }else
                        inicioPresenter.showError("Error del servidor, intente ingresar más tarde.");
                }catch(JSONException ex){
                    ex.printStackTrace();
                    inicioPresenter.showError("Ocurrio un error, intente ingresar más tarde.");
                }
            }else
                inicioPresenter.showError("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.INICIAR_PERFIL_CLIENTE + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
        //getDatosPerfil();
    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            if(response != null)
                inicioPresenter.setFoto(response);
            else
                inicioPresenter.showError("No pudimos cargar la foto de perfil.");
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

}

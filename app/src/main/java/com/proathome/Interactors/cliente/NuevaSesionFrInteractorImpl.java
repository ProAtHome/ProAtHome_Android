package com.proathome.Interactors.cliente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionInteractor;
import com.proathome.Interfaces.cliente.NuevaSesionFragment.NuevaSesionPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;;
import com.proathome.Servicios.cliente.ServiciosCliente;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.MainActivity;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.cliente.fragments.NuevaSesionFragment;
import com.proathome.Views.cliente.fragments.PreOrdenServicio;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class NuevaSesionFrInteractorImpl implements NuevaSesionInteractor {

    private NuevaSesionPresenter nuevaSesionPresenter;

    public NuevaSesionFrInteractorImpl(NuevaSesionPresenter nuevaSesionPresenter){
        this.nuevaSesionPresenter = nuevaSesionPresenter;
    }

    /*
    @Override
    public void getSesionActual(int idCliente, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject rutaJSON = data.getJSONObject("mensaje");
                        int seccion = rutaJSON.getInt("idSeccion");
                        int nivel = rutaJSON.getInt("idNivel");
                        int bloque = rutaJSON.getInt("idBloque");
                        int minutos_horas = rutaJSON.getInt("horas");
                        //ANUNCIO DE REPASO DE LECCIONES POR RUTA FINALIZADA

                        //Iniciamos los adaptadores con el nivel actual TODO ESTARAN EN EL ON_CREATE.
                        nuevaSesionPresenter.setAdapters(seccion, nivel, bloque, minutos_horas);
                        nuevaSesionPresenter.setSeccionesListener(seccion, nivel, bloque, minutos_horas);
                        nuevaSesionPresenter.setNivelesListener(seccion, nivel, bloque, minutos_horas);
                        nuevaSesionPresenter.setBloquesListener(seccion, nivel, bloque, minutos_horas);

                        if(rutaJSON.getBoolean("rutaFinalizada"))
                            nuevaSesionPresenter.setEstatusRutaFinalizada();

                    }else
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(context, "Ocurrio un error, intenta de nuevo mas tarde.", Toast.LENGTH_LONG).show();
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_SESION_ACTUAL + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

    /*
    @Override
    public void validarPlan(int idCliente, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response != null){
                    JSONObject jsonDatos = new JSONObject(response);
                    if(!jsonDatos.getBoolean("respuesta")){
                        SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información.", true, "OK", ()->{
                            SharedPreferencesManager.getInstance(context).logout();
                            context.startActivity(new Intent(context, MainActivity.class));
                        });
                    }else{
                        JSONObject body = jsonDatos.getJSONObject("mensaje");
                        SesionesFragment.PLAN =  body.getString("tipoPlan");
                        SesionesFragment.MONEDERO = body.getInt("monedero");
                        SesionesFragment.FECHA_INICIO = body.getString("fechaInicio");
                        SesionesFragment.FECHA_FIN = body.getString("fechaFin");
                        InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (body.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : body.getString("tipoPlan")));
                        InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " + obtenerHorario(body.getInt("monedero")));
                        InicioCliente.planActivo = body.getString("tipoPlan");
                    }
                }else
                    Toast.makeText(context, "Ocurrio un error, intenta de nuevo mas tarde.", Toast.LENGTH_LONG).show();
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_PLAN + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

    /*
    @Override
    public void validarBanco(int idCliente, Context context) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                if(response != null){
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta")){
                        JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                        if(mensaje.getBoolean("existe")){
                            DetallesFragment.banco = true;
                            //TODO ESTARA EN ON_CREATE
                            nuevaSesionPresenter.setBanco(true);
                            //Datos bancarios Pre Orden.
                            PreOrdenServicio.nombreTitular = mensaje.getString("nombreTitular");
                            PreOrdenServicio.tarjeta = mensaje.get("tarjeta").toString();
                            PreOrdenServicio.mes = mensaje.get("mes").toString();
                            PreOrdenServicio.ano = mensaje.get("ano").toString();
                        }else{
                            nuevaSesionPresenter.setBanco(false);
                            DetallesFragment.banco = false;
                        }
                    }else
                        SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.get("mensaje").toString(), false, null, null);
                }else
                    Toast.makeText(context, "Ocurrio un error, intenta de nuevo mas tarde", Toast.LENGTH_LONG).show();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_DATOS_BANCO_CLIENTE + idCliente + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }*/

    /*
    @Override
    public void guardarPago(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("costoServicio", 0.0);
            jsonObject.put("costoTE", 0.0);
            jsonObject.put("estatusPago", "Pagado");
            jsonObject.put("idCliente", idCliente);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                registrarSesion(bundle, token, idCliente, rutaFinalizada, context);
            }, APIEndPoints.GUARDAR_TOKEN_PAGO, WebServicesAPI.PUT, jsonObject);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void registrarServicio(int idCliente, String token, Bundle bundle, boolean rutaFinalizada, int nuevoMonedero, Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("costoServicio", 0.0);
            jsonObject.put("costoTE", 0.0);
            jsonObject.put("estatusPago", "Pagado");
            jsonObject.put("idCliente", idCliente);
            jsonObject.put("horario", bundle.getString("horario"));
            jsonObject.put("lugar", bundle.getString("lugar"));
            jsonObject.put("tiempo", bundle.getInt("tiempo"));
            jsonObject.put("idSeccion", bundle.getInt("idSeccion"));
            jsonObject.put("idNivel", bundle.getInt("idNivel"));
            jsonObject.put("idBloque", bundle.getInt("idBloque"));
            jsonObject.put("extras", bundle.getString("extras"));
            jsonObject.put("tipoServicio", bundle.getString("tipoServicio"));
            jsonObject.put("latitud", bundle.getDouble("latitud"));
            jsonObject.put("longitud", bundle.getDouble("longitud"));
            jsonObject.put("actualizado", bundle.getString("actualizado"));
            jsonObject.put("fecha",  bundle.getString("fecha"));
            jsonObject.put("sumar", rutaFinalizada ? false : bundle.getBoolean("sumar"));
            jsonObject.put("tipoPlan", bundle.getString("tipoPlan"));
            jsonObject.put("personas", bundle.getInt("personas"));
            jsonObject.put("nuevoMonedero", nuevoMonedero);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    JSONObject respuesta = new JSONObject(response);
                    if(respuesta.getBoolean("respuesta")){
                        JSONObject responseOneProvisional = respuesta.getJSONObject("mensaje");
                        JSONObject body = responseOneProvisional.getJSONObject("mensaje");
                        SesionesFragment.PLAN =  body.getString("tipoPlan");
                        SesionesFragment.MONEDERO = body.getInt("monedero");
                        SesionesFragment.FECHA_INICIO = body.getString("fechaInicio");
                        SesionesFragment.FECHA_FIN = body.getString("fechaFin");
                        InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (body.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : body.getString("tipoPlan")));
                        InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " + obtenerHorario(body.getInt("monedero")));
                        InicioCliente.planActivo = body.getString("tipoPlan");
                        NuevaSesionFragment.nuevoMonedero = 0;

                        SweetAlert.showMsg(context, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", "Servicio registrado exitosamente",
                                true, "¡VAMOS!", ()->{
                                    nuevaSesionPresenter.finishFragment();
                                });
                    }else
                        nuevaSesionPresenter.showError(respuesta.getString("mensaje"));
                }else
                    nuevaSesionPresenter.showError("Ocurrio un error al guardar el servicio, comunica este error al soporte Tecnico.");
            }, APIEndPoints.REGISTRAR_SERVICIO, WebServicesAPI.POST, jsonObject);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void validarEmpalme(int idCliente, String fecha, String horario) {
        JSONObject data = new JSONObject();
        try {
            data.put("idCliente", idCliente);
            data.put("fecha", fecha);
            data.put("horario", horario);
            data.put("reagendar", false);
            data.put("idSesion", 0);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    JSONObject dataResponse = new JSONObject(response);
                    if(dataResponse.getBoolean("respuesta"))
                        nuevaSesionPresenter.validacionPlanes_Ruta();
                    else
                        nuevaSesionPresenter.showError(dataResponse.getString("mensaje"));
                }else
                    nuevaSesionPresenter.showError("Ocurrio un error al validar el servicio, comunica este error al soporte Tecnico.");
            }, APIEndPoints.VALIDAR_EMPALMES, WebServicesAPI.POST, data);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    private void registrarSesion(Bundle bundle, String token, int idCliente, boolean rutaFinalizada, Context context){
        JSONObject parametrosPOST = new JSONObject();
        try {
            parametrosPOST.put("idCliente", idCliente);
            parametrosPOST.put("horario", bundle.getString("horario"));
            parametrosPOST.put("lugar", bundle.getString("lugar"));
            parametrosPOST.put("tiempo", bundle.getInt("tiempo"));
            parametrosPOST.put("idSeccion", bundle.getInt("idSeccion"));
            parametrosPOST.put("idNivel", bundle.getInt("idNivel"));
            parametrosPOST.put("idBloque", bundle.getInt("idBloque"));
            parametrosPOST.put("extras", bundle.getString("extras"));
            parametrosPOST.put("tipoServicio", bundle.getString("tipoServicio"));
            parametrosPOST.put("latitud", bundle.getDouble("latitud"));
            parametrosPOST.put("longitud", bundle.getDouble("longitud"));
            parametrosPOST.put("actualizado", bundle.getString("actualizado"));
            parametrosPOST.put("fecha",  bundle.getString("fecha"));
            //SI LA RUTA YA ESTÁ FINALZADA NO IMPORTA LO QUE VENGA ES FALSE EL SUMAR.
            parametrosPOST.put("sumar", rutaFinalizada ? false : bundle.getBoolean("sumar"));
            parametrosPOST.put("tipoPlan", bundle.getString("tipoPlan"));
            parametrosPOST.put("personas", bundle.getInt("personas"));
            parametrosPOST.put("token", token);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta"))
                    actualizarMonedero(jsonObject, idCliente, context);
                else
                    nuevaSesionPresenter.showError(jsonObject.getString("mensaje"));
            }, APIEndPoints.REGISTRAR_SESION_CLIENTE, WebServicesAPI.POST, parametrosPOST);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    /*
    private void actualizarMonedero(JSONObject jsonObject, int idCliente, Context context) throws JSONException {
        JSONObject parametrosPUT= new JSONObject();
        parametrosPUT.put("idCliente", idCliente);
        parametrosPUT.put("nuevoMonedero", NuevaSesionFragment.nuevoMonedero);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            validarPlan(idCliente, context);
        }, APIEndPoints.ACTUALIZAR_MONEDERO, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();
        NuevaSesionFragment.nuevoMonedero = 0;
        SweetAlert.showMsg(context, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", jsonObject.getString("mensaje"),
                true, "¡VAMOS!", ()->{
                    nuevaSesionPresenter.finishFragment();
                });
    }*/

    public String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

}

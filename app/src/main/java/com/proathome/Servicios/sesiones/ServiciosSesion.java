package com.proathome.Servicios.sesiones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Servicios.cliente.ServiciosCliente;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import com.proathome.Views.cliente.fragments.NuevaSesionFragment;
import com.proathome.Views.cliente.fragments.PreOrdenServicio;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ServiciosSesion {

    private static ProgressDialog progressDialog;

    public static void finalizar(int idSesion, int idPerfil){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.FINALIZAR_SERVICIO + idSesion + "/" + idPerfil, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    public static void validarServicioFinalizadoProfesional(int idSesion, int idPerfil, Context context){
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
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                }catch (JSONException ex){
                    ex.printStackTrace();
                    Toast.makeText(context, "Ocurrio un error, intente de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                }
            }else
                Toast.makeText(context, "Ocurrio un error, intente de nuevo mas tarde.", Toast.LENGTH_LONG).show();
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_PROFESIONAL + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenProfesional(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void validarServicioFinalizadoCliente(int idSesion, int idPerfil, Context context){
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
                        Toast.makeText(context, data.getString("mensaje"), Toast.LENGTH_LONG).show();
                }catch (JSONException ex){
                    ex.printStackTrace();
                    Toast.makeText(context, "Ocurrio un error, intente de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                }
            }else
                Toast.makeText(context, "Ocurrio un error, intente de nuevo mas tarde.", Toast.LENGTH_LONG).show();
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_CLIENTE + idSesion + "/" + idPerfil + "/" + SharedPreferencesManager.getInstance(context).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void cambiarDisponibilidadProfesional(int idSesion, int idPerfil, boolean disponible){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_PROFESIONAL + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    public static void cambiarDisponibilidadCliente(int idSesion, int idPerfil, boolean disponible){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_DISPONIBILIDAD_CLIENTE + idSesion + "/" + idPerfil + "/" + disponible, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    //SÓLO APLICA A PROFESIONAL EL CAMBIO DE ESTATUS.
    public static void cambiarEstatusServicio(int estatus, int idSesion, int idProfesional){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.CAMBIAR_ESTATUS_SERVICIO_PROFESIONAL + idSesion + "/" + idProfesional + "/" + estatus, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    //EL COBRO ES SÓLO A LOS CLIENTES
    public static void cobroServicio(String idCard, String nombreCliente, String correo, double cobro, String descripcion, String deviceID, int idCliente, Bundle bundle, Context context){
        try {
            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("idCard", idCard);
            parametrosPost.put("nombreCliente", nombreCliente);
            parametrosPost.put("correo", correo);
            parametrosPost.put("cobro", formato1.format(cobro));
            parametrosPost.put("descripcion", descripcion);
            parametrosPost.put("deviceId", PreOrdenServicio.deviceID);
            progressDialog = ProgressDialog.show(context, "Generando Cobro", "Por favor, espere...");
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response != null){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        //Guardamos el servicio.
                        if(jsonObject.getBoolean("respuesta"))//Guardamos la info de PAGO
                            registrarServicio(bundle, jsonObject.getString("mensaje"), idCliente, context, cobro, 0.0);
                            //guardarPago(bundle, jsonObject.getString("mensaje"), idCliente, context, cobro, 0.0);
                        else{
                            progressDialog.dismiss();
                            SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrio un error, intente de nuevo mas tarde.", false, null, null);
            }, APIEndPoints.COBROS, WebServicesAPI.POST, parametrosPost);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void registrarServicio(Bundle bundle, String token, int idCliente, Context context, double costoServicio, double costoTE){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("costoServicio", costoServicio);
            jsonObject.put("costoTE", costoTE);
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
            jsonObject.put("sumar", NuevaSesionFragment.rutaFinalizada ? false : bundle.getBoolean("sumar"));
            jsonObject.put("tipoPlan", bundle.getString("tipoPlan"));
            jsonObject.put("personas", bundle.getInt("personas"));
            jsonObject.put("nuevoMonedero", NuevaSesionFragment.nuevoMonedero);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                //registrarSesion(bundle, token, idCliente, context);
                if(response != null){
                    Log.d("TAG1", response);
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject responseOneProvisional = data.getJSONObject("mensaje");
                        JSONObject body = responseOneProvisional.getJSONObject("mensaje");
                        SesionesFragment.PLAN =  body.getString("tipoPlan");
                        SesionesFragment.MONEDERO = body.getInt("monedero");
                        SesionesFragment.FECHA_INICIO = body.getString("fechaInicio");
                        SesionesFragment.FECHA_FIN = body.getString("fechaFin");
                        InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (body.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : body.getString("tipoPlan")));
                        InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " +
                                ServiciosCliente.obtenerHorario(body.getInt("monedero")));
                        InicioCliente.planActivo = body.getString("tipoPlan");
                        //EL FLUJO DE COBRO SERVICIO LLEGA HASTA ACA, CERRAMEOS EL DIALOG QUE LO GENERO.
                        progressDialog.dismiss();
                        progressDialog = null;
                        NuevaSesionFragment.nuevoMonedero = 0;

                        SweetAlert.showMsg(context, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", "Servicio registrado exitosamente", true, "¡VAMOS!", ()->{
                            PreOrdenServicio.dialogFragment.dismiss();
                            NuevaSesionFragment.dialogFragment.dismiss();
                        });

                        //actualizarMonedero(data, idCliente, context);
                    }else
                        SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
                }else
                    SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrio un error, intente de nuevo mas tarde.", false, null, null);
            }, APIEndPoints.REGISTRAR_SERVICIO, WebServicesAPI.POST, jsonObject);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Ocurrio un error, intenta de nuevo mas tarde", Toast.LENGTH_LONG).show();
        }
    }

    /*
    private static void guardarPago(Bundle bundle, String token, int idCliente, Context context, double costoServicio, double costoTE){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("costoServicio", costoServicio);
            jsonObject.put("costoTE", costoTE);
            jsonObject.put("estatusPago", "Pagado");
            jsonObject.put("idCliente", idCliente);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                registrarSesion(bundle, token, idCliente, context);
            }, APIEndPoints.GUARDAR_TOKEN_PAGO, WebServicesAPI.PUT, jsonObject);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    /*
    private static void registrarSesion(Bundle bundle, String token, int idCliente, Context context){
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
            parametrosPOST.put("sumar", NuevaSesionFragment.rutaFinalizada ? false : bundle.getBoolean("sumar"));
            parametrosPOST.put("tipoPlan", bundle.getString("tipoPlan"));
            parametrosPOST.put("personas", bundle.getInt("personas"));
            parametrosPOST.put("token", token);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                JSONObject jsonObject = new JSONObject(response);
                progressDialog.dismiss();
                progressDialog = null;
                if(jsonObject.getBoolean("respuesta")){
                    //EL FLUJO DE COBRO SERVICIO LLEGA HASTA ACA, CERRAMEOS EL DIALOG QUE LO GENERO.
                    PreOrdenServicio.dialogFragment.dismiss();
                    actualizarMonedero(jsonObject, idCliente, context);
                }else
                    SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", jsonObject.getString("mensaje"), false, null, null);
            }, APIEndPoints.REGISTRAR_SESION_CLIENTE, WebServicesAPI.POST, parametrosPOST);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarMonedero(JSONObject jsonObject, int idCliente, Context contexto) throws JSONException {
        JSONObject parametrosPUT= new JSONObject();
        parametrosPUT.put("idCliente", idCliente);
        parametrosPUT.put("nuevoMonedero", NuevaSesionFragment.nuevoMonedero);
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            ServiciosCliente.validarPlan(idCliente, contexto);
        }, APIEndPoints.ACTUALIZAR_MONEDERO, WebServicesAPI.PUT, parametrosPUT);
        webServicesAPI.execute();

        NuevaSesionFragment.nuevoMonedero = 0;
        SweetAlert.showMsg(contexto, SweetAlert.SUCCESS_TYPE, "¡GENIAL!", jsonObject.getString("mensaje"), true, "¡VAMOS!", ()->{
            NuevaSesionFragment.dialogFragment.dismiss();
        });
    }*/

}

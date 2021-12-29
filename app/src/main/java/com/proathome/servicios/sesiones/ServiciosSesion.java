package com.proathome.servicios.sesiones;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.ServiciosCliente;
import com.proathome.ui.ServicioCliente;
import com.proathome.ui.ServicioProfesional;
import com.proathome.ui.SincronizarServicio;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.ui.fragments.NuevaSesionFragment;
import com.proathome.ui.fragments.PreOrdenServicio;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ServiciosSesion {

    public static void finalizar(int idSesion, int idPerfil){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.FINALIZAR_SERVICIO + idSesion + "/" + idPerfil, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    public static void validarServicioFinalizadoProfesional(int idSesion, int idPerfil){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                boolean finalizado = jsonObject.getBoolean("finalizado");

                if(finalizado){
                    DetallesSesionProfesionalFragment.iniciar.setEnabled(false);
                    DetallesSesionProfesionalFragment.iniciar.setText("Servicio finalizado");
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_PROFESIONAL + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void validarServicioFinalizadoCliente(int idSesion, int idPerfil){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                boolean finalizado = jsonObject.getBoolean("finalizado");

                if(finalizado){
                    DetallesFragment.iniciar.setEnabled(false);
                    DetallesFragment.iniciar.setText("Servicio finalizado");
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_CLIENTE + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void validarServicioFinalizadoEnClase(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                boolean finalizado = jsonObject.getBoolean("finalizado");
                if(finalizado){
                    Constants.fragmentActivity.finish();
                    DetallesSesionProfesionalFragment.procedenciaFin = true;
                }else
                    SweetAlert.showMsg(context, SweetAlert.WARNING_TYPE, "¡ESPERA!", "El cliente todavia no decide si tomar tiempo extra o finalizar el servicio.", false, null, null);
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_SERVICIO_FINALIZADO_PROFESIONAL + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void verificarDisponibilidadProfesional(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject resultado = new JSONObject(response);
                boolean disponibilidad = resultado.getBoolean("dispProfesional");
                if(disponibilidad) {
                    SincronizarServicio.timer.cancel();
                    getProgresoCliente(idSesion, idPerfil, context);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, APIEndPoints.VERIFICAR_DISPONIBILIDAD_DE_PROFESIONAL + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void verificarDisponibilidadCliente(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject resultado = new JSONObject(response);
                boolean disponibilidad = resultado.getBoolean("dispCliente");
                if(disponibilidad) {
                    SincronizarServicio.timer.cancel();
                    ServiciosSesion.getProgresoProfesional(idSesion, idPerfil, context);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, APIEndPoints.VERIFICAR_DISPONIBILIDAD_DE_CLIENTE + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
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

    public static void getProgresoCliente(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
            /*Aquí vamos a obtener el progreso inicial de el servicio dependiendo el perfil PROFESIONAL o CLIENTE para iniciar
             las variables correspondientes antes de entrar en la Actividad de el Servicio.*/
                JSONObject jsonObject = new JSONObject(response);
                Intent intent = new Intent(context, ServicioCliente.class);
                if(jsonObject.getBoolean("TE"))
                    ServicioCliente.mTimeLeftMillis = (jsonObject.getInt("progresoTE") * 60 * 1000) + (jsonObject.getInt("progresoSegundosTE") * 1000);
                else
                    ServicioCliente.mTimeLeftMillis = (jsonObject.getInt("progreso") * 60 * 1000) + (jsonObject.getInt("progresoSegundos") * 1000);

                intent.putExtra("idSesion", idSesion);
                intent.putExtra("idCliente", idPerfil);
                intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                intent.putExtra("idNivel", SincronizarServicio.idNivel);
                intent.putExtra("idBloque", SincronizarServicio.idBloque);
                intent.putExtra("sumar", SincronizarServicio.sumar);
                intent.putExtra("tiempo", SincronizarServicio.tiempo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                jsonObject = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, APIEndPoints.GET_PROGRESO_CLIENTE + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void getProgresoProfesional(int idSesion, int idPerfil, Context context){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
            /*Aquí vamos a obtener el progreso inicial de el servicio dependiendo el perfil PROFESIONAL o CLIENTE para iniciar
             las variables correspondientes antes de entrar en la Actividad de el Servicio.*/
                JSONObject jsonObject = new JSONObject(response);
                Intent intent = new Intent(context, ServicioProfesional.class);
                if(jsonObject.getBoolean("TE"))
                    ServicioProfesional.mTimeLeftMillis = (jsonObject.getInt("progresoTE") * 60 * 1000) + (jsonObject.getInt("progresoSegundosTE") * 1000);
                else
                    ServicioProfesional.mTimeLeftMillis = (jsonObject.getInt("progreso") * 60 * 1000) + (jsonObject.getInt("progresoSegundos") * 1000);

                intent.putExtra("idSesion", idSesion);
                intent.putExtra("idProfesional", idPerfil);
                intent.putExtra("idSeccion", SincronizarServicio.idSeccion);
                intent.putExtra("idNivel", SincronizarServicio.idNivel);
                intent.putExtra("idBloque", SincronizarServicio.idBloque);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                jsonObject = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, APIEndPoints.GET_PROGRESO_PROFESIONAL + idSesion + "/" + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void guardarProgreso(int idSesion, int idPerfil, int progreso, int progresoSegundos, int tipoDeTiempo){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
        }, APIEndPoints.GUARDAR_PROGRESO_SERVICIO_PROFESIONAL + idSesion + "/" + idPerfil + "/" + progreso + "/" + progresoSegundos + "/" + tipoDeTiempo, WebServicesAPI.PUT, null);
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

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    //Guardamos el servicio.
                    if(jsonObject.getBoolean("respuesta"))//Guardamos la info de PAGO
                        guardarPago(bundle, jsonObject.getString("mensaje"), idCliente, context);
                    else
                        SweetAlert.showMsg(context, SweetAlert.ERROR_TYPE, "¡ERROR!", response, false, null, null);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.COBROS, WebServicesAPI.POST, parametrosPost);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void guardarPago(Bundle bundle, String token, int idCliente, Context context){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("costoServicio", 0.0);
            jsonObject.put("costoTE", 0.0);
            jsonObject.put("estatusPago", "Pagado");
            jsonObject.put("idCliente", idCliente);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                registrarSesion(bundle, token, idCliente, context);
            }, APIEndPoints.GUARDAR_TOKEN_PAGO, WebServicesAPI.PUT, jsonObject);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
            parametrosPOST.put("sumar", bundle.getBoolean("sumar"));
            parametrosPOST.put("tipoPlan", bundle.getString("tipoPlan"));
            parametrosPOST.put("personas", bundle.getInt("personas"));
            parametrosPOST.put("token", token);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                JSONObject jsonObject = new JSONObject(response);
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
    }

}

package com.proathome.Servicios.cliente;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Views.cliente.InicioCliente;
import com.proathome.Views.cliente.MainActivity;
import com.proathome.Views.cliente.fragments.NuevaSesionFragment;
import com.proathome.Views.cliente.fragments.PlanesFragment;
import com.proathome.Views.cliente.navigator.sesiones.SesionesFragment;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiciosCliente {

    public static int NUEVA_SESION_FRAGMENT = 1, PLANES_FRAGMENT = 2;

    public static void validarExpiracionServicio(String fechaServidor, String fechaServicio, Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateServidor = sdf.parse(fechaServidor);
            Date dateServicio = sdf.parse(fechaServicio);

            if(dateServidor.after(dateServicio))
                SweetAlert.showMsg(context, SweetAlert.WARNING_TYPE, "¡AVISO!", "Esta sesión requerie que actualices la fecha.", false, null, null);
            else if(dateServidor.equals(dateServicio))
                SweetAlert.showMsg(context, SweetAlert.WARNING_TYPE, "¡AVISO!", "Esta sesión tiene fecha límite de hoy, si no se te asigna un profesor a tiempo comunicate con nosotros.", false, null, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void validarPlan(int idCliente, Context contexto){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonDatos = new JSONObject(response);
                Log.d("TAG1PLAN", jsonDatos.toString());
                if(!jsonDatos.getBoolean("respuesta")){
                    SweetAlert.showMsg(contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información.", true, "OK", ()->{
                        SharedPreferencesManager.getInstance(contexto).logout();
                        contexto.startActivity(new Intent(contexto, MainActivity.class));
                    });
                }else{
                    JSONObject body = jsonDatos.getJSONObject("mensaje");
                    SesionesFragment.PLAN =  body.getString("tipoPlan");
                    SesionesFragment.MONEDERO = body.getInt("monedero");
                    SesionesFragment.FECHA_INICIO = body.getString("fechaInicio");
                    SesionesFragment.FECHA_FIN = body.getString("fechaFin");
                    InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (body.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : body.getString("tipoPlan")));
                    InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " +
                            obtenerHorario(body.getInt("monedero")));
                    InicioCliente.planActivo = body.getString("tipoPlan");
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_PLAN + idCliente + "/" + SharedPreferencesManager.getInstance(contexto).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static void getSesionActual(int tipoSolicitud, int idCliente, Context contexto){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject data = new JSONObject(response);
                if(data.getBoolean("respuesta")){
                    JSONObject rutaJSON = data.getJSONObject("mensaje");
                    int seccion = rutaJSON.getInt("idSeccion");
                    int nivel = rutaJSON.getInt("idNivel");
                    int bloque = rutaJSON.getInt("idBloque");
                    int minutos_horas = rutaJSON.getInt("horas");
                    //ANUNCIO DE REPASO DE LECCIONES POR RUTA FINALIZADA

                    if(tipoSolicitud == PLANES_FRAGMENT){
                        PlanesFragment.idSeccion = seccion;
                        PlanesFragment.idNivel = nivel;
                        PlanesFragment.idBloque = bloque;
                    }
                }else
                    Toast.makeText(contexto, data.getString("mensaje"), Toast.LENGTH_LONG).show();
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_SESION_ACTUAL + idCliente + "/" + SharedPreferencesManager.getInstance(contexto).getTokenCliente(), WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public static String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    public static void avisoContenidoRuta(Context context){
        new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("¡AVISO!")
                .setMessage("Los presentes temas son una sugerencia y meramente informativos o de guía para el cliente y el profesional," +
                        " en ningún momento es obligatorio seguir dichos temas," +
                        " PRO AT HOME de ninguna forma sugiere o indica que estos son o serán parte de sus políticas o contenido por el cual" +
                        " recibe un pago, toda vez que este es por el almacenamiento de datos, cuentas, enlaces con clientes y profesionales," +
                        " así como por el uso y mantenimiento de la plataforma.")
                .setPositiveButton("Entendido", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

}

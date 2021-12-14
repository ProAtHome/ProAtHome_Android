package com.proathome.servicios.cliente;

import android.content.Context;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.InicioCliente;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiciosCliente {

    public static void validarPlan(int idCliente, Context contexto){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                if(response == null){
                    new SweetAlert(contexto, SweetAlert.ERROR_TYPE, SweetAlert.CLIENTE)
                            .setTitleText("¡ERROR!")
                            .setContentText("Error al obtener la información.")
                            .show();
                }else{
                    JSONObject jsonDatos = new JSONObject(response);
                    SesionesFragment.PLAN =  jsonDatos.getString("tipoPlan");
                    SesionesFragment.MONEDERO = jsonDatos.getInt("monedero");
                    SesionesFragment.FECHA_INICIO = jsonDatos.getString("fechaInicio");
                    SesionesFragment.FECHA_FIN = jsonDatos.getString("fechaFin");
                    InicioCliente.tipoPlan.setText("PLAN ACTUAL: " + (jsonDatos.getString("tipoPlan").equalsIgnoreCase("PARTICULAR_PLAN") ? "PARTICULAR" : jsonDatos.getString("tipoPlan")));
                    InicioCliente.monedero.setText("HORAS DISPONIBLES:                      " +
                            obtenerHorario(jsonDatos.getInt("monedero")));
                    InicioCliente.planActivo = jsonDatos.getString("tipoPlan").toString();
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.VALIDAR_PLAN + idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }


    public static String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

}

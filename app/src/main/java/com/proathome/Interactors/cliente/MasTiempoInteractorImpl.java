package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoInteractor;
import com.proathome.Interfaces.cliente.MasTiempo.MasTiempoPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.FechaActual;
import com.proathome.Utils.pojos.Component;
import com.proathome.Views.cliente.ServicioCliente;
import com.proathome.Views.cliente.fragments.CobroFinalFragment;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class MasTiempoInteractorImpl implements MasTiempoInteractor {

    private MasTiempoPresenter masTiempoPresenter;

    public MasTiempoInteractorImpl(MasTiempoPresenter masTiempoPresenter){
        this.masTiempoPresenter = masTiempoPresenter;
    }

    @Override
    public void getPreOrden(int idCliente, int idSesion, String token) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                try{
                    JSONObject data = new JSONObject(response);
                    if(data.getBoolean("respuesta")){
                        JSONObject jsonObject = data.getJSONObject("mensaje");
                        CobroFinalFragment.nombreTitular = jsonObject.getString("nombreTitular");
                        CobroFinalFragment.mes = jsonObject.get("mes").toString();
                        CobroFinalFragment.ano = jsonObject.get("ano").toString();
                        CobroFinalFragment.metodoRegistrado = jsonObject.getString("tarjeta");
                        CobroFinalFragment.sesion = "Sesión: " + Component.getSeccion(jsonObject.getInt("idSeccion")) + " / " + Component.getNivel(jsonObject.getInt("idSeccion"), jsonObject.getInt("idNivel")) + " / " + Component.getBloque(jsonObject.getInt("idBloque"));
                        CobroFinalFragment.tiempo = "Tiempo: " + obtenerHorario(jsonObject.getInt("tiempo"));
                        CobroFinalFragment.nombreCliente = jsonObject.get("nombreCliente").toString();
                        CobroFinalFragment.correo = jsonObject.get("correo").toString();
                    }else
                        masTiempoPresenter.showError(data.getString("mensaje"));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    masTiempoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
                }
            }else
                masTiempoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }, APIEndPoints.GET_PRE_ORDEN + idCliente + "/" + idSesion + "/" + token, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    @Override
    public void generarOrdenPago() {
        JSONObject jsonDatos = new JSONObject();
        try {
            jsonDatos.put("costoServicio", 0);
            jsonDatos.put("costoTE", 0);
            //jsonDatos.put("idCliente", this.idCliente);
            //jsonDatos.put("idSesion", this.idSesion);
            jsonDatos.put("estatusPago", "Pagado");
            jsonDatos.put("tipoPlan", DetallesFragment.planSesion);

            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

            }, APIEndPoints.ORDEN_COMPRA_ACTUALIZAR_PAGO, WebServicesAPI.PUT, jsonDatos);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            masTiempoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
        }
    }

    @Override
    public void finalizar(int idCliente, int idSesion) {
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            sumarSevicioRuta(idCliente, idSesion);
        }, APIEndPoints.FINALIZAR_SERVICIO + idSesion + "/" + idCliente, WebServicesAPI.PUT, null);
        webServicesAPI.execute();
    }

    private void sumarSevicioRuta(int idCliente, int idSesion){
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("idCliente", idCliente);
            parametros.put("idSesion", idSesion);
            parametros.put("idSeccion", ServicioCliente.idSeccion);
            parametros.put("idNivel",  ServicioCliente.idNivel);
            parametros.put("idBloque", ServicioCliente.idBloque);
            parametros.put("horasA_sumar", ServicioCliente.tiempo);
            parametros.put("fecha_registro", FechaActual.getFechaActual());
            parametros.put("sumar", ServicioCliente.sumar);
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                //TODO AQUI PODEMOS PONER UN ANUNCIO DE FIN DE RUTA CUANDO LA VAR ultimaSesion SEA TRUE.
                DetallesFragment.procedenciaFin = true;
                masTiempoPresenter.cerrarFragment();
            }, APIEndPoints.SUMAR_SERVICIO_RUTA, WebServicesAPI.POST, parametros);
            webServicesAPI.execute();
        } catch (JSONException e) {
            masTiempoPresenter.showError("Ocurrio un error, intente de nuevo mas tarde.");
            e.printStackTrace();
        }
    }

    public String obtenerHorario(int tiempo){
        String horas = (tiempo/60) + " HRS ";
        String minutos = (tiempo%60) + " min";

        return horas + minutos;
    }

}

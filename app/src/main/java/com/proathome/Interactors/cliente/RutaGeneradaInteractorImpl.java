package com.proathome.Interactors.cliente;

import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaInteractor;
import com.proathome.Interfaces.cliente.RutaGenerada.RutaGeneradaPresenter;
import com.proathome.Servicios.api.APIEndPoints;
import com.proathome.Servicios.api.WebServicesAPI;
import com.proathome.Utils.FechaActual;
import com.proathome.Views.cliente.examen.EvaluarRuta;
import org.json.JSONException;
import org.json.JSONObject;

public class RutaGeneradaInteractorImpl implements RutaGeneradaInteractor {

    private RutaGeneradaPresenter rutaGeneradaPresenter;

    public RutaGeneradaInteractorImpl(RutaGeneradaPresenter rutaGeneradaPresenter){
        this.rutaGeneradaPresenter = rutaGeneradaPresenter;
    }

    @Override
    public void rutaExamen(EvaluarRuta evaluarRuta, int idCliente) {
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("idCliente", idCliente);
            parametros.put("idSeccion", evaluarRuta.getSeccion());
            parametros.put("idNivel", evaluarRuta.getNivel());
            parametros.put("idBloque", evaluarRuta.getBloque());
            parametros.put("horas", 0);
            parametros.put("fecha_registro", FechaActual.getFechaActual());
            parametros.put("sumar", true);

            rutaGeneradaPresenter.showProgress();
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                rutaGeneradaPresenter.hideProgress();
                rutaGeneradaPresenter.cerrarFragment();
            }, APIEndPoints.NUEVA_RUTA_EXAMEN, WebServicesAPI.POST, parametros);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

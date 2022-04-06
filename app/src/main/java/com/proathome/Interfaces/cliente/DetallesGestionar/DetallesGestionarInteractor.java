package com.proathome.Interfaces.cliente.DetallesGestionar;

import android.content.Context;
import org.json.JSONObject;

public interface DetallesGestionarInteractor {

    void getFechaServidor(String fechaSesion, Context context);

    void actualizarSesion(JSONObject jsonObject);

    void eliminarSesion(JSONObject jsonObject);

    void notificarProfesional(JSONObject jsonObject);

    void validarEmpalme(int idCliente, String fecha, String horario, boolean cambioFecha, int idSesion);

}

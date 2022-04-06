package com.proathome.Interfaces.cliente.DetallesGestionar;

import android.content.Context;
import org.json.JSONObject;

public interface DetallesGestionarPresenter {

    void getFechaServidor(String fechaSesion, Context context);

    void setFechaServidor(String fecha);

    void actualizarSesion(JSONObject jsonObject);

    void eliminarSesion(JSONObject jsonObject);

    void showMsg(int tipo, String titulo, String mensaje);

    void successDelete(String mensaje);

    void successUpdate(String mensaje);

    void notificarProfesional(JSONObject jsonObject);

    void closeFragment();

    void validarEmpalme(int idCliente, String fecha, String horario, boolean cambioFecha, int idSesion);

    void update(boolean cambioFecha);

}

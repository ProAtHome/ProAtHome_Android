package com.proathome.servicios.api;

import com.proathome.utils.Constants;

public class APIEndPoints {

    public static final String LATIDO_SQL = Constants.IP + "/ProAtHome/apiProAtHome/admin/latidoSQL";
    public static final String INICIAR_SESION_ESTUDIANTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/sesionCliente";
    public static final String REGISTRAR_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/agregarCliente";
    public static final String INICIAR_PROCESO_RUTA = Constants.IP + "/ProAtHome/apiProAtHome/cliente/iniciarProcesoRuta";
    public static final String ACTUALIZAR_PERFIL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarPerfil";
    public static final String BLOQUEAR_PERFIL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/bloquearPerfil";

    public static final String VERIFICACION_CORREO = Constants.IP_80 + "/assets/lib/Verificacion.php?enviar=true";
}

package com.proathome.servicios.api;

import com.proathome.utils.Constants;

public class APIEndPoints {

    public static final String LATIDO_SQL = Constants.IP + "/ProAtHome/apiProAtHome/admin/latidoSQL";
    public static final String INICIAR_SESION_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/sesionCliente";
    public static final String REGISTRAR_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/agregarCliente";
    public static final String INICIAR_PROCESO_RUTA = Constants.IP + "/ProAtHome/apiProAtHome/cliente/iniciarProcesoRuta";
    public static final String ACTUALIZAR_PERFIL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarPerfil";
    public static final String BLOQUEAR_PERFIL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/bloquearPerfil";
    public static final String ACTUALIZAR_MONEDERO = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarMonedero";
    public static final String DISPONIBILIDAD_SERVICIO = Constants.IP + "/ProAtHome/apiProAtHome/cliente/getDisponibilidadServicio/";
    public static final String ELIMINAR_SESION = Constants.IP + "/ProAtHome/apiProAtHome/cliente/eliminarSesion";
    public static final String GENERAR_PLAN = Constants.IP + "/ProAtHome/apiProAtHome/cliente/generarPlan";
    public static final String NUEVA_RUTA_EXAMEN = Constants.IP + "/ProAtHome/apiProAtHome/cliente/nuevaRuta";

    public static final String COBROS = Constants.IP_80 + "/assets/lib/Cargo.php";
    public static final String VERIFICACION_CORREO = Constants.IP_80 + "/assets/lib/Verificacion.php?enviar=true";
}

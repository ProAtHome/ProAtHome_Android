package com.proathome.servicios.api;

import com.proathome.utils.Constants;

public class APIEndPoints {

    //SQL
    public static final String LATIDO_SQL = Constants.IP + "/ProAtHome/apiProAtHome/admin/latidoSQL";

    //CLIENTES
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
    public static final String ACTUALIZAR_PAGO_TE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarPagoTE";
    public static final String UPDATE_CUENTA_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarCuentaCliente";
    public static final String REGISTRAR_SESION_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/agregarSesion";
    public static final String SESIONES_PAGADAS_Y_FINALIZADAS = Constants.IP + "/ProAtHome/apiProAtHome/cliente/verificarSesionesPagadas/";
    public static final String GUARDAR_TOKEN_PAGO = Constants.IP + "/ProAtHome/apiProAtHome/cliente/guardarTokenPagoServicio";
    public static final String ACTUALIZAR_SESION = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarSesion";
    public static final String SALDAR_DEUDA = Constants.IP + "/ProAtHome/apiProAtHome/cliente/saldarDeuda";
    public static final String VALIDAR_PLAN = Constants.IP + "/ProAtHome/apiProAtHome/cliente/verificarPlan/";
    public static final String FOTO_PERFIL = Constants.IP_80 + "/assets/img/fotoPerfil/";
    public static final String GET_DATOS_FISCALES_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/getDatosFiscales/";
    public static final String UPDATE_DATOS_FISCALES_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/guardarDatosFiscales";
    public static final String SUMAR_SERVICIO_RUTA = Constants.IP + "/ProAtHome/apiProAtHome/cliente/sumarServicioRuta";
    public static final String GET_VALORACION_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/obtenerValoracion/";
    public static final String VALORAR_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/valorarProfesional";
    public static final String VALIDAR_VALORACION_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/validarValoracion/";
    public static final String ORDEN_COMPRA_PAGO_INICIAL = Constants.IP + "/ProAtHome/apiProAtHome/cliente/pagoInicial";
    public static final String ORDEN_COMPRA_ACTUALIZAR_PAGO = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarPago";
    public static final String GET_TICKETS_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/obtenerTickets/";
    public static final String ENVIAR_MSG_TICKET_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/enviarMsgTicket";
    public static final String NUEVO_TICKET_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/nuevoTicket";
    public static final String FINALIZAR_TICKET_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/finalizarTicket/";
    public static final String GET_PERFIL_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/perfilCliente/";
    public static final String GET_SESIONES_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    public static final String GET_PRE_ORDEN = Constants.IP + "/ProAtHome/apiProAtHome/cliente/obtenerPreOrden/";
    public static final String GET_REPORTES_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/cliente/getReportes/";

    //PROFESIONALES
    public static final String DATOS_BANCARIOS_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/obtenerDatosBancarios/";
    public static final String MATCH_SESION = Constants.IP + "/ProAtHome/apiProAtHome/profesional/matchSesion";
    public static final String UPDATE_CUENTA_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/actualizarCuenta";
    public static final String ACTUALIZAR_PERFIL_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/actualizarPerfil";
    public static final String INFO_SESION_MATCH = Constants.IP + "/ProAtHome/apiProAtHome/profesional/informacionSesionMatch/";
    public static final String INICIAR_SESION_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/sesionProfesional/";
    public static final String REGISTRAR_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/agregarProfesional";
    public static final String GET_DATOS_FISCALES_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/getDatosFiscales/";
    public static final String UPDATE_DATOS_FISCALES_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/guardarDatosFiscales";
    public static final String BUSCAR_SESIONES = Constants.IP + "/ProAtHome/apiProAtHome/profesional/obtenerSesionesMovil/";
    public static final String GET_VALORACION_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/profesional/obtenerValoracion/";
    public static final String VALORAR_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/profesional/valorarCliente";
    public static final String VALIDAR_VALORACION_CLIENTE = Constants.IP + "/ProAtHome/apiProAtHome/profesional/validarValoracion/";
    public static final String GET_TICKETS_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/obtenerTickets/";
    public static final String ENVIAR_MSG_TICKET_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/enviarMsgTicket";
    public static final String NUEVO_TICKET_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/nuevoTicket";
    public static final String FINALIZAR_TICKET_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/finalizarTicket/";
    public static final String GET_PERFIL_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/perfilProfesional/";
    public static final String UP_FOTO_PROFESIONAL = Constants.IP_80 + "/assets/lib/ActualizarFotoProfesionalAndroid.php";
    public static final String GET_SESIONES_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/obtenerSesionesProfesionalMatch/";
    public static final String GET_REPORTES_PROFESIONAL = Constants.IP + "/ProAtHome/apiProAtHome/profesional/getReportes/";

    //GENERAL
    public static final String COBROS = Constants.IP_80 + "/assets/lib/Cargo.php";
    public static final String VERIFICACION_CORREO = Constants.IP_80 + "/assets/lib/Verificacion.php?enviar=true";
    public static final String FECHA_SERVIDOR = Constants.IP + "/ProAtHome/apiProAtHome/admin/fechaServidor";
    public static final String GET_MSG_TICKET = Constants.IP + "/ProAtHome/apiProAtHome/admin/obtenerMsgTicket/";
}

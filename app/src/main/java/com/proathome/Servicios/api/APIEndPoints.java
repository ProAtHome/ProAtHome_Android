package com.proathome.Servicios.api;

import com.proathome.Utils.Constants;

public class APIEndPoints {

    //SQL
    public static final String LATIDO_SQL = Constants.IP + "/ProAtHome/api/admin/latidoSQL";

    //CLIENTES
    public static final String NOTIFICACION_PROFESIONAL = Constants.IP_80 + "/assets/lib/NotificacionProfesional.php";
    public static final String VALIDAR_EMPALMES = Constants.IP + "/ProAtHome/api/cliente/validarEmpalme";
    public static final String VALIDAR_TOKEN_SESION = Constants.IP + "/ProAtHome/api/cliente/validarTokenSesion/";
    public static final String INICIAR_SESION_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/sesionCliente";
    public static final String REGISTRAR_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/agregarCliente";
    public static final String INICIAR_PROCESO_RUTA = Constants.IP + "/ProAtHome/api/cliente/iniciarProcesoRuta";
    public static final String ACTUALIZAR_PERFIL = Constants.IP + "/ProAtHome/api/cliente/actualizarPerfil";
    public static final String BLOQUEAR_PERFIL = Constants.IP + "/ProAtHome/api/cliente/bloquearPerfil";
    public static final String ACTUALIZAR_MONEDERO = Constants.IP + "/ProAtHome/api/cliente/actualizarMonedero";
    public static final String DISPONIBILIDAD_SERVICIO = Constants.IP + "/ProAtHome/api/cliente/getDisponibilidadServicio/";
    public static final String ELIMINAR_SESION = Constants.IP + "/ProAtHome/api/cliente/eliminarSesion";
    public static final String GENERAR_PLAN = Constants.IP + "/ProAtHome/api/cliente/generarPlan";
    public static final String NUEVA_RUTA_EXAMEN = Constants.IP + "/ProAtHome/api/cliente/nuevaRuta";
    public static final String ACTUALIZAR_PAGO_TE = Constants.IP + "/ProAtHome/api/cliente/actualizarPagoTE";
    public static final String UPDATE_CUENTA_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/actualizarCuentaCliente";
    public static final String REGISTRAR_SESION_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/agregarSesion";
    public static final String SESIONES_PAGADAS_Y_FINALIZADAS = Constants.IP + "/ProAtHome/api/cliente/verificarSesionesPagadas/";
    public static final String GUARDAR_TOKEN_PAGO = Constants.IP + "/ProAtHome/api/cliente/guardarTokenPagoServicio";
    public static final String ACTUALIZAR_SESION = Constants.IP + "/ProAtHome/api/cliente/actualizarSesion";
    public static final String SALDAR_DEUDA = Constants.IP + "/ProAtHome/api/cliente/saldarDeuda";
    public static final String VALIDAR_PLAN = Constants.IP + "/ProAtHome/api/cliente/verificarPlan/";
    public static final String GET_DATOS_FISCALES_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/getDatosFiscales/";
    public static final String UPDATE_DATOS_FISCALES_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/guardarDatosFiscales";
    public static final String SUMAR_SERVICIO_RUTA = Constants.IP + "/ProAtHome/api/cliente/sumarServicioRuta";
    public static final String GET_VALORACION_PROFESIONAL = Constants.IP + "/ProAtHome/api/cliente/obtenerValoracion/";
    public static final String VALORAR_PROFESIONAL = Constants.IP + "/ProAtHome/api/cliente/valorarProfesional";
    public static final String VALIDAR_VALORACION_PROFESIONAL = Constants.IP + "/ProAtHome/api/cliente/validarValoracion/";
    public static final String ORDEN_COMPRA_PAGO_INICIAL = Constants.IP + "/ProAtHome/api/cliente/pagoInicial";
    public static final String ORDEN_COMPRA_ACTUALIZAR_PAGO = Constants.IP + "/ProAtHome/api/cliente/actualizarPago";
    public static final String GET_TICKETS_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/obtenerTickets/";
    public static final String ENVIAR_MSG_TICKET_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/enviarMsgTicket";
    public static final String NUEVO_TICKET_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/nuevoTicket";
    public static final String FINALIZAR_TICKET_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/finalizarTicket/";
    public static final String GET_PERFIL_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/perfilCliente/";
    public static final String INICIAR_PERFIL_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/iniciarPerfil/";
    public static final String GET_SESIONES_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/obtenerSesiones/";
    public static final String GET_PRE_ORDEN = Constants.IP + "/ProAtHome/api/cliente/obtenerPreOrden/";
    public static final String GET_REPORTES_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/getReportes/";
    public static final String GET_DATOS_BANCO_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/obtenerDatosBancarios/";
    public static final String GET_ESTADO_RUTA = Constants.IP + "/ProAtHome/api/cliente/estadoRutaAprendizaje/";
    public static final String GET_SESION_ACTUAL = Constants.IP + "/ProAtHome/api/cliente/obtenerSesionActual/";
    public static final String REINICIAR_EXAMEN_DIAGNOSTICO = Constants.IP + "/ProAtHome/api/cliente/reiniciarExamenDiagnostico";
    public static final String GET_ESTATUS_EXAMEN_DIAGNOSTICO = Constants.IP + "/ProAtHome/api/cliente/estatusExamenDiagnostico/";
    public static final String GET_INFO_EXAMEN_FINAL = Constants.IP + "/ProAtHome/api/cliente/infoExamenDiagnosticoFinal/";
    public static final String EN_CURSO_EXAMEN_DIAGNOSTICO = Constants.IP + "/ProAtHome/api/cliente/enCursoExamenDiagnostico";
    public static final String INICIO_O_CANCELAR_EXAMEN_DIAGNOSTICO = Constants.IP + "/ProAtHome/api/cliente/examenDiagnostico";
    public static final String GET_INFO_EXAMEN_DIAGNOSTICO = Constants.IP + "/ProAtHome/api/cliente/infoExamenDiagnostico/";
    public static final String ACTIVAR_TIEMPO_EXTRA = Constants.IP + "/ProAtHome/api/cliente/activarTE/";
    public static final String GET_PROGRESO_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/validarEstatusServicio/";
    public static final String CAMBIAR_DISPONIBILIDAD_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/servicioDisponible/";
    public static final String VERIFICAR_DISPONIBILIDAD_DE_PROFESIONAL =  Constants.IP + "/ProAtHome/api/cliente/sincronizarServicio/";
    public static final String VALIDAR_SERVICIO_FINALIZADO_CLIENTE = Constants.IP + "/ProAtHome/api/cliente/validarServicioFinalizada/";
    public static final String FINALIZAR_SERVICIO = Constants.IP + "/ProAtHome/api/cliente/finalizarServicio/";
    public static final String REGISTRAR_SERVICIO = Constants.IP + "/ProAtHome/api/cliente/registrarServicio";
    public static final String GET_INFO_INICIO_SESIONES = Constants.IP + "/ProAtHome/api/cliente/getInicioSesion/";

    //PROFESIONALES
    public static final String NOTIFICACION_CLIENTE = Constants.IP_80 + "/assets/lib/NotificacionCliente.php";
    public static final String VALIDAR_TOKEN_SESION_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/validarTokenSesion/";
    public static final String DATOS_BANCARIOS_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/obtenerDatosBancarios/";
    public static final String MATCH_SESION = Constants.IP + "/ProAtHome/api/profesional/matchSesion";
    public static final String UPDATE_CUENTA_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/actualizarCuenta";
    public static final String ACTUALIZAR_PERFIL_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/actualizarPerfil";
    public static final String INFO_SESION_MATCH = Constants.IP + "/ProAtHome/api/profesional/informacionSesionMatch/";
    public static final String INICIAR_SESION_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/sesionProfesional/";
    public static final String REGISTRAR_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/agregarProfesional";
    public static final String GET_DATOS_FISCALES_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/getDatosFiscales/";
    public static final String UPDATE_DATOS_FISCALES_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/guardarDatosFiscales";
    //public static final String BUSCAR_SESIONES = Constants.IP + "/ProAtHome/api/profesional/obtenerSesionesMovil/";
    public static final String BUSCAR_SESIONES = Constants.IP + "/ProAtHome/api/profesional/getServicios/";
    public static final String GET_VALORACION_CLIENTE = Constants.IP + "/ProAtHome/api/profesional/obtenerValoracion/";
    public static final String VALORAR_CLIENTE = Constants.IP + "/ProAtHome/api/profesional/valorarCliente";
    public static final String VALIDAR_VALORACION_CLIENTE = Constants.IP + "/ProAtHome/api/profesional/validarValoracion/";
    public static final String GET_TICKETS_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/obtenerTickets/";
    public static final String ENVIAR_MSG_TICKET_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/enviarMsgTicket";
    public static final String NUEVO_TICKET_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/nuevoTicket";
    public static final String FINALIZAR_TICKET_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/finalizarTicket/";
    public static final String GET_PERFIL_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/perfilProfesional/";
    public static final String UP_FOTO_PROFESIONAL = Constants.IP_80 + "/assets/lib/ActualizarFotoProfesionalAndroid.php";
    public static final String GET_SESIONES_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/obtenerSesionesProfesionalMatch/";
    public static final String GET_REPORTES_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/getReportes/";
    public static final String SOLICITUD_ELIMINAR_SESION_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/solicitudEliminarSesion/";
    public static final String CANCELAR_SERVICIO = Constants.IP + "/ProAtHome/api/profesional/cancelarServicio";
    public static final String CAMBIAR_ESTATUS_SERVICIO_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/cambiarEstatusServicio/";
    public static final String GUARDAR_PROGRESO_SERVICIO_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/actualizarProgresoServicio/";
    public static final String GET_PROGRESO_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/validarEstatusServicio/";
    public static final String CAMBIAR_DISPONIBILIDAD_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/servicioDisponible/";
    public static final String VERIFICAR_DISPONIBILIDAD_DE_CLIENTE = Constants.IP + "/ProAtHome/api/profesional/sincronizarServicio/";
    public static final String VALIDAR_SERVICIO_FINALIZADO_PROFESIONAL = Constants.IP + "/ProAtHome/api/profesional/validarServicioFinalizada/";

    //GENERAL
    public static final String FOTO_PERFIL = Constants.IP_80 + "/assets/img/fotoPerfil/";
    public static final String COBROS = Constants.IP_80 + "/assets/lib/Cargo.php";
    public static final String VERIFICACION_CORREO = Constants.IP_80 + "/assets/lib/Verificacion.php?enviar=true";
    public static final String FECHA_SERVIDOR = Constants.IP + "/ProAtHome/api/admin/fechaServidor";
    public static final String GET_MSG_TICKET = Constants.IP + "/ProAtHome/api/admin/obtenerMsgTicket/";
}

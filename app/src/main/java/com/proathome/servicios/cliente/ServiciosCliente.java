package com.proathome.servicios.cliente;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.InicioCliente;
import com.proathome.ui.fragments.NuevaSesionFragment;
import com.proathome.ui.fragments.PlanesFragment;
import com.proathome.ui.fragments.PreOrdenServicio;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiciosCliente {

    private static boolean primeraVezI1 = true, primeraVezI2 = true, primeraVezI3 = true, primeraVezI4 = true, primeraVezI5 = true;
    private static boolean primeraVezA1 = true, primeraVezA2 = true, primeraVezA3 = true, primeraVezA4 = true, primeraVezA5 = true;
    private static boolean primeraVezB1 = true, primeraVezB2 = true, primeraVezB3 = true, primeraVezB4 = true, primeraVezB5 = true;

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
                if(response == null){
                    SweetAlert.showMsg(contexto, SweetAlert.ERROR_TYPE, "¡ERROR!", "Error al obtener la información.", false, null, null);
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

    public static void getSesionActual(int tipoSolicitud, int idCliente, Context contexto){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject rutaJSON = new JSONObject(response);
                int seccion = rutaJSON.getInt("idSeccion");
                int nivel = rutaJSON.getInt("idNivel");
                int bloque = rutaJSON.getInt("idBloque");
                int minutos_horas = rutaJSON.getInt("horas");
                //ANUNCIO DE REPASO DE LECCIONES POR RUTA FINALIZADA

                if(tipoSolicitud == NUEVA_SESION_FRAGMENT){
                    //Iniciamos los adaptadores con el nivel actual.
                    NuevaSesionFragment.minutosAnteriores = minutos_horas;
                    NuevaSesionFragment.tomarSesion = new ControladorTomarSesion(contexto, seccion, nivel, bloque);
                    NuevaSesionFragment.minutosEstablecidos = NuevaSesionFragment.tomarSesion.validarHorasRestantes(seccion, nivel, bloque);
                    NuevaSesionFragment.secciones.setAdapter(NuevaSesionFragment.tomarSesion.obtenerSecciones());
                    NuevaSesionFragment.secciones.setSelection(seccion-1);
                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles());
                    NuevaSesionFragment.niveles.setSelection(nivel-1);
                    NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques());
                    NuevaSesionFragment.bloques.setSelection(bloque-1);

                    NuevaSesionFragment.secciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(NuevaSesionFragment.tomarSesion.getSeccion() == Constants.BASICO){
                                if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                                    if(!NuevaSesionFragment.basicoVisto){

                                        NuevaSesionFragment.basicoVisto = true;
                                    }else{
                                        NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.BASICO));
                                    }
                                }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.AVANZADO));
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.tomarSesion.getSeccion() == Constants.INTERMEDIO){
                                if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.BASICO));
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                                    if(!NuevaSesionFragment.intermedioVisto){

                                        NuevaSesionFragment.intermedioVisto = true;
                                    }else{
                                        NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
                                    }
                                }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.AVANZADO));
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.tomarSesion.getSeccion() == Constants.AVANZADO){
                                if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")){
                                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.BASICO));
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                                    NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.INTERMEDIO));
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                                    if(!NuevaSesionFragment.avanzadoVisto){

                                        NuevaSesionFragment.avanzadoVisto = true;
                                    }else{
                                        NuevaSesionFragment.niveles.setAdapter(NuevaSesionFragment.tomarSesion.obtenerNiveles(Constants.AVANZADO));
                                    }
                                }
                            }

                            if(NuevaSesionFragment.tomarSesion.validarSesionCorrecta(seccion, nivel, bloque, NuevaSesionFragment.secciones.getSelectedItemPosition()+1, NuevaSesionFragment.niveles.getSelectedItemPosition()+1, NuevaSesionFragment.bloques.getSelectedItemPosition()+1)){
                                NuevaSesionFragment.horasDisponiblesTV.setText("*Tienes " + NuevaSesionFragment.tomarSesion.minutosAHRS(minutos_horas) + " / " +  NuevaSesionFragment.tomarSesion.minutosAHRS(NuevaSesionFragment.tomarSesion.validarHorasRestantes(seccion, nivel, bloque)) + " de avance en el actual bloque*");
                                NuevaSesionFragment.rutaRecomendada = true;
                            }else{
                                NuevaSesionFragment.rutaRecomendada = false;
                            }
                        }
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            return;
                        }
                    });

                    NuevaSesionFragment.niveles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Básico")) {
                                if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 1")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.BASICO_1){
                                        if(!primeraVezB1)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_1));
                                        primeraVezB1 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_1));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 2")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.BASICO_2){
                                        if(!primeraVezB2)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_2));
                                        primeraVezB2 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_2));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 3")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.BASICO_3){
                                        if(!primeraVezB3)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_3));
                                        primeraVezB3 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_3));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 4")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.BASICO_4){
                                        if(!primeraVezB4)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_4));
                                        primeraVezB4 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_4));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Básico 5")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.BASICO_5){
                                        if(!primeraVezB5)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_5));
                                        primeraVezB5 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.BASICO, Constants.BASICO_5));
                                    }
                                }
                            }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Intermedio")){
                                if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 1")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.INTERMEDIO_1){
                                        if(!primeraVezI1)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_1));
                                        primeraVezI1 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_1));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 2")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.INTERMEDIO_2){
                                        if(!primeraVezI2)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_2));
                                        primeraVezI2 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_2));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 3")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.INTERMEDIO_3){
                                        if(!primeraVezI3)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_3));
                                        primeraVezI3 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_3));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 4")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.INTERMEDIO_4){
                                        if(!primeraVezI4)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_4));
                                        primeraVezI4 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_4));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Intermedio 5")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.INTERMEDIO_5){
                                        if(!primeraVezI5)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_5));
                                        primeraVezI5 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.INTERMEDIO, Constants.INTERMEDIO_5));
                                    }
                                }
                            }else if(NuevaSesionFragment.secciones.getSelectedItem().toString().equalsIgnoreCase("Avanzado")){
                                if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 1")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.AVANZADO_1){
                                        if(!primeraVezA1)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_1));
                                        primeraVezA1 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_1));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 2")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.AVANZADO_2){
                                        if(!primeraVezA2)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_2));
                                        primeraVezA2 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_2));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 3")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.AVANZADO_3){
                                        if(!primeraVezA3)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_3));
                                        primeraVezA3 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_3));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 4")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.AVANZADO_4){
                                        if(!primeraVezA4)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_4));
                                        primeraVezA4 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_4));
                                    }
                                }else if(NuevaSesionFragment.niveles.getSelectedItem().toString().equalsIgnoreCase("Avanzado 5")){
                                    if(NuevaSesionFragment.tomarSesion.getNivel() == Constants.AVANZADO_5){
                                        if(!primeraVezA5)
                                            NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_5));
                                        primeraVezA5 = false;
                                    }else {
                                        NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                        NuevaSesionFragment.bloques.setAdapter(NuevaSesionFragment.tomarSesion.obtenerBloques(Constants.AVANZADO, Constants.AVANZADO_5));
                                    }
                                }
                            }

                            if(NuevaSesionFragment.tomarSesion.validarSesionCorrecta(seccion, nivel, bloque, NuevaSesionFragment.secciones.getSelectedItemPosition()+1, NuevaSesionFragment.niveles.getSelectedItemPosition()+1, NuevaSesionFragment.bloques.getSelectedItemPosition()+1)){
                                NuevaSesionFragment.horasDisponiblesTV.setText("*Tienes " + NuevaSesionFragment.tomarSesion.minutosAHRS(minutos_horas) + " / " +  NuevaSesionFragment.tomarSesion.minutosAHRS(NuevaSesionFragment.tomarSesion.validarHorasRestantes(seccion, nivel, bloque)) + " de avance en el actual bloque*");
                                NuevaSesionFragment.rutaRecomendada = true;
                            }else{
                                NuevaSesionFragment.rutaRecomendada = false;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    NuevaSesionFragment.bloques.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 1")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 1){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 2")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 2){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 3")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 3){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 4")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 4){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 5")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 5){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 6")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 6){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }else if(NuevaSesionFragment.bloques.getSelectedItem().toString().equalsIgnoreCase("Bloque 7")){
                                if(NuevaSesionFragment.tomarSesion.getBloque() == 7){
                                }else{
                                    NuevaSesionFragment.horasDisponiblesTV.setText("Estas eligiendo una sesión a tu preferencia, no afecta a tu Ruta de Aprendizaje");
                                }
                            }

                            if(NuevaSesionFragment.tomarSesion.validarSesionCorrecta(seccion, nivel, bloque, NuevaSesionFragment.secciones.getSelectedItemPosition()+1, NuevaSesionFragment.niveles.getSelectedItemPosition()+1, NuevaSesionFragment.bloques.getSelectedItemPosition()+1)){
                                NuevaSesionFragment.horasDisponiblesTV.setText("*Tienes " + NuevaSesionFragment.tomarSesion.minutosAHRS(minutos_horas) + " / " +  NuevaSesionFragment.tomarSesion.minutosAHRS(NuevaSesionFragment.tomarSesion.validarHorasRestantes(seccion, nivel, bloque)) + " de avance en el actual bloque*");
                                NuevaSesionFragment.rutaRecomendada = true;
                            }else{
                                NuevaSesionFragment.rutaRecomendada = false;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if(rutaJSON.getBoolean("rutaFinalizada")){
                        NuevaSesionFragment.rutaFinalizada = true;
                        SweetAlert.showMsg(contexto, SweetAlert.WARNING_TYPE,"¡AVISO!", "La Ruta de Aprendizaje fue finalizada, pero puedes repasar tus lecciones.", false, null, null);
                        NuevaSesionFragment.horasDisponiblesTV.setVisibility(View.INVISIBLE);
                    }
                }else if(tipoSolicitud == PLANES_FRAGMENT){
                    PlanesFragment.idSeccion = seccion;
                    PlanesFragment.idNivel = nivel;
                    PlanesFragment.idBloque = bloque;
                }

            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_SESION_ACTUAL + idCliente, WebServicesAPI.GET, null);
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

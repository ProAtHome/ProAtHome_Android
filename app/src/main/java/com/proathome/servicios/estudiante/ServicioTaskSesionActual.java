package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.fragments.PlanesFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServicioTaskSesionActual extends AsyncTask <Void, Void, String> {

    private Context contexto;
    private String linkAPISesionActual = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerSesionActual/", respuesta;
    private int idEstudiante, tipoSolicitud;
    private ProgressDialog progressDialog;
    private boolean primeraVezI1 = true, primeraVezI2 = true, primeraVezI3 = true, primeraVezI4 = true, primeraVezI5 = true;
    private boolean primeraVezA1 = true, primeraVezA2 = true, primeraVezA3 = true, primeraVezA4 = true, primeraVezA5 = true;
    private boolean primeraVezB1 = true, primeraVezB2 = true, primeraVezB3 = true, primeraVezB4 = true, primeraVezB5 = true;
    public static int NUEVA_SESION_FRAGMENT = 1, PLANES_FRAGMENT = 2;

    public  ServicioTaskSesionActual(Context contexto, int idEstudiante, int tipoSolicitud){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
        this.tipoSolicitud = tipoSolicitud;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Cargando sesión recomendada.", "Por favor, espere..");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        String wsURL = this.linkAPISesionActual + this.idEstudiante;
        try{
            URL url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while ((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                result= stringBuffer.toString();
                this.respuesta = result;
            }else{
                result = new String("Error: "+ responseCode);
                this.respuesta = null;
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        try{
            JSONObject rutaJSON = new JSONObject(s);
            int seccion = rutaJSON.getInt("idSeccion");
            int nivel = rutaJSON.getInt("idNivel");
            int bloque = rutaJSON.getInt("idBloque");
            int minutos_horas = rutaJSON.getInt("horas");

            if(this.tipoSolicitud == ServicioTaskSesionActual.NUEVA_SESION_FRAGMENT){
                //Iniciamos los adaptadores con el nivel actual.
                NuevaSesionFragment.minutosAnteriores = minutos_horas;
                NuevaSesionFragment.tomarSesion = new ControladorTomarSesion(this.contexto, seccion, nivel, bloque);
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
            }else if(this.tipoSolicitud == ServicioTaskSesionActual.PLANES_FRAGMENT){
                PlanesFragment.idSeccion = seccion;
                PlanesFragment.idNivel = nivel;
                PlanesFragment.idBloque = bloque;
            }

        }catch (JSONException ex){
            ex.printStackTrace();
        }

    }

}

package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.ClaseEstudiante;
import com.proathome.controladores.clase.ServicioTaskFinalizarClase;
import com.proathome.controladores.clase.ServicioTaskMasTiempo;
import com.proathome.controladores.clase.ServicioTaskSumarClaseRuta;
import com.proathome.fragments.CobroFinalFragment;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.MasTiempo;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ServicioTaskCobro extends AsyncTask<Void, Void, String> {

    public static final int TOKEN_PHONE = 1;
    public static final int TOKEN_BD = 2;
    public static final int ENTENDIDO_CANCELAR = 3;
    public static final int ENTENDIDO_TE = 4;
    private String linkCobro = "http://" + Constants.IP + "/ProAtHome/assets/lib/Cargo.php";
    private String linkObtenerToken = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerToken/";
    private int idSesion, idEstudiante, tipo_token, tipo_boton;
    private String idCard, deviceId;
    private double cobro;
    private Context contexto;
    private ProgressDialog progressDialog;

    public ServicioTaskCobro(Context contexto, String deviceId, int idSesion, int idEstudiante, String idCard, double cobro, int tipo_token, int tipo_boton){
        this.contexto = contexto;
        this.deviceId = deviceId;
        this.idSesion = idSesion;
        this.idEstudiante = idEstudiante;
        this.idCard = idCard;
        this.cobro = cobro;
        this.tipo_token = tipo_token;
        this.tipo_boton = tipo_boton;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(contexto, "Generando Cobro", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String resultado = null;

        if(this.tipo_token == ServicioTaskCobro.TOKEN_PHONE){

            try {

                URL url = new URL(this.linkCobro);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
                separadoresPersonalizados.setDecimalSeparator('.');
                DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
                JSONObject parametrosPost= new JSONObject();
                parametrosPost.put("idCard", this.idCard);
                parametrosPost.put("nombreEstudiante", CobroFinalFragment.nombreEstudiante);
                parametrosPost.put("correo", CobroFinalFragment.correo);
                parametrosPost.put("cobro", formato1.format(this.cobro));
                parametrosPost.put("descripcion", "Cargo ProAtHome - " + CobroFinalFragment.sesion);
                parametrosPost.put("deviceId", this.deviceId);

                //DEFINIR PARAMETROS DE CONEXION
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //OBTENER EL RESULTADO DEL REQUEST
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(parametrosPost));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){

                    BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer sb= new StringBuffer("");
                    String linea="";
                    while ((linea=in.readLine())!= null){

                        sb.append(linea);
                        break;

                    }
                    in.close();
                    resultado = sb.toString();
                }
                else{
                    resultado = new String("Error: "+ responseCode);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(this.tipo_token == ServicioTaskCobro.TOKEN_BD){

            try {
                URL url = new URL(this.linkObtenerToken + this.idSesion + "/" + this.idEstudiante);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";
                    while ((linea = bufferedReader.readLine()) != null) {
                        stringBuffer.append(linea);
                        break;
                    }

                    bufferedReader.close();
                    resultado = stringBuffer.toString();
                }else {
                    resultado = new String("Error: "+ responseCode);
                }

            }catch (MalformedURLException | ProtocolException ex){
                ex.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        if(this.tipo_token == ServicioTaskCobro.TOKEN_PHONE){
            if(this.tipo_boton == ServicioTaskCobro.ENTENDIDO_CANCELAR){
                if(s.equalsIgnoreCase("")){
                    System.out.println("Entendido cancelar");
                    //Actualizar la orden de pago con estatusPago = Pagado.
                    ServicioTaskOrdenPago ordenPago = new ServicioTaskOrdenPago(this.idEstudiante, this.idSesion, TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, ClaseEstudiante.tiempo, TabuladorCosto.PARTICULAR), TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, CobroFinalFragment.progresoTotal, TabuladorCosto.PARTICULAR));
                    ordenPago.execute();
                    //Finalizamos la clase, sumamos la ruta y obtenemos el token de el celular para realizar el cobro.
                    ServicioTaskFinalizarClase finalizarClase = new ServicioTaskFinalizarClase(this.contexto, this.idSesion, this.idEstudiante, Constants.FINALIZAR_CLASE, DetallesFragment.ESTUDIANTE);
                    finalizarClase.execute();
                    ServicioTaskSumarClaseRuta sumarClaseRuta = new ServicioTaskSumarClaseRuta(this.contexto, this.idSesion, this.idEstudiante, ClaseEstudiante.idSeccion, ClaseEstudiante.idNivel, ClaseEstudiante.idBloque, ClaseEstudiante.tiempo, ClaseEstudiante.sumar);
                    sumarClaseRuta.execute();
                    Constants.fragmentActivity.finish();
                }else{//Mostramos el error.
                    Toast.makeText(this.contexto, s, Toast.LENGTH_LONG).show();
                    System.out.println(s);
                }
            }else if(this.tipo_boton == ServicioTaskCobro.ENTENDIDO_TE){
                if(s.equalsIgnoreCase("")){
                    System.out.println("Entendido TE");
                    //Actualizar la orden de pago con estatusPago = Pagado.
                    ServicioTaskOrdenPago ordenPago = new ServicioTaskOrdenPago(this.idEstudiante, this.idSesion, TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, ClaseEstudiante.tiempo, TabuladorCosto.PARTICULAR), TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, CobroFinalFragment.progresoTotal, TabuladorCosto.PARTICULAR));
                    ordenPago.execute();
                    //Generamos el tiempo extra y la vida sigue.
                    ServicioTaskMasTiempo masTiempo = new ServicioTaskMasTiempo(this.contexto, this.idSesion, this.idEstudiante, CobroFinalFragment.progresoTotal);
                    masTiempo.execute();
                }else{//Mostramos el error.
                    Toast.makeText(this.contexto, s, Toast.LENGTH_LONG).show();
                    System.out.println(s);
                    //Volvemos a mostrar los Tiempos extras disponibles.
                    MasTiempo masTiempo = new MasTiempo();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                    bundle.putInt("idEstudiante", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                    FragmentTransaction fragmentTransaction = ClaseEstudiante.obtenerFargment(Constants.fragmentActivity);
                    masTiempo.setArguments(bundle);
                    masTiempo.show(fragmentTransaction, "Tiempo Extra");
                }
            }
        }else if(this.tipo_token == ServicioTaskCobro.TOKEN_BD){
            if(this.tipo_boton == ServicioTaskCobro.ENTENDIDO_CANCELAR){
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(this.contexto, this.deviceId, this.idSesion, this.idEstudiante, jsonObject.get("token").toString(), this.cobro, ServicioTaskCobro.TOKEN_PHONE, ServicioTaskCobro.ENTENDIDO_CANCELAR);
                    servicioTaskCobro.execute();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(this.tipo_boton == ServicioTaskCobro.ENTENDIDO_TE){
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    ServicioTaskCobro servicioTaskCobro = new ServicioTaskCobro(this.contexto, this.deviceId, this.idSesion, this.idEstudiante, jsonObject.get("token").toString(), this.cobro, ServicioTaskCobro.TOKEN_PHONE, ServicioTaskCobro.ENTENDIDO_TE);
                    servicioTaskCobro.execute();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

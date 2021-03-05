package com.proathome.servicios;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.proathome.ClaseEstudiante;
import com.proathome.fragments.MasTiempo;
import com.proathome.servicios.clase.ServicioTaskActPagoTE;
import com.proathome.servicios.clase.ServicioTaskFinalizarClase;
import com.proathome.servicios.clase.ServicioTaskMasTiempo;
import com.proathome.servicios.clase.ServicioTaskSumarClaseRuta;
import com.proathome.fragments.CobroFinalFragment;
import com.proathome.fragments.DetallesFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
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
    private int idSesion, idEstudiante, tipo_boton;
    private String idCard, deviceId;
    private double cobro;
    private Context contexto;
    private ProgressDialog progressDialog;

    public ServicioTaskCobro(Context contexto, String deviceId, int idSesion, int idEstudiante, String idCard,
                             double cobro, int tipo_boton){
        this.contexto = contexto;
        this.deviceId = deviceId;
        this.idSesion = idSesion;
        this.idEstudiante = idEstudiante;
        this.idCard = idCard;
        this.cobro = cobro;
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

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(s);
            System.out.println(jsonObject);

            if(jsonObject.getBoolean("respuesta")){
                System.out.println("Entendido TE");
                //Actualizar la orden de pago con el costo del TE
                ServicioTaskActPagoTE actPagoTE = new ServicioTaskActPagoTE(this.contexto, this.cobro, this.idSesion);
                actPagoTE.execute();
                /*
                if(DetallesFragment.planSesion.equalsIgnoreCase("PARTICULAR")){
                    ServicioTaskOrdenPago ordenPago = new ServicioTaskOrdenPago(this.idEstudiante,
                            this.idSesion, TabuladorCosto.getCosto(ClaseEstudiante.idSeccion, ClaseEstudiante.tiempo,
                            TabuladorCosto.PARTICULAR), TabuladorCosto.getCosto(ClaseEstudiante.idSeccion,
                            CobroFinalFragment.progresoTotal, TabuladorCosto.PARTICULAR),
                            DetallesFragment.planSesion, "Pagado");
                    ordenPago.execute();
                }else{
                    ServicioTaskOrdenPago ordenPago = new ServicioTaskOrdenPago(this.idEstudiante,
                            this.idSesion, 0, TabuladorCosto.getCosto(ClaseEstudiante.idSeccion,
                            CobroFinalFragment.progresoTotal, TabuladorCosto.PARTICULAR),
                            DetallesFragment.planSesion, "Pagado");
                    ordenPago.execute();
                }*/

                //Generamos el tiempo extra y la vida sigue.
                ServicioTaskMasTiempo masTiempo = new ServicioTaskMasTiempo(this.contexto, this.idSesion,
                        this.idEstudiante, CobroFinalFragment.progresoTotal);
                masTiempo.execute();
            }else//Mostramos el error.
                showMsg("¡ERROR!",jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
        }catch(JSONException ex){
            ex.printStackTrace();
        }


    }

    public void showMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.ESTUDIANTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmButton("OK", sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    //Preguntamos si desea más tiempo Extra.
                    MasTiempo masTiempo = new MasTiempo();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idSesion", Constants.idSesion_DISPONIBILIDAD_PROGRESO);
                    bundle.putInt("idEstudiante", Constants.idPerfil_DISPONIBILIDAD_PROGRESO);
                    masTiempo.setArguments(bundle);
                    masTiempo.show(ClaseEstudiante.obtenerFargment(Constants.fragmentActivity), "Tiempo Extra");

                })
                .show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

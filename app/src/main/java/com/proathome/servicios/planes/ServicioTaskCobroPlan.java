package com.proathome.servicios.planes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.DialogFragment;
import com.proathome.fragments.OrdenCompraPlanFragment;
import com.proathome.fragments.PlanesFragment;
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
import java.net.URL;

public class ServicioTaskCobroPlan extends AsyncTask<Void, Void, String> {

    private String linkCobro = "http://" + Constants.IP + "/ProAtHome/assets/lib/Cargo.php";
    private String nombreEstudiante, correo, idCard, descripcion;
    private double cobro;
    private Context contexto;
    private ProgressDialog progressDialog;
    private DialogFragment dialogFragment;

    public ServicioTaskCobroPlan(Context contexto, String nombreEstudiante, String correo, String idCard, double cobro, String descripcion, DialogFragment dialogFragment){
        this.contexto = contexto;
        this.nombreEstudiante = nombreEstudiante;
        this.correo = correo;
        this.idCard = idCard;
        this.cobro = cobro;
        this.descripcion = descripcion;
        this.dialogFragment = dialogFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Generando cobro...", "Por favor, espere.");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {

            URL url = new URL(this.linkCobro);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("idCard", this.idCard);
            parametrosPost.put("nombreEstudiante", this.nombreEstudiante);
            parametrosPost.put("correo", this.correo);
            parametrosPost.put("cobro", this.cobro);
            parametrosPost.put("descripcion", this.descripcion);
            parametrosPost.put("deviceId", OrdenCompraPlanFragment.deviceIdString);

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

        if(s.equalsIgnoreCase(  "")){
            ServicioTaskGenerarPlan generarPlan = new ServicioTaskGenerarPlan(this.contexto, OrdenCompraPlanFragment.tipoPlan, OrdenCompraPlanFragment.fechaIn, OrdenCompraPlanFragment.fechaFi, OrdenCompraPlanFragment.monedero, OrdenCompraPlanFragment.idEstudiante);
            generarPlan.execute();
            new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡GENIAL!")
                    .setContentText("Pago correcto de PLAN.")
                    .setConfirmButton("OK", sweetAlertDialog -> {
                        /*TODO FLUJO_COBRO_PLAN: Activamos el PLAN correspondiente en el perfil y generamos las horas en monedero.
                            Guardamos el PLAN  en el historial.
                                Vamos a NuevaSesionFragment con el PLAN activo.*/
                        dialogFragment.dismiss();
                        PlanesFragment.planesFragment.dismiss();
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .show();
        }else{
            new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                    .setTitleText("¡ERROR!")
                    .setContentText("Fallo en la transacción - " + s)
                    .setConfirmButton("OK", sweetAlertDialog -> {
                        OrdenCompraPlanFragment.comprar.setEnabled(true);
                        sweetAlertDialog.dismissWithAnimation();
                    })
                    .show();
        }

    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

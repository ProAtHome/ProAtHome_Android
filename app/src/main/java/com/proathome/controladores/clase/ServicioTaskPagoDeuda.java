package com.proathome.controladores.clase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.proathome.fragments.PagoPendienteFragment;
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
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ServicioTaskPagoDeuda extends AsyncTask<Void, Void, String> {

    private String linkCobro = "http://" + Constants.IP + "/ProAtHome/assets/lib/Cargo.php";
    private String nombreEstudiante, correo, idCard, descripcion, deviceId;
    private int idSesion;
    private double cobro;
    private Context contexto;
    private ProgressDialog progressDialog;
    private DialogFragment dialogFragment;

    public ServicioTaskPagoDeuda(Context contexto, String nombreEstudiante, String correo, String idCard, double cobro, String descripcion, DialogFragment dialogFragment, int idSesion, String deviceId){
        this.contexto = contexto;
        this.nombreEstudiante = nombreEstudiante;
        this.correo = correo;
        this.idCard = idCard;
        this.cobro = cobro;
        this.descripcion = descripcion;
        this.dialogFragment = dialogFragment;
        this.idSesion = idSesion;
        this.deviceId = deviceId;
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

            DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
            separadoresPersonalizados.setDecimalSeparator('.');
            DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
            JSONObject parametrosPost= new JSONObject();
            parametrosPost.put("idCard", this.idCard);
            parametrosPost.put("nombreEstudiante", this.nombreEstudiante);
            parametrosPost.put("correo", this.correo);
            parametrosPost.put("cobro", formato1.format(this.cobro));
            parametrosPost.put("descripcion", this.descripcion);
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
        if(s.equalsIgnoreCase(  "")){
            Toast.makeText(this.contexto, "Cobro correcto.", Toast.LENGTH_LONG).show();
            dialogFragment.dismiss();
            //Actualizar Pago en Pagos
            ServicioTaskSaldarDeuda saldarDeuda = new ServicioTaskSaldarDeuda(this.idSesion);
            saldarDeuda.execute();
        }else{
            Toast.makeText(this.contexto, "Error en el cobro", Toast.LENGTH_LONG).show();
            dialogFragment.dismiss();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

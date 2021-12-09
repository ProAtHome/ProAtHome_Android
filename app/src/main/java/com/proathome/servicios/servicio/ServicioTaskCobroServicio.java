package com.proathome.servicios.servicio;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.net.HttpURLConnection;

public class ServicioTaskCobroServicio extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String idCard, nombreCliente, correo, descripcion, deviceID;
    private double cobro;
    private String linkCobro = Constants.IP_80 + "/assets/lib/Cargo.php";
    private ProgressDialog progressDialog;
    private int idCliente;
    private Bundle bundle;

    public ServicioTaskCobroServicio(Context contexto, String idCard, String nombreCliente, String correo, double cobro,
                                     String descripcion, String deviceID, int idCliente){
        this.contexto = contexto;
        this.idCard = idCard;
        this.nombreCliente = nombreCliente;
        this.correo = correo;
        this.cobro = cobro;
        this.descripcion = descripcion;
        this.deviceID = deviceID;
        this.idCliente = idCliente;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Generando cobro", "Por favor espere...");
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
            parametrosPost.put("nombreCliente", this.nombreCliente);
            parametrosPost.put("correo", this.correo);
            parametrosPost.put("cobro", formato1.format(this.cobro));
            parametrosPost.put("descripcion", this.descripcion);
            parametrosPost.put("deviceId", this.deviceID);

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

        try{
            System.out.println(s);
            JSONObject jsonObject = new JSONObject(s);
            //Guardamos el servicio.
            if(jsonObject.getBoolean("respuesta")){
                //Guardamos la info de PAGO
                ServicioTaskGuardarPago guardarPago = new ServicioTaskGuardarPago(this.contexto, jsonObject.getString("mensaje"),
                        this.cobro, 0.0, "Pagado", this.idCliente);
                guardarPago.setBundleSesion(bundle);
                guardarPago.execute();
            }else
                showMsg("¡ERROR!", s, SweetAlert.ERROR_TYPE);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public void showMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.PROFESIONAL)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public void setBundleSesion(Bundle bundle){
        this.bundle = bundle;
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }
}

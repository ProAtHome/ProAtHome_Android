package com.proathome.servicios;

import android.os.AsyncTask;
import com.proathome.utils.Constants;
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

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskOrdenPago extends AsyncTask<Void, Void, String> {

    public static final int SOLICITUD_COBRO_INICIAL = 1;
    public static final int SOLICITUD_COBRO_TE = 2;
    private int idEstudiante, idSesion, tipoSolicitud;
    private double costoClase, costoTE;
    private String tipoPlan, estatusPago;
    private String linkActualizarPago = Constants.IP + "/ProAtHome/apiProAtHome/cliente/actualizarPago";
    private String linkPagoInicial = Constants.IP + "/ProAtHome/apiProAtHome/cliente/pagoInicial";

    public ServicioTaskOrdenPago(int idEstudiante, int idSesion, double costoClase, double costoTE,
                                 String tipoPlan, String estatusPago, int tipoSolicitud){
        this.idEstudiante = idEstudiante;
        this.idSesion = idSesion;
        this.costoClase = costoClase;
        this.costoTE = costoTE;
        this.tipoPlan = tipoPlan;
        this.estatusPago = estatusPago;
        this.tipoSolicitud = tipoSolicitud;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{

            URL url = null;
            if(this.tipoSolicitud == ServicioTaskOrdenPago.SOLICITUD_COBRO_INICIAL)
                url = new URL(this.linkPagoInicial);
            else if(this.tipoSolicitud == ServicioTaskOrdenPago.SOLICITUD_COBRO_TE)
                url = new URL(this.linkActualizarPago);
            HttpsURLConnection httpURLConnection = (HttpsURLConnection)url.openConnection();

            JSONObject jsonDatos = new JSONObject();
            jsonDatos.put("costoClase", this.costoClase);
            jsonDatos.put("costoTE", this.costoTE);
            jsonDatos.put("idEstudiante", this.idEstudiante);
            jsonDatos.put("idSesion", this.idSesion);
            jsonDatos.put("estatusPago", this.estatusPago);
            jsonDatos.put("tipoPlan", this.tipoPlan);

            //PARAMETROS DE CONEXIÓN.
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoInput(true);

            //OBTENER RESULTADO DEL REQUEST
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(getPostDataString(jsonDatos));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpsURLConnection.HTTP_OK){

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while ((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();

            }else{
                resultado = new String("Error: " + responseCode);
            }

        }catch (MalformedURLException ex){
            ex.printStackTrace();
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
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

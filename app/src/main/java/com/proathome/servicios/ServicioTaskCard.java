package com.proathome.servicios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import com.proathome.SincronizarClase;
import com.proathome.servicios.clase.ServicioTaskSincronizarClases;
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
import java.net.URL;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;

public class ServicioTaskCard extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String nombreTitular, tarjeta, cvv, token;
    private int mes, ano, solicitud, idSesion, idEstudiante;
    public static final int CREAR_TOKEN = 1;
    public static final int GUARDAR_TOKEN_BD = 2;
    public static final int OBTENER_TOKEN_BD = 3;
    private String linkGuardarToken = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/actualizarToken";

    public ServicioTaskCard(Context contexto, String nombreTitular, String tarjeta, int mes, int ano,
                            String cvv, int solicitud){
        this.contexto = contexto;
        this.nombreTitular = nombreTitular;
        this.tarjeta = tarjeta;
        this.mes = mes;
        this.ano = ano;
        this.cvv = cvv;
        this.solicitud = solicitud;
    }

    public ServicioTaskCard(String token, int idSesion, int idEstudiante, int solcitud){
        this.token = token;
        this.idSesion = idSesion;
        this.idEstudiante = idEstudiante;
        this.solicitud = solcitud;
    }

    public ServicioTaskCard(int idSesion, int idEstudiante){
        this.idSesion = idSesion;
        this.idEstudiante = idEstudiante;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        if(this.solicitud == ServicioTaskCard.GUARDAR_TOKEN_BD){

            try{

                URL url = new URL(this.linkGuardarToken);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("idSesion", this.idSesion);
                jsonObject.put("idEstudiante", this.idEstudiante);
                jsonObject.put("token", this.token);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(jsonObject));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String linea = "";

                    while ((linea = in.readLine()) != null) {
                        sb.append(linea);
                        break;
                    }

                    in.close();
                    result = sb.toString();
                }else{
                    result = new String("Error: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(this.solicitud == ServicioTaskCard.CREAR_TOKEN){
            Card card = new Card();
            card.holderName(this.nombreTitular);
            card.cardNumber(this.tarjeta);
            card.expirationMonth(this.mes);
            card.expirationYear(this.ano);
            card.cvv2(this.cvv);

            Constants.openpay.createToken(card, new OperationCallBack<Token>() {

                @Override
                public void onError(mx.openpay.android.exceptions.OpenpayServiceException e) {
                    errorMsg(e.toString());
                }

                @Override
                public void onCommunicationError(mx.openpay.android.exceptions.ServiceUnavailableException e) {
                    errorMsg(e.toString());
                }

                @Override
                public void onSuccess(OperationResult<Token> operationResult) {//Si este pedo funcionó.
                    /*Guardamos el nuevo token en el perro phone*/
                    String idCard = operationResult.getResult().getId();
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(contexto);
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    String idCardSesion = "idCard" + DetallesFragment.idSesion;
                    myEditor.putString(idCardSesion, idCard);
                    myEditor.commit();

                    /*Guardamos el token en la perra BD*/
                    ServicioTaskCard servicioTaskCard = new ServicioTaskCard(idCard, DetallesFragment.idSesion,
                            DetallesFragment.idEstudiante, ServicioTaskCard.GUARDAR_TOKEN_BD);
                    servicioTaskCard.execute();
                    /*Iniciamos la búsqueda de el profesor*/
                    ServicioTaskSincronizarClases sincronizarClases = new ServicioTaskSincronizarClases(contexto,
                            DetallesFragment.idSesion, DetallesFragment.idEstudiante, DetallesFragment.ESTUDIANTE,
                                Constants.CAMBIAR_DISPONIBILIDAD, true);
                    sincronizarClases.execute();

                    Intent intent = new Intent(contexto, SincronizarClase.class);
                    intent.putExtra("perfil", DetallesFragment.ESTUDIANTE);
                    intent.putExtra("idSesion", DetallesFragment.idSesion);
                    intent.putExtra("idPerfil", DetallesFragment.idEstudiante);
                    intent.putExtra("tiempo", DetallesFragment.tiempoPasar);
                    intent.putExtra("idSeccion", DetallesFragment.idSeccion);
                    intent.putExtra("idNivel", DetallesFragment.idNivel);
                    intent.putExtra("idBloque", DetallesFragment.idBloque);
                    intent.putExtra("sumar", DetallesFragment.sumar);
                    contexto.startActivity(intent);
                }
            });
        }else if(this.solicitud == ServicioTaskCard.OBTENER_TOKEN_BD){
        }

        return result;
    }

    public void errorMsg(String mensaje){
        new SweetAlert(DetallesFragment.contexto, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

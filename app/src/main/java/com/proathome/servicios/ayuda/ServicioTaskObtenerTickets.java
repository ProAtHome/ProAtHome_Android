package com.proathome.servicios.ayuda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import com.proathome.fragments.FragmentTicketAyuda;
import com.proathome.ui.ayuda.AyudaFragment;
import com.proathome.ui.ayudaProfesor.AyudaProfesorFragment;
import com.proathome.utils.ComponentTicket;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskObtenerTickets extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String linkObtenerTickets = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/obtenerTickets/";
    private String linkObtenerTicketsProfesor = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/obtenerTickets/";
    private int idUsuario, tipoUsuario;
    private ProgressDialog progressDialog;

    public ServicioTaskObtenerTickets(Context contexto, int idUsuario, int tipoUsuario){
        this.contexto = contexto;
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = ProgressDialog.show(this.contexto, "Cargando tus tickets de ayuda", "Por favor espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = null;
            if(this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE)
                url = new URL(this.linkObtenerTickets + idUsuario);
            else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESOR)
                url = new URL(this.linkObtenerTicketsProfesor + idUsuario);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();

            }else{
                resultado = "Error: " + responseCode;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.progressDialog.dismiss();

        try{
            if(s != null){
                JSONArray jsonArray = new JSONArray(s);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE){
                            if (jsonObject.getBoolean("sinTickets")){
                                AyudaFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                            } else{
                                AyudaFragment.lottieAnimationView.setVisibility(View.INVISIBLE);
                                AyudaFragment.componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                        ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                        jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                        jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                        jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario")));
                            }
                        }else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESOR){
                            if (jsonObject.getBoolean("sinTickets")){
                                AyudaProfesorFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                            } else{
                                AyudaProfesorFragment.lottieAnimationView.setVisibility(View.INVISIBLE);
                                AyudaProfesorFragment.componentAdapterTicket.add(FragmentTicketAyuda.getmInstance(jsonObject.getString("topico"),
                                        ComponentTicket.validarEstatus(jsonObject.getInt("estatus")),
                                        jsonObject.getString("fechaCreacion"), jsonObject.getInt("idTicket"),
                                        jsonObject.getString("descripcion"), jsonObject.getString("noTicket"),
                                        jsonObject.getInt("estatus"), jsonObject.getInt("tipoUsuario")));
                            }
                        }

                    }
            }else{
                msgInfo(SweetAlert.ERROR_TYPE, "¡ERROR!", "Ocurrió un error inseperado, intenta nuevamente.",
                        this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE ? SweetAlert.ESTUDIANTE : SweetAlert.PROFESOR);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void msgInfo(int tipo, String titulo, String contenido, int tipoUsuario){
        new SweetAlert(this.contexto, tipo, tipoUsuario)
                .setTitleText(titulo)
                .setContentText(contenido)
                .show();
    }

}

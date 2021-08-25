package com.proathome.servicios.estudiante;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.PagoPendienteFragment;
import com.proathome.inicioEstudiante;
import com.proathome.utils.Component;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskBloquearPerfil extends AsyncTask<Void, Void, String> {

    private String linkBloquearPerfil = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/bloquearPerfil/";
    private int idEstudiante, procedencia;
    private Context contexto;

    public ServicioTaskBloquearPerfil(Context contexto, int idEstudiante, int procedencia){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
        this.procedencia = procedencia;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = new URL(this.linkBloquearPerfil + this.idEstudiante);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpsURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();

            }else{
                resultado = new String("Error: " + responseCode);
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

        try{
            if(s != null){
                JSONObject jsonObject = new JSONObject(s);
                PagoPendienteFragment pagoPendienteFragment = new PagoPendienteFragment();
                Bundle bundle = new Bundle();
                if(jsonObject.getBoolean("bloquear")){
                    FragmentTransaction fragmentTransaction = null;
                    bundle.putDouble("deuda", jsonObject.getDouble("deuda"));
                    bundle.putString("sesion", Component.getSeccion(jsonObject.getInt("idSeccion")) +
                            " / " + Component.getNivel(jsonObject.getInt("idSeccion"),
                                jsonObject.getInt("idNivel")) + " / " + jsonObject.getInt("idBloque"));
                    bundle.putString("lugar", jsonObject.getString("lugar"));
                    bundle.putString("nombre", jsonObject.getString("nombre"));
                    bundle.putString("correo", jsonObject.getString("correo"));
                    bundle.putInt("idSesion", jsonObject.getInt("idSesion"));
                    if(this.procedencia == DetallesFragment.PROCEDENCIA_DETALLES_FRAGMENT)
                        fragmentTransaction = DetallesFragment.detallesFragment.getFragmentManager()
                                .beginTransaction();
                    else if(this.procedencia == inicioEstudiante.PROCEDENCIA_INICIO_ESTUDIANTE)
                        fragmentTransaction = inicioEstudiante.appCompatActivity.getSupportFragmentManager()
                                .beginTransaction();

                    pagoPendienteFragment.setArguments(bundle);
                    pagoPendienteFragment.show(fragmentTransaction, "Pago pendiente");
                }
            }else{
                new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                        .setTitleText("¡ERROR!")
                        .setContentText("Error al obtener la información de tu historial de pagos, intente de nuevo más tarde.")
                        .show();
            }

        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

}

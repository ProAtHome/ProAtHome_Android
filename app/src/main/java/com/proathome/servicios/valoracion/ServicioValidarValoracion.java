package com.proathome.servicios.valoracion;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.fragments.EvaluarFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServicioValidarValoracion extends AsyncTask<Void, Void, String> {

    private int idSesion, idPerfil, procedencia;
    private String linkValidarValEst = Constants.IP +
            "/ProAtHome/apiProAtHome/profesor/validarValoracion/";
    private String linkValidarValProf = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/validarValoracion/";
    public static int PROCEDENCIA_ESTUDIANTE = 1, PROCEDENCIA_PROFESOR = 2;

    public ServicioValidarValoracion(int idSesion, int idPerfil, int procedencia){
        this.idSesion = idSesion;
        this.idPerfil = idPerfil;
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

            URL url = null;
            if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_ESTUDIANTE)
                url = new URL(this.linkValidarValProf + idSesion + "/" + idPerfil);
            else if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_PROFESOR)
                url = new URL(this.linkValidarValEst + idSesion + "/" + idPerfil);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpsURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
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
            JSONObject jsonObject = new JSONObject(s);
            if(!jsonObject.getBoolean("valorado")){
                if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_ESTUDIANTE){
                    FragmentTransaction fragmentTransaction = DetallesFragment.detallesFragment
                            .getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_ESTUDIANTE);
                    EvaluarFragment evaluarFragment = new EvaluarFragment();
                    evaluarFragment.setArguments(bundle);
                    evaluarFragment.show(fragmentTransaction, "Evaluación");
                }else if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_PROFESOR){
                    FragmentTransaction fragmentTransaction = DetallesSesionProfesorFragment
                            .fragmentDetallesProf.getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_PROFESOR);
                    EvaluarFragment evaluarFragment = new EvaluarFragment();
                    evaluarFragment.setArguments(bundle);
                    evaluarFragment.show(fragmentTransaction, "Evaluación");
                }
            }

        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

}

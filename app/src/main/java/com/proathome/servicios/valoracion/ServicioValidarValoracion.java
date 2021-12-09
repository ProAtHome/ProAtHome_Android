package com.proathome.servicios.valoracion;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesionalFragment;
import com.proathome.fragments.EvaluarFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioValidarValoracion extends AsyncTask<Void, Void, String> {

    private int idSesion, idPerfil, procedencia;
    private String linkValidarValEst = Constants.IP +
            "/ProAtHome/apiProAtHome/profesional/validarValoracion/";
    private String linkValidarValProf = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/validarValoracion/";
    public static int PROCEDENCIA_CLIENTE = 1, PROCEDENCIA_PROFESIONAL = 2;

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
            if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_CLIENTE)
                url = new URL(this.linkValidarValProf + idSesion + "/" + idPerfil);
            else if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_PROFESIONAL)
                url = new URL(this.linkValidarValEst + idSesion + "/" + idPerfil);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
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
                if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_CLIENTE){
                    FragmentTransaction fragmentTransaction = DetallesFragment.detallesFragment
                            .getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_CLIENTE);
                    EvaluarFragment evaluarFragment = new EvaluarFragment();
                    evaluarFragment.setArguments(bundle);
                    evaluarFragment.show(fragmentTransaction, "Evaluación");
                }else if(this.procedencia == ServicioValidarValoracion.PROCEDENCIA_PROFESIONAL){
                    FragmentTransaction fragmentTransaction = DetallesSesionProfesionalFragment
                            .fragmentDetallesProf.getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putInt("procedencia", EvaluarFragment.PROCEDENCIA_PROFESIONAL);
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

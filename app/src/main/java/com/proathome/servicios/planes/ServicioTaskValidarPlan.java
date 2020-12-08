package com.proathome.servicios.planes;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.inicioEstudiante;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServicioTaskValidarPlan extends AsyncTask<Void, Void, String> {

    private int idEstudiante;
    private Context contexto;
    private String linkValidarPlan = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/verificarPlan/";

    public ServicioTaskValidarPlan(Context contexto, int idEstudiante){
        this.idEstudiante = idEstudiante;
        this.contexto = contexto;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {
            URL url = new URL(this.linkValidarPlan + this.idEstudiante);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while ((linea = bufferedReader.readLine()) != null) {
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();
            }else {
                resultado = new String("Error: "+ responseCode);
            }

        }catch (MalformedURLException | ProtocolException ex){
            ex.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            if(s == null){
                new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.ESTUDIANTE)
                        .setTitleText("¡ERROR!")
                        .setContentText("Error al obtener la información.")
                        .show();
            }else{
                JSONObject jsonDatos = new JSONObject(s);
                SesionesFragment.PLAN =  jsonDatos.getString("tipoPlan");
                SesionesFragment.MONEDERO = jsonDatos.getInt("monedero");
                SesionesFragment.FECHA_INICIO = jsonDatos.getString("fechaInicio");
                SesionesFragment.FECHA_FIN = jsonDatos.getString("fechaFin");
                inicioEstudiante.tipoPlan.setText("PLAN ACTUAL: " + jsonDatos.getString("tipoPlan"));
                inicioEstudiante.monedero.setText("HORAS DISPONIBLES:                      " +
                        obtenerHorario(jsonDatos.getInt("monedero")));
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }

    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

}
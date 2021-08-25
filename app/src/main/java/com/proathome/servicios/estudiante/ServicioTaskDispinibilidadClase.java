package com.proathome.servicios.estudiante;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.proathome.ui.sesiones.SesionesFragment;
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

public class ServicioTaskDispinibilidadClase extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idEstudiante;
    private String linkDisponibilidadClase = Constants.IP + "/ProAtHome/apiProAtHome/cliente/getDisponibilidadClase/";
    private ProgressDialog progressDialog;

    public ServicioTaskDispinibilidadClase(Context contexto, int idEstudiante){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Validando la información de tus clases.", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        try {
            URL url = new URL(this.linkDisponibilidadClase + this.idEstudiante);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";

                while ((linea = in.readLine()) != null) {
                    sb.append(linea);
                    break;
                }

                in.close();
                result = sb.toString();
            } else
                result = new String("Error: " + responseCode);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta")){
                if(jsonObject.getBoolean("disponibilidad")){
                    SesionesFragment.disponibilidad = true;
                    SesionesFragment.horasDisponibles = jsonObject.getInt("horasDisponibles");
                }else
                    SesionesFragment.disponibilidad = false;
            }else
                msg("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void msg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.ESTUDIANTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

}

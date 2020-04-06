package com.proathome.controladores.profesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.fragments.BuscarSesionFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ServicioTaskObtenerSesiones extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    private String linkAPI, respuestaAPI, nombre;
    private Context contexto;
    private GoogleMap googleMap;

    public ServicioTaskObtenerSesiones(Context contexto, String linkAPI, GoogleMap googleMap){

        this.contexto = contexto;
        this.linkAPI = linkAPI;
        this.googleMap = googleMap;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Cargando Sesiones", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String resultado = null;

        try {
            URL url = new URL(this.linkAPI);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";
                while ((linea = bufferedReader.readLine()) != null) {

                    stringBuffer.append(linea);
                    break;

                }

                bufferedReader.close();
                resultado = stringBuffer.toString();
                this.respuestaAPI = resultado;
            }else {

                resultado = new String("Error: "+ responseCode);
                this.respuestaAPI = null;

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
        progressDialog.dismiss();
        this.respuestaAPI = s;

        if(this.respuestaAPI == null){

            Toast.makeText(this.contexto, "Error del servidor.", Toast.LENGTH_LONG).show();

        }else{

            if(!this.respuestaAPI.equals("null")){

                try{

                    JSONArray jsonArray = new JSONArray(this.respuestaAPI);
                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        LatLng ubicacion = new LatLng(object.getDouble("latitud"), object.getDouble("longitud"));
                        Marker marker = BuscarSesionFragment.mMap.addMarker(new MarkerOptions().position(ubicacion).title("Sesion de: " + object.getString("nombre") + "\n" + "Nivel: " + object.getString("nivel")).snippet(String.valueOf(object.getInt("idSesion"))).visible(true));
                        BuscarSesionFragment.perth.add(marker);

                    }

                }catch(JSONException ex){

                    ex.printStackTrace();

                }

            }else{

                Toast.makeText(this.contexto, "Sin Sesiones disponibles.",Toast.LENGTH_LONG).show();

            }

        }

    }

}
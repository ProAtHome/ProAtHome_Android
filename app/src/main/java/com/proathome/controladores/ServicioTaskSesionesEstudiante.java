package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesGestionarFragment;
import com.proathome.ui.inicio.InicioFragment;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class ServicioTaskSesionesEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi="";
    public String linkrequestAPI="";
    public String respuesta;
    public int idCliente;
    public int tipo;

    public ServicioTaskSesionesEstudiante(Context context, String linkAPI, int idCliente, int tipo){

        this.httpContext = context;
        this.idCliente = idCliente;
        this.linkrequestAPI = linkAPI + idCliente;
        this.tipo = tipo;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Cargando tus Clases.", "Por favor, espere...");

    }

    @Override
    protected String doInBackground(Void... voids) {

        String result =  null;
        String wsURL = linkrequestAPI;
        URL url = null;

        try{

            url = new URL(wsURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

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
                result= sb.toString();
                respuesta = result;

            }else{

                result = new String("Error: "+ responseCode);
                respuesta = null;

            }

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;

    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
        progressDialog.dismiss();
        resultadoapi=s;

        if(resultadoapi == null){

            Toast.makeText(httpContext, "Error del servidor.", Toast.LENGTH_LONG).show();

        }else{

            if(!resultadoapi.equals("null")){

                try{

                    JSONObject jsonObject = new JSONObject(resultadoapi);
                    JSONArray jsonArray = jsonObject.getJSONArray("sesiones");
                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        if(tipo == 1) {
                            InicioFragment.myAdapter.add(DetallesFragment.getmInstance(object.getInt("idsesiones"), object.getString("nivel"), object.getString("tipoClase"), object.getString("horario"),
                                    object.getString("profesor").equals("null") ? "Sin profesor Asignado" : object.getString("profesor"), object.getString("lugar"), object.getString("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                    object.getDouble("longitud")));
                        }else if(tipo == 2){
                            SesionesFragment.myAdapter.add(DetallesGestionarFragment.getmInstance(object.getInt("idsesiones"), object.getString("nivel"), object.getString("tipoClase"), object.getString("horario"),
                                    object.getString("profesor").equals("null") ? "Sin profesor Asignado" : object.getString("profesor") , object.getString("lugar"), object.getString("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                    object.getDouble("longitud"), object.getString("actualizado")));
                        }

                    }

                }catch(JSONException ex){

                    ex.printStackTrace();

                }

            }else{

                Toast.makeText(httpContext, "Usuario sin Sesiones.",Toast.LENGTH_LONG).show();

            }

        }

    }

}

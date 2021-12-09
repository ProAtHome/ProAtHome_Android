package com.proathome.servicios.cliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesGestionarFragment;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.ui.inicio.InicioFragment;
import com.proathome.ui.sesiones.SesionesFragment;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

public class ServicioTaskSesionesCliente extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi="";
    public String linkrequestAPI="";
    public String respuesta;
    public int idCliente;
    public int tipo;

    public ServicioTaskSesionesCliente(Context context, String linkAPI, int idCliente, int tipo){
        this.httpContext = context;
        this.idCliente = idCliente;
        this.linkrequestAPI = linkAPI + idCliente;
        this.tipo = tipo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Cargando tus Servicios.", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result =  null;
        try{

            URL url = new URL(this.linkrequestAPI);
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
            errorMsg("¡ERROR!", "Error del servidor, intente de nuevo más tarde.", SweetAlert.ERROR_TYPE);
        }else{
            try{
                JSONObject parametros = new JSONObject();
                parametros.put("idCliente", idCliente);
                WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {

                }, APIEndPoints.INICIAR_PROCESO_RUTA, WebServicesAPI.POST, parametros);
                webServicesAPI.execute();

                if(!resultadoapi.equals("null")){

                        JSONObject jsonObject = new JSONObject(resultadoapi);
                        JSONArray jsonArray = jsonObject.getJSONArray("sesiones");

                        if(jsonArray.length() == 0 && tipo == 1){
                            InicioFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                        }else if(jsonArray.length() == 0 && tipo == 2){
                            SesionesFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                        }

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(tipo == 1) {
                                InicioFragment.myAdapter.add(DetallesFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                                        object.getString("profesional"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                        object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getString("fecha"),
                                        object.getString("fotoProfesional"), object.getString("descripcionProfesional"), object.getString("correoProfesional"), object.getBoolean("sumar"),
                                        object.getString("tipoPlan"), object.getInt("profesionales_idprofesionales")));
                            }else if(tipo == 2){//TODO FLUJO_PLANES: Nota - Si el servicio está finalizada no se puede eliminar ni editar (No mostrar en Gestión)
                                if(!object.getBoolean("finalizado")){
                                    SesionesFragment.myAdapter.add(DetallesGestionarFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                                            object.getString("profesional"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                            object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                                            object.getString("fecha"), object.getString("tipoPlan")));
                                }
                             }

                        }
                }else
                    errorMsg("¡AVISO!","Usuario sin servicios disponibles.", SweetAlert.WARNING_TYPE);
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }
    }

    public void errorMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.httpContext, tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

}

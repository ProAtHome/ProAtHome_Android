package com.proathome.servicios.profesional;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import com.proathome.ui.fragments.DetallesGestionarProfesionalFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.ui.inicioProfesional.InicioProfesionalFragment;
import com.proathome.ui.sesionesProfesional.SesionesProfesionalFragment;
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

public class ServicioTaskSesionesProfesional extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    private String linkAPI, respuestaAPI;
    private int idProfesional, tipo;
    private Context contexto;

    public ServicioTaskSesionesProfesional(Context contexto, String linkAPI, int idProfesional, int tipo){
        this.contexto = contexto;
        this.linkAPI = linkAPI + idProfesional;
        this.idProfesional = idProfesional;
        this.tipo = tipo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto, "Cargando Sesiones", "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String result =  null;
        String wsURL = this.linkAPI;
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
                this.respuestaAPI = result;
            }else{
                result = new String("Error: "+ responseCode);
                this.respuestaAPI = null;
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
        this.respuestaAPI = s;
        progressDialog.dismiss();
        System.out.println(this.respuestaAPI);
        if(this.respuestaAPI == null){
            errorMsg("¡ERROR!", "Error en el servidor, intente de nuevo más tarde.", SweetAlert.ERROR_TYPE);
        }else{
            if(!this.respuestaAPI.equals("[]")){
                try{
                    JSONArray jsonArray = new JSONArray(this.respuestaAPI);

                    if(jsonArray.length() == 0 && tipo == 1){
                        InicioProfesionalFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                    } else if(jsonArray.length() == 0 && tipo == 2){
                        SesionesProfesionalFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        if(tipo == 1) {
                            InicioProfesionalFragment.myAdapter.add(DetallesSesionProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("nombreCliente"), object.getString("descripcion"), object.getString("correo"), object.getString("foto"),  object.getString("tipoServicio"), object.getString("horario"),
                                    "Soy yo", object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                    object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getInt("idCliente")));
                        }else if(tipo == 2){
                            if(!object.getBoolean("finalizado")){
                                SesionesProfesionalFragment.myAdapter.add(DetallesGestionarProfesionalFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoServicio"), object.getString("horario"),
                                        object.getString("nombreCliente"), object.getString("correo"), object.getString("descripcion"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                        object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                                        object.getString("fecha"), object.getString("tipoPlan"), object.getString("foto")));
                            }
                        }
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else
                errorMsg("¡AVISO!", "Usuario sin servicios disponibles.", SweetAlert.WARNING_TYPE);
        }
    }

    public void errorMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.PROFESIONAL)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

}

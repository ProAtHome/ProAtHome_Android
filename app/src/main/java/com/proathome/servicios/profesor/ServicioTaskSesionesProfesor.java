package com.proathome.servicios.profesor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import com.proathome.fragments.DetallesGestionarProfesorFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.ui.inicioProfesor.InicioProfesorFragment;
import com.proathome.ui.sesionesProfesor.SesionesProfesorFragment;
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

public class ServicioTaskSesionesProfesor extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    private String linkAPI, respuestaAPI;
    private int idProfesor, tipo;
    private Context contexto;

    public ServicioTaskSesionesProfesor(Context contexto, String linkAPI, int idProfesor, int tipo){
        this.contexto = contexto;
        this.linkAPI = linkAPI + idProfesor;
        this.idProfesor = idProfesor;
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
                        InicioProfesorFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                    } else if(jsonArray.length() == 0 && tipo == 2){
                        SesionesProfesorFragment.lottieAnimationView.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        if(tipo == 1) {
                            InicioProfesorFragment.myAdapter.add(DetallesSesionProfesorFragment.getmInstance(object.getInt("idsesiones"), object.getString("nombreEstudiante"), object.getString("descripcion"), object.getString("correo"), object.getString("foto"),  object.getString("tipoClase"), object.getString("horario"),
                                    "Soy yo", object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                    object.getDouble("longitud"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"), object.getInt("idEstudiante")));
                        }else if(tipo == 2){
                            if(!object.getBoolean("finalizado")){
                                SesionesProfesorFragment.myAdapter.add(DetallesGestionarProfesorFragment.getmInstance(object.getInt("idsesiones"), object.getString("tipoClase"), object.getString("horario"),
                                        object.getString("nombreEstudiante"), object.getString("correo"), object.getString("descripcion"), object.getString("lugar"), object.getInt("tiempo"), object.getString("extras"), object.getDouble("latitud"),
                                        object.getDouble("longitud"), object.getString("actualizado"), object.getInt("idSeccion"), object.getInt("idNivel"), object.getInt("idBloque"),
                                        object.getString("fecha"), object.getString("tipoPlan"), object.getString("foto")));
                            }
                        }
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }else
                errorMsg("¡AVISO!", "Usuario sin clases disponibles.", SweetAlert.WARNING_TYPE);
        }
    }

    public void errorMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(this.contexto, tipo, SweetAlert.PROFESOR)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

}

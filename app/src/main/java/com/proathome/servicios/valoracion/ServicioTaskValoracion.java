package com.proathome.servicios.valoracion;

import android.os.AsyncTask;

import com.proathome.fragments.PerfilFragment;
import com.proathome.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskValoracion extends AsyncTask<Void, Void, String> {

    private int idPerfil, tipoSolicitud;
    private String linkValoracionProfesional = Constants.IP + "/ProAtHome/apiProAtHome/cliente/obtenerValoracion/";
    private String linkValoracionCliente = Constants.IP + "/ProAtHome/apiProAtHome/profesional/obtenerValoracion/";
    public static int PERFIL_PROFESIONAL = 1, PERFIL_CLIENTE = 2;

    public ServicioTaskValoracion(int idPerfil, int tipoSolicitud){
        this.idPerfil = idPerfil;
        this.tipoSolicitud = tipoSolicitud;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        if(this.tipoSolicitud == ServicioTaskValoracion.PERFIL_PROFESIONAL){
            try{
                URL url = new URL(this.linkValoracionProfesional + this.idPerfil);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
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
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else if(this.tipoSolicitud == ServicioTaskValoracion.PERFIL_CLIENTE){
            try{
                URL url = new URL(this.linkValoracionCliente + this.idPerfil);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
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
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        int numValoraciones = 0;
        double sumaValoraciones = 0;
        try{
            JSONArray jsonArray = new JSONArray(s);
            if(jsonArray == null){
                System.out.println("Error en la solicitud");
            }else{
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    System.out.println(jsonObject);
                    if(jsonObject.getBoolean("valoraciones")){//Valoraciones del profesional
                        if(jsonObject.getBoolean("error")){
                            //Hay error.
                            PerfilFragment.componentAdapterValoraciones.add(PerfilFragment.getmInstance("El usuario no tiene valoraciones aún.", 0.0f));
                        }else{
                            //Obtener promedio
                            numValoraciones++;
                            sumaValoraciones += Double.parseDouble(jsonObject.get("valoracion").toString());
                            PerfilFragment.componentAdapterValoraciones.add(PerfilFragment.getmInstance(jsonObject.getString("comentario"), Float.parseFloat(jsonObject.get("valoracion").toString())));
                        }
                    }else{//Perfil de profesional
                        PerfilFragment.tvNombre.setText(jsonObject.getString("nombre"));
                        PerfilFragment.tvCoreo.setText(jsonObject.getString("correo"));
                        PerfilFragment.descripcion.setText(jsonObject.getString("descripcion"));
                    }
                }
                //Promedio
                double promedio = sumaValoraciones / numValoraciones;
                if(numValoraciones == 0)
                    PerfilFragment.tvCalificacion.setText("0.00");
                else
                    PerfilFragment.tvCalificacion.setText(String.format("%.2f", promedio));
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

}

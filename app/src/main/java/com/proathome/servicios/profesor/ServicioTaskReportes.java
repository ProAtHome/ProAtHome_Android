package com.proathome.servicios.profesor;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.proathome.ui.editarPerfil.EditarPerfilFragment;
import com.proathome.ui.editarPerfilProfesor.EditarPerfilProfesorFragment;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskReportes extends AsyncTask<Void,Void,String> {

    public String linkAPIEstudiante = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/getReportes/";
    public String linkAPIProfesor = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/getReportes/";
    public Context contexto;
    public int perfil, idUsuario;

    public ServicioTaskReportes(Context contexto, int perfil, int idUsuario){
        this.contexto = contexto;
        this.perfil = perfil;
        this.idUsuario = idUsuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try {
            URL url = null;
            if(this.perfil == Constants.TIPO_ESTUDIANTE)
                url = new URL(this.linkAPIEstudiante + this.idUsuario);
            else if(this.perfil == Constants.TIPO_PROFESOR)
                url = new URL(this.linkAPIProfesor + this.idUsuario);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //DEFINIR PARAMETROS DE CONEXION
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode=urlConnection.getResponseCode();
            if(responseCode== HttpURLConnection.HTTP_OK){
                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb= new StringBuffer("");
                String linea="";

                while ((linea=in.readLine())!= null){
                    sb.append(linea);
                    break;
                }

                in.close();
                resultado = sb.toString();
            }
            else{
                resultado = new String("Error: "+ responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta")){
                JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                if(mensaje.getInt("reportes") == 0){
                    //ocultamos avisos
                    if(this.perfil == Constants.TIPO_ESTUDIANTE){
                        EditarPerfilFragment.tvAviso.setVisibility(View.INVISIBLE);
                        EditarPerfilFragment.imgAviso.setVisibility(View.INVISIBLE);
                        EditarPerfilFragment.cardValoracion.setVisibility(View.INVISIBLE);
                    }else if(this.perfil == Constants.TIPO_PROFESOR){
                        EditarPerfilProfesorFragment.tvAviso.setVisibility(View.INVISIBLE);
                        EditarPerfilProfesorFragment.imgAviso.setVisibility(View.INVISIBLE);
                        EditarPerfilProfesorFragment.cardValoracion.setVisibility(View.INVISIBLE);
                    }
                }else if(mensaje.getInt("reportes") > 0){
                    int numReportes = mensaje.getInt("reportes");
                    String descripcion = mensaje.getString("aviso");

                    if(this.perfil == Constants.TIPO_ESTUDIANTE){
                        EditarPerfilFragment.tvAviso.setVisibility(View.VISIBLE);
                        EditarPerfilFragment.imgAviso.setVisibility(View.VISIBLE);
                        EditarPerfilFragment.cardValoracion.setVisibility(View.VISIBLE);
                        EditarPerfilFragment.tvAviso.setText("Aviso No. " + numReportes + ": " + descripcion);
                    }else if(this.perfil == Constants.TIPO_PROFESOR){
                        EditarPerfilProfesorFragment.tvAviso.setVisibility(View.VISIBLE);
                        EditarPerfilProfesorFragment.imgAviso.setVisibility(View.VISIBLE);
                        EditarPerfilProfesorFragment.cardValoracion.setVisibility(View.VISIBLE);
                        EditarPerfilProfesorFragment.tvAviso.setText("Aviso No. " + numReportes + ": " + descripcion);
                    }
                    //mostramos aviso
                }
            }else{
                //Error mensaje.
            }

        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

}

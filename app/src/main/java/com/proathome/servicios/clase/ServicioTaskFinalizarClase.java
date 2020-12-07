package com.proathome.servicios.clase;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskFinalizarClase extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idSesion, idPerfil, tipoSolicitud, tipoPerfil;
    private String linkFinalizarClase = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/finalizarClase/";
    private String linkValidarClaseFinalizada = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/validarClaseFinalizada/";
    private String linkValidarClaseFinalizadaEstudiante = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/validarClaseFinalizada/";

    public ServicioTaskFinalizarClase(Context contexto, int idSesion, int idPerfil, int tipoSolicitud, int tipoPerfil){
        this.contexto = contexto;
        this.idSesion = idSesion;
        this.idPerfil = idPerfil;
        this.tipoSolicitud = tipoSolicitud;
        this.tipoPerfil = tipoPerfil;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        if(this.tipoSolicitud == Constants.FINALIZAR_CLASE){
            try{
                URL url = new URL(this.linkFinalizarClase + this.idSesion + "/" + this.idPerfil);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

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
                    result = sb.toString();
                    in = null;
                    sb = null;
                }else{
                    result = new String("Error: " +responseCode);
                }

                urlConnection = null;

            }catch(MalformedURLException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else if(this.tipoSolicitud == Constants.VALIDAR_CLASE_FINALIZADA){
            try{

                URL url = new URL(this.linkValidarClaseFinalizada + this.idSesion + "/" + this.idPerfil);
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
                    result = sb.toString();
                    in = null;
                    sb = null;

                }else{
                    result = new String("Error: "+ responseCode);
                }

                urlConnection = null;

            }catch(MalformedURLException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else if(this.tipoSolicitud == Constants.VALIDAR_CLASE_FINALIZADA_AMBOS_PERFILES){
            if(this.tipoPerfil == DetallesFragment.ESTUDIANTE){
                try{
                    URL url = new URL(this.linkValidarClaseFinalizadaEstudiante + this.idSesion + "/" + this.idPerfil);
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
                        result = sb.toString();
                        in = null;
                        sb = null;

                    }else{
                        result = new String("Error: "+ responseCode);
                    }

                    urlConnection = null;

                }catch(MalformedURLException ex){
                    ex.printStackTrace();
                }catch(IOException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else if(this.tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                try{
                    URL url = new URL(this.linkValidarClaseFinalizada + this.idSesion + "/" + this.idPerfil);
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
                        result = sb.toString();
                        in = null;
                        sb = null;
                    }else{
                        result = new String("Error: "+ responseCode);
                    }

                    urlConnection = null;
                }catch(MalformedURLException ex){
                    ex.printStackTrace();
                }catch(IOException ex){
                    ex.printStackTrace();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

        }

        return  result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(this.tipoSolicitud == Constants.VALIDAR_CLASE_FINALIZADA){
            try{
                JSONObject jsonObject = new JSONObject(s);
                boolean finalizado = jsonObject.getBoolean("finalizado");
                if(finalizado){
                    Constants.fragmentActivity.finish();
                    DetallesSesionProfesorFragment.procedenciaFin = true;
                }else{
                    new SweetAlert(this.contexto, SweetAlert.WARNING_TYPE, SweetAlert.PROFESOR)
                            .setTitleText("¡ESPERA!")
                            .setContentText("El estudiante todavia no decide si tomar tiempo extra o finalizar la clase.")
                            .show();
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }else if(this.tipoSolicitud == Constants.VALIDAR_CLASE_FINALIZADA_AMBOS_PERFILES){
            try{
                JSONObject jsonObject = new JSONObject(s);
                boolean finalizado = jsonObject.getBoolean("finalizado");

                if(this.tipoPerfil == DetallesFragment.ESTUDIANTE){
                    if(finalizado){
                        DetallesFragment.iniciar.setEnabled(false);
                        DetallesFragment.iniciar.setText("Clase finalizada");
                    }
                }else if(this.tipoPerfil == DetallesSesionProfesorFragment.PROFESOR){
                    if(finalizado){
                        DetallesSesionProfesorFragment.iniciar.setEnabled(false);
                        DetallesSesionProfesorFragment.iniciar.setText("Clase finalizada");
                    }
                }

            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }

    }

}

package com.proathome.servicios.servicio;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskFinalizarServicio extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idSesion, idPerfil, tipoSolicitud, tipoPerfil;
    private String linkFinalizarServicio = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/finalizarServicio/";
    private String linkValidarServicioFinalizada = Constants.IP +
            "/ProAtHome/apiProAtHome/profesional/validarServicioFinalizada/";
    private String linkValidarServicioFinalizadaCliente = Constants.IP +
            "/ProAtHome/apiProAtHome/cliente/validarServicioFinalizada/";

    public ServicioTaskFinalizarServicio(Context contexto, int idSesion, int idPerfil, int tipoSolicitud, int tipoPerfil){
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

        if(this.tipoSolicitud == Constants.FINALIZAR_SERVICIO){
            try{
                URL url = new URL(this.linkFinalizarServicio + this.idSesion + "/" + this.idPerfil);
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
        }else if(this.tipoSolicitud == Constants.VALIDAR_SERVICIO_FINALIZADA){
            try{

                URL url = new URL(this.linkValidarServicioFinalizada + this.idSesion + "/" + this.idPerfil);
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
        }else if(this.tipoSolicitud == Constants.VALIDAR_SERVICIO_FINALIZADA_AMBOS_PERFILES){
            if(this.tipoPerfil == DetallesFragment.CLIENTE){
                try{
                    URL url = new URL(this.linkValidarServicioFinalizadaCliente + this.idSesion + "/" + this.idPerfil);
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
            }else if(this.tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL){
                try{
                    URL url = new URL(this.linkValidarServicioFinalizada + this.idSesion + "/" + this.idPerfil);
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

        if(this.tipoSolicitud == Constants.VALIDAR_SERVICIO_FINALIZADA){
            try{
                JSONObject jsonObject = new JSONObject(s);
                boolean finalizado = jsonObject.getBoolean("finalizado");
                if(finalizado){
                    Constants.fragmentActivity.finish();
                    DetallesSesionProfesionalFragment.procedenciaFin = true;
                }else{
                    new SweetAlert(this.contexto, SweetAlert.WARNING_TYPE, SweetAlert.PROFESIONAL)
                            .setTitleText("¡ESPERA!")
                            .setContentText("El cliente todavia no decide si tomar tiempo extra o finalizar el servicio.")
                            .show();
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }else if(this.tipoSolicitud == Constants.VALIDAR_SERVICIO_FINALIZADA_AMBOS_PERFILES){
            try{
                JSONObject jsonObject = new JSONObject(s);
                boolean finalizado = jsonObject.getBoolean("finalizado");

                if(this.tipoPerfil == DetallesFragment.CLIENTE){
                    if(finalizado){
                        DetallesFragment.iniciar.setEnabled(false);
                        DetallesFragment.iniciar.setText("Servicio finalizada");
                    }
                }else if(this.tipoPerfil == DetallesSesionProfesionalFragment.PROFESIONAL){
                    if(finalizado){
                        DetallesSesionProfesionalFragment.iniciar.setEnabled(false);
                        DetallesSesionProfesionalFragment.iniciar.setText("Servicio finalizada");
                    }
                }

            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }

    }

}

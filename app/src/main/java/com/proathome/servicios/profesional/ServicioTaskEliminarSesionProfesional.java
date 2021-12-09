package com.proathome.servicios.profesional;

import android.content.Context;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskEliminarSesionProfesional extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int solicitud, idServicio, idProfesional;
    private String solicitudEliminar = Constants.IP + "/ProAtHome/apiProAtHome/profesional/solicitudEliminarSesion/";
    private String eliminarServicio = Constants.IP + "/ProAtHome/apiProAtHome/profesional/cancelarServicio";
    private Fragment fragment;

    public ServicioTaskEliminarSesionProfesional(Context contexto, int idServicio, int idProfesional, int solicitud, Fragment fragment){
        this.contexto = contexto;
        this.idServicio = idServicio;
        this.idProfesional = idProfesional;
        this.solicitud = solicitud;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        if(this.solicitud == Constants.SOLICITUD_ELIMINAR){
            try {
                URL url = new URL(this.solicitudEliminar + this.idServicio + "/" + this.idProfesional);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //DEFINIR PARAMETROS DE CONEXION
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String linea = "";

                    while ((linea = in.readLine()) != null) {
                        sb.append(linea);
                        break;
                    }

                    in.close();
                    resultado = sb.toString();
                } else
                    resultado = new String("Error: " + responseCode);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(this.solicitud == Constants.ELIMINAR_SESION){
            try{
                URL url = new URL(this.eliminarServicio);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject parametrosPOST = new JSONObject();
                parametrosPOST.put("idProfesional", this.idProfesional);
                parametrosPOST.put("idServicio", this.idServicio);
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(parametrosPOST));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String linea = "";

                    while ((linea = in.readLine()) != null){
                        sb.append(linea);
                        break;
                    }

                    in.close();
                    resultado = sb.toString();
                }else
                    resultado = new String("Error: " +responseCode);

            }catch(MalformedURLException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch (JSONException ex){
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONObject jsonObject = new JSONObject(s);
            if(jsonObject.getBoolean("respuesta")){
                if(this.solicitud == Constants.SOLICITUD_ELIMINAR){
                    JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                    if(mensaje.getBoolean("eliminar") && mensaje.getBoolean("multa")){
                        //Mostrar que se puede eliminar pero con multa.
                        showAlert("Si cancelas esta servicio serás acreedor de una multa, ya que para cancelar libremente deberá ser 3 HRS antes.");
                    }else if(mensaje.getBoolean("eliminar") && !mensaje.getBoolean("multa")){
                        //Eliminar sin multa.
                        showAlert("¿Deseas cancelar el servicio?");
                    }else if(!mensaje.getBoolean("eliminar")){
                        //No se puede eliminar
                        errorMsg("No se puede cancelar el servicio a menos de 24 HRS de esta.");
                    }
                }else if(this.solicitud == Constants.ELIMINAR_SESION){
                    new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, SweetAlert.PROFESIONAL)
                            .setTitleText("¡GENIAL!")
                            .setContentText(jsonObject.getString("mensaje"))
                            .setConfirmButton("OK", sweetAlertDialog -> {
                                this.fragment.getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                this.fragment.getActivity().finish();
                            }).show();
                }
            }else
                errorMsg(jsonObject.getString("mensaje"));

        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    private void errorMsg(String mensaje){
        new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    private void showAlert(String mensaje) {
        new MaterialAlertDialogBuilder(this.contexto, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Cancelar Servicio")
                .setMessage(mensaje)
                .setPositiveButton("Cancelar Servicio", (dialog, which) -> {
                    ServicioTaskEliminarSesionProfesional eliminarSesion = new ServicioTaskEliminarSesionProfesional(this.contexto, this.idServicio, this.idProfesional, Constants.ELIMINAR_SESION, this.fragment);
                    eliminarSesion.execute();
                })
                .setNegativeButton("Cerrar", (dialog, which) -> {
                })
                .show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

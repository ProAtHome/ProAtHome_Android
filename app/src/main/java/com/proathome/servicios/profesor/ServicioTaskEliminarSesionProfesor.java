package com.proathome.servicios.profesor;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskEliminarSesionProfesor extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int solicitud, idClase, idProfesor;
    private String solicitudEliminar = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/solicitudEliminarSesion/";
    private String eliminarClase = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/cancelarClase";
    private Fragment fragment;

    public ServicioTaskEliminarSesionProfesor(Context contexto, int idClase, int idProfesor, int solicitud, Fragment fragment){
        this.contexto = contexto;
        this.idClase = idClase;
        this.idProfesor = idProfesor;
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
                URL url = new URL(this.solicitudEliminar + this.idClase + "/" + this.idProfesor);
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
                URL url = new URL(this.eliminarClase);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                JSONObject parametrosPOST = new JSONObject();
                parametrosPOST.put("idProfesor", this.idProfesor);
                parametrosPOST.put("idClase", this.idClase);
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
                        showAlert("Si cancelas esta clase serás acreedor de una multa, ya que para cancelar libremente deberá ser 3 HRS antes.");
                    }else if(mensaje.getBoolean("eliminar") && !mensaje.getBoolean("multa")){
                        //Eliminar sin multa.
                        showAlert("¿Deseas cancelar la clase?");
                    }else if(!mensaje.getBoolean("eliminar")){
                        //No se puede eliminar
                    }
                }else if(this.solicitud == Constants.ELIMINAR_SESION){
                    new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, SweetAlert.PROFESOR)
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
        new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.PROFESOR)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    private void showAlert(String mensaje) {
        new MaterialAlertDialogBuilder(this.contexto, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Cancelar Clase")
                .setMessage(mensaje)
                .setPositiveButton("Cancelar Clase", (dialog, which) -> {
                    ServicioTaskEliminarSesionProfesor eliminarSesion = new ServicioTaskEliminarSesionProfesor(this.contexto, this.idClase, this.idProfesor, Constants.ELIMINAR_SESION, this.fragment);
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

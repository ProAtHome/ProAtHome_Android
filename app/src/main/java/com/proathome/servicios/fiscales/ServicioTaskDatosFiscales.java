package com.proathome.servicios.fiscales;

import android.content.Context;
import android.os.AsyncTask;

import androidx.fragment.app.DialogFragment;

import com.proathome.fragments.DatosFiscalesFragment;
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

public class ServicioTaskDatosFiscales extends AsyncTask<Void,Void,String> {

    private Context contexto;
    private int tipoPerfil, idUsuario, tipoPeticion;
    private String linkEstudiantesGET = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/getDatosFiscales/";
    private String linkProfesoresGET = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/getDatosFiscales/";
    private String linkEstudiantesUP = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/guardarDatosFiscales";
    private String linkProfesoresUP = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/profesor/guardarDatosFiscales";
    private String razonSocial, tipoPersona, rfc, cfdi;
    private DialogFragment dialogFragment;

    public ServicioTaskDatosFiscales(Context contexto, int tipoPerfil, int idUsuario, int tipoPeticion){
        this.contexto = contexto;
        this.tipoPerfil = tipoPerfil;
        this.idUsuario = idUsuario; 
        this.tipoPeticion = tipoPeticion;
    }

    public void setUpDatos(String tipoPersona, String razonSocial, String rfc, String cfdi, DialogFragment dialogFragment){
        this.tipoPersona = tipoPersona;
        this.razonSocial = razonSocial;
        this.rfc = rfc;
        this.cfdi = cfdi;
        this.dialogFragment = dialogFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        if(this.tipoPeticion == Constants.GET_DATOS_FISCALES){
            try{
                URL url = null;
                if(this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE)
                    url = new URL(this.linkEstudiantesGET + this.idUsuario);
                else if(this.tipoPerfil == Constants.TIPO_USUARIO_PROFESOR)
                    url = new URL(this.linkProfesoresGET + this.idUsuario);
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
        }else if(this.tipoPeticion == Constants.UP_DATOS_FISCALES){
            try{
                URL url = null;
                if(this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE)
                    url = new URL(this.linkEstudiantesUP);
                else if(this.tipoPerfil == Constants.TIPO_USUARIO_PROFESOR)
                    url = new URL(this.linkProfesoresUP);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                JSONObject jsonDatos = new JSONObject();
                if(this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE)
                    jsonDatos.put("idEstudiante", this.idUsuario);
                else if(this.tipoPerfil == Constants.TIPO_USUARIO_PROFESOR)
                    jsonDatos.put("idProfesor", this.idUsuario);
                jsonDatos.put("tipoPersona", this.tipoPersona);
                jsonDatos.put("razonSocial", this.razonSocial);
                jsonDatos.put("rfc", this.rfc);
                jsonDatos.put("cfdi", this.cfdi);

                //PARAMETROS DE CONEXIÓN.
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoInput(true);

                //OBTENER RESULTADO DEL REQUEST
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(getPostDataString(jsonDatos));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer("");
                    String linea = "";

                    while ((linea = bufferedReader.readLine()) != null){
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
            }catch (Exception e) {
                e.printStackTrace();
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
                if(this.tipoPeticion == Constants.GET_DATOS_FISCALES){
                    JSONObject datos = jsonObject.getJSONObject("mensaje");
                    if(datos.getBoolean("existe")){
                        DatosFiscalesFragment.etRFC.setText(datos.getString("rfc"));
                        DatosFiscalesFragment.etRazonSocial.setText(datos.getString("razonSocial"));
                        if(datos.getString("tipoPersona").equals("FISICA"))
                            DatosFiscalesFragment.spTipoPersona.setSelection(0);
                        else if(datos.getString("tipoPersona").equals("MORAL"))
                            DatosFiscalesFragment.spTipoPersona.setSelection(1);

                        if(datos.getString("cfdi").equals("POR DEFINIR"))
                            DatosFiscalesFragment.spCFDI.setSelection(0);
                        else if(datos.getString("cfdi").equals("POR DEFINIR"))
                            DatosFiscalesFragment.spCFDI.setSelection(1);
                    }
                }else if(this.tipoPeticion == Constants.UP_DATOS_FISCALES){
                    new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE ? SweetAlert.ESTUDIANTE : SweetAlert.PROFESOR)
                            .setTitleText("¡GENIAL!")
                            .setContentText(jsonObject.getString("mensaje"))
                            .setConfirmButton("OK", sweetAlertDialog -> {
                                sweetAlertDialog.dismissWithAnimation();
                                dialogFragment.dismiss();
                            })
                    .show();
                }
            }else
                msgError("¡ERROR!", jsonObject.getString("mensaje"));

        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void msgError(String titulo, String mensaje){
        new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, this.tipoPerfil == Constants.TIPO_USUARIO_ESTUDIANTE ? SweetAlert.ESTUDIANTE : SweetAlert.PROFESOR)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }
}

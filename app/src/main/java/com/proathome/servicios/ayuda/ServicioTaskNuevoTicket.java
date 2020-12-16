package com.proathome.servicios.ayuda;

import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.DialogFragment;
import com.proathome.ui.ayuda.AyudaFragment;
import com.proathome.ui.ayudaProfesor.AyudaProfesorFragment;
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

public class ServicioTaskNuevoTicket extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String topico, descripcion, fechaCreacion;
    private String linkNuevoTiket = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/nuevoTicket";
    private String linkNuevoTiketProfesor = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/nuevoTicket";
    private int tipoUsuario, estatus, idUsuario;
    private DialogFragment dialogFragment;

    public ServicioTaskNuevoTicket(Context contexto, int tipoUsuario, String topico, String descripcion,
                                   String fechaCreacion, int estatus, int idUsuario, DialogFragment dialogFragment){
        this.contexto = contexto;
        this.tipoUsuario = tipoUsuario;
        this.topico = topico;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estatus = estatus;
        this.idUsuario = idUsuario;
        this.dialogFragment = dialogFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = null;
            if(this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE)
                url = new URL(this.linkNuevoTiket);
            else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESOR)
                url = new URL(this.linkNuevoTiketProfesor);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            JSONObject jsonDatos = new JSONObject();
            jsonDatos.put("tipoUsuario", this.tipoUsuario);
            jsonDatos.put("topico", this.topico);
            jsonDatos.put("descripcion", this.descripcion);
            jsonDatos.put("fechaCreacion", this.fechaCreacion);
            jsonDatos.put("estatus", this.estatus);
            jsonDatos.put("idUsuario", this.idUsuario);

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

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
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

        }catch (MalformedURLException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        new SweetAlert(this.contexto, SweetAlert.SUCCESS_TYPE, this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE ?
                SweetAlert.ESTUDIANTE : SweetAlert.PROFESOR)
                .setTitleText("¡GENIAL!")
                .setContentText("Tu solicitud será revisada y en breve te contestará soporte.")
                .setConfirmButton("OK", sweetAlertDialog -> {
                    this.dialogFragment.dismiss();
                    sweetAlertDialog.dismissWithAnimation();
                    if(this.tipoUsuario == Constants.TIPO_USUARIO_ESTUDIANTE){
                        AyudaFragment.configAdapter();
                        AyudaFragment.configRecyclerView();
                    }else if(this.tipoUsuario == Constants.TIPO_USUARIO_PROFESOR){
                        AyudaProfesorFragment.configAdapter();
                        AyudaProfesorFragment.configRecyclerView();
                    }

                    ServicioTaskObtenerTickets obtenerTickets = new ServicioTaskObtenerTickets(this.contexto, this.idUsuario, this.tipoUsuario);
                    obtenerTickets.execute();
                })
                .show();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}

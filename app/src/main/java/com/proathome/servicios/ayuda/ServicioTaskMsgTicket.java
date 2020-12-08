package com.proathome.servicios.ayuda;

import android.content.Context;
import android.os.AsyncTask;

import com.proathome.fragments.FragmentTicketAyuda;
import com.proathome.ui.ayuda.AyudaFragment;
import com.proathome.utils.Constants;
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

public class ServicioTaskMsgTicket extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String linkMsgTicket = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/enviarMsgTicket";
    private String mensaje;
    private int idUsuario, idTicket;
    private boolean operador;

    public ServicioTaskMsgTicket(Context contexto, String mensaje, int idUsuario, boolean operador, int idTicket){
        this.contexto = contexto;
        this.mensaje = mensaje;
        this.idUsuario = idUsuario;
        this.operador = operador;
        this.idTicket = idTicket;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        try{
            URL url = new URL(this.linkMsgTicket);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            JSONObject jsonDatos = new JSONObject();
            jsonDatos.put("mensaje", this.mensaje);
            jsonDatos.put("idUsuario", this.idUsuario);
            jsonDatos.put("operador", this.operador);
            jsonDatos.put("idTicket", this.idTicket);

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
        ServicioTaskObtenerMsgTicket obtenerMsgTicket = new ServicioTaskObtenerMsgTicket(this.contexto, this.idUsuario, this.idTicket);
        obtenerMsgTicket.execute();
        FragmentTicketAyuda.configAdapter();
        FragmentTicketAyuda.configRecyclerView();
    }

    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }

}
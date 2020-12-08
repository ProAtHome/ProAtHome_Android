package com.proathome.servicios.ayuda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.proathome.fragments.FragmentTicketAyuda;
import com.proathome.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServicioTaskObtenerMsgTicket extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private int idEstudiante, idTicket;
    private String linkObtenerMsgTicket = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/admin/obtenerMsgTicket/";

    public ServicioTaskObtenerMsgTicket(Context contexto, int idEstudiante, int idTicket){
        this.contexto = contexto;
        this.idEstudiante = idEstudiante;
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
            URL url = new URL(this.linkObtenerMsgTicket + idEstudiante + "/" + Constants.TIPO_USUARIO_ESTUDIANTE + "/" + idTicket);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();

            }else{
                resultado = "Error: " + responseCode;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            JSONArray jsonArray = new JSONArray(s);
            JSONObject ticket = jsonArray.getJSONObject(0);
            JSONObject mensajes = jsonArray.getJSONObject(1);
            JSONArray jsonArrayMensajes = mensajes.getJSONArray("mensajes");
            for(int i = 0; i < jsonArrayMensajes.length(); i++){
                JSONObject mensaje = jsonArrayMensajes.getJSONObject(i);
                FragmentTicketAyuda.componentAdapterMsgTickets.add(FragmentTicketAyuda.getmInstance(
                        "Usuario", mensaje.getString("msg"), mensaje.getBoolean("operador")));
            }
            FragmentTicketAyuda.recyclerView.getLayoutManager().scrollToPosition(jsonArrayMensajes.length() - 1);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

}

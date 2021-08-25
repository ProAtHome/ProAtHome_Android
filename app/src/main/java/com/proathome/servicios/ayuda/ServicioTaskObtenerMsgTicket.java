package com.proathome.servicios.ayuda;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.proathome.fragments.FragmentTicketAyuda;
import com.proathome.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskObtenerMsgTicket extends AsyncTask<Void, Void, String> {

    public ServicioTaskObtenerMsgTicket(Context contexto, int idUsuario, int idTicket, int tipoUsuario){
        Constants.contexto_Ticket = contexto;
        Constants.idUsuario_Ticket = idUsuario;
        Constants.idTicket_Ticket = idTicket;
        Constants.tipoUsuario_Ticket = tipoUsuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        try{
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) Constants.obtenerURL_Ticket().openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();

            if(responseCode == HttpsURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                Constants.result_Ticket = stringBuffer.toString();

                bufferedReader.close();
                stringBuffer = null;
                bufferedReader = null;

            }else{
                Constants.result_Ticket = "Error: " + responseCode;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Constants.result_Ticket;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            JSONArray jsonArray = new JSONArray(s);
            JSONObject mensajes = jsonArray.getJSONObject(1);
            JSONArray jsonArrayMensajes = mensajes.getJSONArray("mensajes");
            boolean entro = true;
            for(int i = FragmentTicketAyuda.componentAdapterMsgTickets.getItemCount(); i < jsonArrayMensajes.length(); i++){
                JSONObject mensaje = jsonArrayMensajes.getJSONObject(i);
                System.out.println(mensaje);
                FragmentTicketAyuda.componentAdapterMsgTickets.add(FragmentTicketAyuda.getmInstance(
                        mensaje.getBoolean("operador") ? "Operador - Soporte" : mensaje.getString("nombreUsuario"), mensaje.getString("msg"),
                            mensaje.getBoolean("operador"), Constants.tipoUsuario_Ticket));
                if(entro)
                    FragmentTicketAyuda.recyclerView.getLayoutManager().scrollToPosition(jsonArrayMensajes.length()-1);
                entro = false;
            }

        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

}

package com.proathome.servicios.fiscales;

import android.content.Context;
import android.os.AsyncTask;
import com.proathome.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class ServicioTaskDatosFiscales extends AsyncTask<Void,Void,String> {

    private Context contexto;
    private int tipoPerfil, idUsuario, tipoPeticion;
    private String linkEstudiantes = "http://" + Constants.IP + "";
    private String linkProfesores = "http://" + Constants.IP + "";

    public ServicioTaskDatosFiscales(Context contexto, int tipoPerfil, int idUsuario, int tipoPeticion){
        this.contexto = contexto;
        this.tipoPerfil = tipoPerfil;
        this.idUsuario = idUsuario; 
        this.tipoPeticion = tipoPeticion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String resultado = null;

        if(this.tipoPeticion == Constants.GET_DATOS_FISCALES){

        }else if(this.tipoPeticion == Constants.UP_DATOS_FISCALES){

        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            JSONObject jsonObject = new JSONObject(s);
            if(this.tipoPeticion == Constants.GET_DATOS_FISCALES){

            }else if(this.tipoPeticion == Constants.UP_DATOS_FISCALES){

            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }
}

package com.proathome.controladores;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import com.proathome.fragments.ButtonFragment;
import com.proathome.ui.inicio.InicioFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class ServicioTaskSesionesEstudiante extends AsyncTask<Void, Void, String> {

    private Context httpContext;
    private ProgressDialog progressDialog;
    public String resultadoapi="";
    public String linkrequestAPI="";
    public String respuesta;
    public int idCliente;

    public ServicioTaskSesionesEstudiante(Context context, String linkAPI, int idCliente){

        this.httpContext = context;
        this.idCliente = idCliente;
        this.linkrequestAPI = linkAPI + String.valueOf(idCliente);
        System.out.println(linkrequestAPI);

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Cargando tus Clases.", "Por favor, espere...");

    }

    //FUNCIONES----------------------------------------------------------------------
    //Transformar JSON Obejct a String *******************************************
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String result =  null;
        String wsURL = linkrequestAPI;
        URL url = null;

        try{

            url = new URL(wsURL);
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
                result= sb.toString();
                respuesta = result;

            }else{

                result = new String("Error: "+ responseCode);
                respuesta = null;

            }

        }catch(MalformedURLException ex){
            ex.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;

    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
        progressDialog.dismiss();
        resultadoapi=s;

        System.out.println(resultadoapi);

        if(resultadoapi == null){

            Toast.makeText(httpContext, "Error del servidor.", Toast.LENGTH_LONG).show();

        }else{

            if(!resultadoapi.equals("null")){

                System.out.println("Se muestran...");
                try{

                    JSONObject jsonObject = new JSONObject(resultadoapi);
                    JSONArray jsonArray = jsonObject.getJSONArray("sesiones");
                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);


                        InicioFragment.myAdapter.add(ButtonFragment.getmInstance(String.valueOf(object.get("nivel")), String.valueOf(object.get("tipoClase")), String.valueOf(object.get("horario"))));

                        /*
                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(httpContext, "sesion", null, 1);
                        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                        ContentValues registro = new ContentValues();
                        registro.put("id", i);
                        registro.put("idGeneral", "1");
                        registro.put("nivel", String.valueOf(object.get("nivel")));
                        baseDeDatos.insert("clases", null, registro);
                        baseDeDatos.close();*/

                    }

                }catch(JSONException ex){

                    ex.printStackTrace();

                }

            }else{

                Toast.makeText(httpContext, "Usuario sin Sesiones.",Toast.LENGTH_LONG).show();

            }

        }

    }
}

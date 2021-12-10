package com.proathome.servicios.profesional;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.view.View;

import com.proathome.ui.MatchSesionCliente;
import com.proathome.utils.Component;
import com.proathome.utils.SweetAlert;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskInfoSesion extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private ProgressDialog progressDialog;
    private String linkAPI, linkFoto, respuestaAPI, nombre, descripcion, correo, direccion, tipo,
            horario, observaciones, foto;
    private double latitud, longitud;
    private int idSesion, idProfesional, tiempo, idSeccion, idNivel, idBloque;
    private Bitmap loadedImage;

    public ServicioTaskInfoSesion(Context contexto, String linkAPI, String linkFoto, int idSesion){
        this.contexto = contexto;
        this.linkAPI = linkAPI + "/" + idSesion;
        this.linkFoto = linkFoto;
        this.idSesion = idSesion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.contexto,"Cargando información",
                "Por favor, espere...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        String resultado = null;
        try{
            URL url = new URL(this.linkAPI);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                JsonReader jsonReader = new JsonReader(inputStreamReader);
                jsonReader.beginObject();

                while (jsonReader.hasNext()){
                    String key = jsonReader.nextName();

                    if (key.equals("nombre"))
                        this.nombre = jsonReader.nextString();
                    else if(key.equals("idProfesional"))
                        this.idProfesional = jsonReader.nextInt();
                    else if (key.equals("descripcion"))
                        this.descripcion = jsonReader.nextString();
                    else if (key.equals("correo"))
                        this.correo = jsonReader.nextString();
                    else if (key.equals("foto"))
                        this.foto = jsonReader.nextString();
                    else if (key.equals("latitud"))
                        this.latitud = jsonReader.nextDouble();
                    else if (key.equals("longitud"))
                        this.longitud = jsonReader.nextDouble();
                    else if (key.equals("lugar"))
                        this.direccion = jsonReader.nextString();
                    else if (key.equals("tiempo"))
                        this.tiempo = jsonReader.nextInt();
                    else if (key.equals("idSeccion"))
                        this.idSeccion = jsonReader.nextInt();
                    else if(key.equals("idNivel"))
                        this.idNivel = jsonReader.nextInt();
                    else if(key.equals("idBloque"))
                        this.idBloque = jsonReader.nextInt();
                    else if (key.equals("tipoServicio"))
                        this.tipo = jsonReader.nextString();
                    else if (key.equals("extras"))
                        this.observaciones = jsonReader.nextString();
                    else if (key.equals("horario"))
                        this.horario = jsonReader.nextString();
                    else
                        jsonReader.skipValue();

                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String linea = "";

                while((linea = bufferedReader.readLine()) != null){
                    stringBuffer.append(linea);
                    break;
                }

                bufferedReader.close();
                resultado = stringBuffer.toString();
                this.respuestaAPI = resultado;
            }else{
                resultado = new String("Error: " + responseCode);
                this.respuestaAPI = resultado;
            }

            URL imageUrl = null;
            try {
                imageUrl = new URL(this.linkFoto + this.foto);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                loadedImage = BitmapFactory.decodeStream(conn.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public String obtenerHorario(int tiempo){
        String horas = String.valueOf(tiempo/60) + " HRS ";
        String minutos = String.valueOf(tiempo%60) + " min";

        return horas + minutos;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        this.respuestaAPI = s;

        if(this.respuestaAPI == null){
            errorMsg("Error, ocurrió un problema con el servidor.");
        }else {
            if(!this.respuestaAPI.equals("null")){
                if(MatchSesionCliente.idProfesionalSesion == this.idProfesional || this.idProfesional != 0){
                    MatchSesionCliente.matchBTN.setVisibility(View.INVISIBLE);
                }else{
                    MatchSesionCliente.matchBTN.setVisibility(View.VISIBLE);
                }
                MatchSesionCliente.imageView.setImageBitmap(loadedImage);
                MatchSesionCliente.nombreTV.setText(this.nombre);
                MatchSesionCliente.correoTV.setText(this.correo);
                MatchSesionCliente.descripcionTV.setText(this.descripcion);
                MatchSesionCliente.direccionTV.setText("Dirección: " + this.direccion);
                MatchSesionCliente.tiempoTV.setText("Tiempo de la Sesión: " + obtenerHorario(this.tiempo));
                MatchSesionCliente.nivelTV.setText("Nivel: " + Component.getSeccion(this.idSeccion) +
                        "/" + Component.getNivel(this.idSeccion, this.idNivel) + "/" +
                            Component.getBloque(this.idBloque));
                MatchSesionCliente.tipoServicioTV.setText("Tipo de servicio: " + this.tipo);
                MatchSesionCliente.observacionesTV.setText("Observaciones: " + this.observaciones);
                MatchSesionCliente.horarioTV.setText("Horario: " + this.horario);
            }else{
                errorMsg("Sin datos, ocurrió un error.");
            }
        }
    }

    public void errorMsg(String mensaje){
        new SweetAlert(this.contexto, SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

}

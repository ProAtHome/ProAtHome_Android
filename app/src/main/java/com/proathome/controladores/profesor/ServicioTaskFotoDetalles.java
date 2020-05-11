package com.proathome.controladores.profesor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.utils.Constants;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicioTaskFotoDetalles extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String foto;
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private Bitmap loadedImage;

    public ServicioTaskFotoDetalles(Context contexto, String foto){

        this.contexto = contexto;
        this.foto = foto;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = null;

        URL imageUrl = null;
        try {
            imageUrl = new URL(this.imageHttpAddress + this.foto);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  result;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        DetallesSesionProfesorFragment.foto.setImageBitmap(this.loadedImage);
    }

}

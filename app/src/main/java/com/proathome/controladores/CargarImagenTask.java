package com.proathome.controladores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.inicioEstudiante;
import com.proathome.inicioProfesor;
import com.proathome.ui.editarPerfil.EditarPerfilFragment;
import com.proathome.utils.Constants;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CargarImagenTask extends AsyncTask<Void, Void, String> {

    private String link, nombreFoto;
    private Bitmap loadedImage;
    private int tipo;

    public CargarImagenTask(String link, String nombreFoto, int tipo){

        this.link = link;
        this.nombreFoto = nombreFoto;
        this.tipo = tipo;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        URL imageUrl = null;
        try {
            imageUrl = new URL(this.link + this.nombreFoto);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Carga exitosa";

    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
        if(this.tipo == Constants.FOTO_EDITAR_PERFIL)
            EditarPerfilFragment.ivFoto.setImageBitmap(loadedImage);
        else if (this.tipo == Constants.FOTO_PERFIL)
            inicioEstudiante.foto.setImageBitmap(loadedImage);
        else if(this.tipo == Constants.FOTO_PERFIL_PROFESOR)
            inicioProfesor.foto.setImageBitmap(loadedImage);

    }

}

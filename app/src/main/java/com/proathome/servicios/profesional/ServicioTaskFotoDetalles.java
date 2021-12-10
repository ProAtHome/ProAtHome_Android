package com.proathome.servicios.profesional;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.ui.fragments.DetallesFragment;
import com.proathome.ui.fragments.DetallesGestionarProfesionalFragment;
import com.proathome.ui.fragments.DetallesSesionProfesionalFragment;
import com.proathome.ui.fragments.PerfilFragment;
import com.proathome.utils.Constants;
import java.io.IOException;
import java.net.URL;

import java.net.HttpURLConnection;

public class ServicioTaskFotoDetalles extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String foto;
    private String imageHttpAddress = Constants.IP_80 + "/assets/img/fotoPerfil/";
    private Bitmap loadedImage;
    private  int tipo;

    public ServicioTaskFotoDetalles(Context contexto, String foto, int tipo){
        this.contexto = contexto;
        this.foto = foto;
        this.tipo = tipo;
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
        if(this.tipo == DetallesSesionProfesionalFragment.PROFESIONAL)
            DetallesSesionProfesionalFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == DetallesFragment.CLIENTE)
            DetallesFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == PerfilFragment.PERFIL_PROFESIONAL_EN_CLIENTE)
            PerfilFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == PerfilFragment.PERFIL_CLIENTE_EN_PROFESIONAL)
            PerfilFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == DetallesGestionarProfesionalFragment.GESTION_PROFESIONAL)
            DetallesGestionarProfesionalFragment.foto.setImageBitmap(this.loadedImage);
    }

}

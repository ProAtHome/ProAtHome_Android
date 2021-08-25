package com.proathome.servicios.profesor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.proathome.fragments.DetallesFragment;
import com.proathome.fragments.DetallesGestionarProfesorFragment;
import com.proathome.fragments.DetallesSesionProfesorFragment;
import com.proathome.fragments.PerfilFragment;
import com.proathome.utils.Constants;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServicioTaskFotoDetalles extends AsyncTask<Void, Void, String> {

    private Context contexto;
    private String foto;
    private String imageHttpAddress = Constants.IP + "/assets/img/fotoPerfil/";
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
            HttpsURLConnection conn = (HttpsURLConnection) imageUrl.openConnection();
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
        if(this.tipo == DetallesSesionProfesorFragment.PROFESOR)
            DetallesSesionProfesorFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == DetallesFragment.ESTUDIANTE)
            DetallesFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == PerfilFragment.PERFIL_PROFESOR_EN_ESTUDIANTE)
            PerfilFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == PerfilFragment.PERFIL_ESTUDIANTE_EN_PROFESOR)
            PerfilFragment.foto.setImageBitmap(this.loadedImage);
        else if(this.tipo == DetallesGestionarProfesorFragment.GESTION_PROFESOR)
            DetallesGestionarProfesorFragment.foto.setImageBitmap(this.loadedImage);
    }

}

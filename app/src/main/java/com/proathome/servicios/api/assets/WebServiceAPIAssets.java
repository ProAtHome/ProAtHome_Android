package com.proathome.servicios.api.assets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class WebServiceAPIAssets extends AsyncTask<Void, Void, Bitmap> {

    public AsyncResponseAPIAssets delegate = null;
    private String urlApiAsset, nombre;
    public static int GET = 1;
    public static int POST = 2;
    public static int PUT = 3;
    private Bitmap loadedImage;

    public WebServiceAPIAssets(AsyncResponseAPIAssets delegate, String urlApiAsset, String nombre){
        this.delegate = delegate;
        this.urlApiAsset = urlApiAsset;
        this.nombre = nombre;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        URL imageUrl = null;
        try {
            imageUrl = new URL(this.urlApiAsset + this.nombre);
            HttpsURLConnection conn = (HttpsURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedImage;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        delegate.processFinish(bitmap);
    }


    public String getPostDataString(JSONObject params) throws Exception {
        return params.toString();
    }
}

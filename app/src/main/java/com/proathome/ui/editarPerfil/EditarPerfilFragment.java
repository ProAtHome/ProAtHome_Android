package com.proathome.ui.editarPerfil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioTaskBancoEstudiante;
import com.proathome.controladores.estudiante.ServicioTaskPerfilEstudiante;
import com.proathome.controladores.estudiante.ServicioTaskUpCuentaEstudiante;
import com.proathome.controladores.estudiante.ServicioTaskUpPerfilEstudiante;
import com.proathome.utils.Constants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel editarPerfilViewModel;
    private String linkRESTCargarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/perfilCliente";
    private String linkRESTDatosBancarios = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerDatosBancarios";
    private String linkRESTActualizarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/informacionPerfil";
    private String linkRESTActualizarBanco = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/actualizarCuentaCliente";
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private String linkFoto = "http://" + Constants.IP + "/ProAtHome/assets/lib/ActualizarFotoAndroid.php";
    private Unbinder mUnbinder;
    private ServicioTaskPerfilEstudiante perfilEstudiante;
    private ServicioTaskBancoEstudiante bancoEstudiante;
    private ServicioTaskUpPerfilEstudiante actualizarPerfil;
    private ServicioTaskUpCuentaEstudiante actualizarBanco;
    public static TextInputEditText etNombre;
    public static TextInputEditText etEdad;
    public static TextInputEditText etDesc;
    public static TextInputEditText etDireccion;
    public static TextInputEditText etCuenta;
    public static TextInputEditText etBanco;
    public static TextInputEditText etTipoDePago;
    public static ImageView ivFoto;
    private static final int PICK_IMAGE = 100;
    public static final int RESULT_OK = -1;
    public int idEstudiante;
    private String correo;
    @BindView(R.id.bottomNavigationPerfil)
    BottomNavigationView bottomNavigationPerfil;
    @BindView(R.id.btnFoto)
    Button btnFoto;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvEdad)
    TextView tvEdad;
    @BindView(R.id.tvDescripcion)
    TextView tvDesc;
    @BindView(R.id.btnActualizarInfo)
    Button btnActualizarInfo;
    @BindView(R.id.tvInfoBancaria)
    TextView tvInfoBancaria;
    @BindView(R.id.tvTipoDePago)
    TextView tvTipoDePago;
    @BindView(R.id.tvBanco)
    TextView tvBanco;
    @BindView(R.id.tvCuenta)
    TextView tvCuenta;
    @BindView(R.id.tvDireccion)
    TextView tvDireccion;
    @BindView(R.id.btnActualizarInfoBancaria)
    Button btnActualizarInfoBancaria;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    private String ID_ESTUDIANTE = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        editarPerfilViewModel = ViewModelProviders.of(this).get(EditarPerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante, correo FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            this.idEstudiante = fila.getInt(0);
            this.ID_ESTUDIANTE = String.valueOf(fila.getInt(0));
            this.correo = fila.getString(1);

        }else{

            baseDeDatos.close();

        }

        btnActualizarInfo.setOnClickListener(view -> {

            actualizarPerfil = new ServicioTaskUpPerfilEstudiante(getContext(), linkRESTActualizarPerfil, this.idEstudiante, etNombre.getText().toString(), this.correo, Integer.valueOf(etEdad.getText().toString()), etDesc.getText().toString());
            actualizarPerfil.execute();
            uploadImage();

        });

        btnActualizarInfoBancaria.setOnClickListener(view -> {

            actualizarBanco = new ServicioTaskUpCuentaEstudiante(getContext(), linkRESTActualizarBanco, this.idEstudiante, etTipoDePago.getText().toString(), etBanco.getText().toString(), etCuenta.getText().toString(), etDireccion.getText().toString());
            actualizarBanco.execute();

        });

        bottomNavigationPerfil.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.action_informacion:
                    ivFoto.setVisibility(View.VISIBLE);
                    btnFoto.setVisibility(View.VISIBLE);
                    tvNombre.setVisibility(View.VISIBLE);
                    etNombre.setVisibility(View.VISIBLE);
                    tvEdad.setVisibility(View.VISIBLE);
                    etEdad.setVisibility(View.VISIBLE);
                    tvDesc.setVisibility(View.VISIBLE);
                    etDesc.setVisibility(View.VISIBLE);
                    btnActualizarInfo.setVisibility(View.VISIBLE);
                    tvInfoBancaria.setVisibility(View.INVISIBLE);
                    tvTipoDePago.setVisibility(View.INVISIBLE);
                    etTipoDePago.setVisibility(View.INVISIBLE);
                    tvBanco.setVisibility(View.INVISIBLE);
                    etBanco.setVisibility(View.INVISIBLE);
                    tvCuenta.setVisibility(View.INVISIBLE);
                    etCuenta.setVisibility(View.INVISIBLE);
                    tvDireccion.setVisibility(View.INVISIBLE);
                    etDireccion.setVisibility(View.INVISIBLE);
                    btnActualizarInfoBancaria.setVisibility(View.INVISIBLE);
                    return true;

                case R.id.action_datos:
                    ivFoto.setVisibility(View.INVISIBLE);
                    btnFoto.setVisibility(View.INVISIBLE);
                    tvNombre.setVisibility(View.INVISIBLE);
                    etNombre.setVisibility(View.INVISIBLE);
                    tvEdad.setVisibility(View.INVISIBLE);
                    etEdad.setVisibility(View.INVISIBLE);
                    tvDesc.setVisibility(View.INVISIBLE);
                    etDesc.setVisibility(View.INVISIBLE);
                    btnActualizarInfo.setVisibility(View.INVISIBLE);
                    tvInfoBancaria.setVisibility(View.VISIBLE);
                    tvTipoDePago.setVisibility(View.VISIBLE);
                    etTipoDePago.setVisibility(View.VISIBLE);
                    tvBanco.setVisibility(View.VISIBLE);
                    etBanco.setVisibility(View.VISIBLE);
                    tvCuenta.setVisibility(View.VISIBLE);
                    etCuenta.setVisibility(View.VISIBLE);
                    tvDireccion.setVisibility(View.VISIBLE);
                    etDireccion.setVisibility(View.VISIBLE);
                    btnActualizarInfoBancaria.setVisibility(View.VISIBLE);
                    return true;

            }

            return true;

        });

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNombre = getView().findViewById(R.id.etNombre);
        etEdad = getView().findViewById(R.id.etEdad);
        etDesc = getView().findViewById(R.id.etDesc);
        etTipoDePago = getView().findViewById(R.id.etTipoDePago);
        etBanco = getView().findViewById(R.id.etBanco);
        etCuenta = getView().findViewById(R.id.etCuenta);
        etDireccion = getView().findViewById(R.id.etDireccion);
        ivFoto = getView().findViewById(R.id.ivFoto);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            this.idEstudiante = fila.getInt(0);
            perfilEstudiante = new ServicioTaskPerfilEstudiante(getContext(), linkRESTCargarPerfil, this.imageHttpAddress, this.idEstudiante, Constants.INFO_PERFIl_EDITAR);
            perfilEstudiante.execute();
            bancoEstudiante = new ServicioTaskBancoEstudiante(getContext(), linkRESTDatosBancarios, this.idEstudiante);
            bancoEstudiante.execute();

        }else{

            baseDeDatos.close();

        }

        baseDeDatos.close();


    }

    @OnClick(R.id.btnFoto)
    public void onClickFoto(View view){

        showFileChooser();

    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, linkFoto,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);

                //Obtener el nombre de la imagen
                String nombre = ID_ESTUDIANTE + "_perfil";

                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();

                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
                params.put("idCliente", ID_ESTUDIANTE);

                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                ivFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }

}
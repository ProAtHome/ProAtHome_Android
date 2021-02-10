package com.proathome.ui.editarPerfilProfesor;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.profesor.AdminSQLiteOpenHelperProfesor;
import com.proathome.servicios.profesor.ServicioTaskBancoProfesor;
import com.proathome.servicios.profesor.ServicioTaskPerfilProfesor;
import com.proathome.servicios.profesor.ServicioTaskReportes;
import com.proathome.servicios.profesor.ServicioTaskUpCuentaProfesor;
import com.proathome.servicios.profesor.ServicioTaskUpPerfilProfesor;
import com.proathome.utils.Constants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditarPerfilProfesorFragment extends Fragment {

    private String linkRESTCargarPerfil = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/perfilProfesor";
    private String linkRESTDatosBancarios = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/obtenerDatosBancarios";
    private String linkRESTActualizarPerfil = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/informacionPerfil";
    private String linkRESTActualizarBanco = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/profesor/actualizarCuenta";
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private String linkFoto = "http://" + Constants.IP + "/ProAtHome/assets/lib/ActualizarFotoProfesorAndroid.php";
    private Unbinder mUnbinder;
    private ServicioTaskPerfilProfesor perfilEstudiante;
    private ServicioTaskBancoProfesor bancoEstudiante;
    private ServicioTaskUpPerfilProfesor actualizarPerfil;
    private ServicioTaskUpCuentaProfesor actualizarBanco;
    public static TextInputEditText etNombre;
    public static TextInputEditText etCorreo;
    public static TextInputEditText etCelular;
    public static TextInputEditText etTelefono;
    public static TextInputEditText etDireccion;
    public static TextInputEditText etDesc;
    public static TextInputEditText etClabe;
    public static TextInputEditText etBanco;
    public static TextInputEditText etTitular;
    public static TextView tvAviso;
    public static ImageView imgAviso;
    public static ImageView ivFoto;
    public static MaterialCardView cardValoracion;
    private static final int PICK_IMAGE = 100;
    public static final int RESULT_OK = -1;
    public int idProfesor;
    private String correo;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    private String ID_PROFESOR = "";
    @BindView(R.id.bottomNavigationPerfil)
    BottomNavigationView bottomNavigationPerfil;
    @BindView(R.id.btnFoto)
    Button btnFoto;
    @BindView(R.id.tvNombre)
    TextView tvNombre;
    @BindView(R.id.tvCorreo)
    TextView tvCorreo;
    @BindView(R.id.tvCelular)
    TextView tvCelular;
    @BindView(R.id.tvTelefono)
    TextView tvTelefono;
    @BindView(R.id.tvDireccion)
    TextView tvDireccion;
    @BindView(R.id.tvDescripcion)
    TextView tvDesc;
    @BindView(R.id.btnActualizarInfo)
    Button btnActualizarInfo;
    @BindView(R.id.tvInfoBancaria)
    TextView tvInfoBancaria;
    @BindView(R.id.tvTitular)
    TextView tvTitular;
    @BindView(R.id.tvBanco)
    TextView tvBanco;
    @BindView(R.id.tvClabe)
    TextView tvClabe;
    @BindView(R.id.btnActualizarInfoBancaria)
    Button btnActualizarInfoBancaria;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_editar_perfil_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(getContext(), "sesionProfesor",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor, correo FROM sesionProfesor WHERE id = " + 1,
                null);

        if(fila.moveToFirst()){
            this.idProfesor = fila.getInt(0);
            this.ID_PROFESOR = String.valueOf(fila.getInt(0));
            this.correo = fila.getString(1);
        }else{
            baseDeDatos.close();
        }

        /*
        btnActualizarInfo.setOnClickListener(view -> {
            actualizarPerfil = new ServicioTaskUpPerfilProfesor(getContext(), linkRESTActualizarPerfil,
                    this.idProfesor, etNombre.getText().toString(), this.correo,
                        Integer.valueOf(etEdad.getText().toString()), etDesc.getText().toString());
            actualizarPerfil.execute();
            uploadImage();
        });*/

        btnActualizarInfoBancaria.setOnClickListener(view -> {
            actualizarBanco = new ServicioTaskUpCuentaProfesor(getContext(), linkRESTActualizarBanco,
                    this.idProfesor, etTitular.getText().toString(), etBanco.getText().toString(),
                        etClabe.getText().toString());
            actualizarBanco.execute();
        });

        bottomNavigationPerfil.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_informacion:
                    ivFoto.setVisibility(View.VISIBLE);
                    btnFoto.setVisibility(View.VISIBLE);
                    tvNombre.setVisibility(View.VISIBLE);
                    etNombre.setVisibility(View.VISIBLE);
                    tvCorreo.setVisibility(View.VISIBLE);
                    etCorreo.setVisibility(View.VISIBLE);
                    tvCelular.setVisibility(View.VISIBLE);
                    etCelular.setVisibility(View.VISIBLE);
                    tvTelefono.setVisibility(View.VISIBLE);
                    etTelefono.setVisibility(View.VISIBLE);
                    tvDireccion.setVisibility(View.VISIBLE);
                    etDireccion.setVisibility(View.VISIBLE);
                    tvDesc.setVisibility(View.VISIBLE);
                    etDesc.setVisibility(View.VISIBLE);
                    btnActualizarInfo.setVisibility(View.VISIBLE);
                    tvInfoBancaria.setVisibility(View.INVISIBLE);
                    tvTitular.setVisibility(View.INVISIBLE);
                    etTitular.setVisibility(View.INVISIBLE);
                    tvBanco.setVisibility(View.INVISIBLE);
                    etBanco.setVisibility(View.INVISIBLE);
                    tvClabe.setVisibility(View.INVISIBLE);
                    etClabe.setVisibility(View.INVISIBLE);
                    btnActualizarInfoBancaria.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.action_datos:
                    ivFoto.setVisibility(View.INVISIBLE);
                    btnFoto.setVisibility(View.INVISIBLE);
                    tvNombre.setVisibility(View.INVISIBLE);
                    etNombre.setVisibility(View.INVISIBLE);
                    tvCorreo.setVisibility(View.INVISIBLE);
                    etCorreo.setVisibility(View.INVISIBLE);
                    tvCelular.setVisibility(View.INVISIBLE);
                    etCelular.setVisibility(View.INVISIBLE);
                    tvTelefono.setVisibility(View.INVISIBLE);
                    etTelefono.setVisibility(View.INVISIBLE);
                    tvDireccion.setVisibility(View.INVISIBLE);
                    etDireccion.setVisibility(View.INVISIBLE);
                    tvDesc.setVisibility(View.INVISIBLE);
                    etDesc.setVisibility(View.INVISIBLE);
                    btnActualizarInfo.setVisibility(View.INVISIBLE);
                    tvInfoBancaria.setVisibility(View.VISIBLE);
                    tvTitular.setVisibility(View.VISIBLE);
                    etTitular.setVisibility(View.VISIBLE);
                    tvBanco.setVisibility(View.VISIBLE);
                    etBanco.setVisibility(View.VISIBLE);
                    tvClabe.setVisibility(View.VISIBLE);
                    etClabe.setVisibility(View.VISIBLE);
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
        etCorreo = getView().findViewById(R.id.etCorreo);
        etCelular = getView().findViewById(R.id.etCelular);
        etTelefono = getView().findViewById(R.id.etTelefono);
        etDireccion = getView().findViewById(R.id.etDireccion);
        etDesc = getView().findViewById(R.id.etDesc);
        etTitular = getView().findViewById(R.id.etTitular);
        etBanco = getView().findViewById(R.id.etBanco);
        etClabe = getView().findViewById(R.id.etClabe);
        etDireccion = getView().findViewById(R.id.etDireccion);
        ivFoto = getView().findViewById(R.id.ivFoto);
        tvAviso = getView().findViewById(R.id.tvAviso);
        imgAviso = getView().findViewById(R.id.imgAviso);
        cardValoracion = getView().findViewById(R.id.cardValoracion);

        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(getContext(), "sesionProfesor",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            this.idProfesor = fila.getInt(0);
            ServicioTaskReportes reportes = new ServicioTaskReportes(getContext(), Constants.TIPO_PROFESOR, this.idProfesor);
            reportes.execute();
            perfilEstudiante = new ServicioTaskPerfilProfesor(getContext(), linkRESTCargarPerfil,
                    this.imageHttpAddress, this.idProfesor, Constants.INFO_PERFIl_EDITAR);
            perfilEstudiante.execute();
            bancoEstudiante = new ServicioTaskBancoProfesor(getContext(), linkRESTDatosBancarios, this.idProfesor);
            bancoEstudiante.execute();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

    }

    @OnClick(R.id.btnFoto)
    public void onClickFoto(){
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
                response -> {
                },
                error -> {
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);
                //Obtener el nombre de la imagen
                String nombre = ID_PROFESOR + "_perfilProfesor";
                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
                params.put("idProfesor", ID_PROFESOR);
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
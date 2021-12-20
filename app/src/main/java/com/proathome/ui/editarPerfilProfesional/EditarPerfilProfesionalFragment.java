package com.proathome.ui.editarPerfilProfesional;

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
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.ui.fragments.DatosFiscalesFragment;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.servicios.profesional.ServicioTaskReportes;
import com.proathome.utils.Constants;
import com.proathome.utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditarPerfilProfesionalFragment extends Fragment {

    private Unbinder mUnbinder;
    public static TextView tvNombre;
    public static TextView tvCorreo;
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
    public int idProfesional;
    private String correo;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    private String ID_PROFESIONAL = "";
    @BindView(R.id.bottomNavigationPerfil)
    BottomNavigationView bottomNavigationPerfil;
    @BindView(R.id.btnFoto)
    Button btnFoto;
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
    @BindView(R.id.btnActualizarFiscales)
    MaterialButton btnActualizarFiscales;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_editar_perfil_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(getContext(), "sesionProfesional",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional, correo FROM sesionProfesional WHERE id = " + 1,
                null);

        if(fila.moveToFirst()){
            this.idProfesional = fila.getInt(0);
            this.ID_PROFESIONAL = String.valueOf(fila.getInt(0));
            this.correo = fila.getString(1);
        }else{
            baseDeDatos.close();
        }

        btnActualizarInfo.setOnClickListener(view -> {
            actualizarPerfil();
            uploadImage();
        });

        bottomNavigationPerfil.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_informacion:
                    ivFoto.setVisibility(View.VISIBLE);
                    btnFoto.setVisibility(View.VISIBLE);
                    tvNombre.setVisibility(View.VISIBLE);
                    tvCorreo.setVisibility(View.VISIBLE);
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
                    btnActualizarFiscales.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.action_datos:
                    ivFoto.setVisibility(View.INVISIBLE);
                    btnFoto.setVisibility(View.INVISIBLE);
                    tvNombre.setVisibility(View.INVISIBLE);
                    tvCorreo.setVisibility(View.INVISIBLE);
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
                    btnActualizarFiscales.setVisibility(View.VISIBLE);
                    return true;
            }
            return true;

        });

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNombre = getView().findViewById(R.id.tvNombre);
        tvCorreo = getView().findViewById(R.id.tvCorreo);
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

        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(getContext(), "sesionProfesional",
                null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            this.idProfesional = fila.getInt(0);
            ServicioTaskReportes reportes = new ServicioTaskReportes(getContext(), Constants.TIPO_PROFESIONAL, this.idProfesional);
            reportes.execute();
            getDatosPerfil();
            getDatosBanco();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

    }

    private void setImageBitmap(String foto){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            ivFoto.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, foto);
        webServiceAPIAssets.execute();
    }

    private void getDatosPerfil(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            if(response != null){
                if (!response.equals("null")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        tvNombre.setText("Nombre: " + jsonObject.getString("nombre"));
                        tvCorreo.setText("Correo: " + jsonObject.getString("correo"));
                        etCelular.setText(jsonObject.getString("celular"));
                        etTelefono.setText(jsonObject.getString("telefonoLocal"));
                        etDireccion.setText(jsonObject.getString("direccion"));
                        etDesc.setText(jsonObject.getString("descripcion"));
                        setImageBitmap(jsonObject.getString("foto"));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } else
                    errorMsg("Error en el perfil, intente ingresar más tarde.");
            }else
                errorMsg("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_PERFIL_PROFESIONAL + this.idProfesional, WebServicesAPI.GET,  null);
        webServicesAPI.execute();
    }

    public void errorMsg(String mensaje){
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    private void actualizarPerfil(){
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", this.idProfesional);
            parametrosPUT.put("celular", etCelular.getText().toString());
            parametrosPUT.put("telefonoLocal", etTelefono.getText().toString());
            parametrosPUT.put("direccion", etDireccion.getText().toString());
            parametrosPUT.put("descripcion", etDesc.getText().toString());
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta"))
                        msgSweet("¡GENIAL!", jsonObject.getString("mensaje"), SweetAlert.SUCCESS_TYPE);
                    else
                        msgSweet("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.WARNING_TYPE);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.ACTUALIZAR_PERFIL_PROFESIONAL, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDatosBanco(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject jsonDatos = jsonObject.getJSONObject("mensaje");
                    if(jsonDatos.getBoolean("existe")){
                        etTitular.setText(jsonDatos.getString("nombreTitular"));
                        etBanco.setText(jsonDatos.getString("banco"));
                        etClabe.setText(jsonDatos.getString("clabe"));
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, APIEndPoints.DATOS_BANCARIOS_PROFESIONAL + this.idProfesional, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void actualizarDatosBancarios(){
        if(!etTitular.getText().toString().trim().equalsIgnoreCase("") && !etBanco.getText().toString().trim().equalsIgnoreCase("")
            && !etClabe.getText().toString().trim().equalsIgnoreCase("")){
            upDatosBancoService();
        }else{
            new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.PROFESIONAL)
                    .setTitleText("¡ERROR!")
                    .setContentText("Llena todos los campos correctamente.")
                    .show();
        }
    }

    private void upDatosBancoService(){
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idProfesional", this.idProfesional);
            parametrosPUT.put("nombreTitular", etTitular.getText().toString());
            parametrosPUT.put("banco",  etBanco.getText().toString());
            parametrosPUT.put("clabe", etClabe.getText().toString().trim());
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("respuesta"))
                        msgSweet("¡GENIAL!", jsonObject.getString("mensaje"), SweetAlert.SUCCESS_TYPE);
                    else
                        msgSweet("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.ERROR_TYPE);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }, APIEndPoints.UPDATE_CUENTA_PROFESIONAL, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void msgSweet(String titulo, String mensaje, int tipo){
        new SweetAlert(getContext(), tipo, SweetAlert.PROFESIONAL)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public void actualizarFiscales(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_PROFESIONAL);
        bundle.putInt("idUsuario", this.idProfesional);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DatosFiscalesFragment datosFiscalesFragment = new DatosFiscalesFragment();
        datosFiscalesFragment.setArguments(bundle);
        datosFiscalesFragment.show(fragmentTransaction, "DATOS FISCALES");
    }

    @OnClick({R.id.btnFoto, R.id.btnActualizarFiscales, R.id.btnActualizarInfoBancaria})
    public void onClickFoto(View view){
        switch (view.getId()){
            case R.id.btnFoto:
                showFileChooser();
                break;
            case R.id.btnActualizarFiscales:
                actualizarFiscales();
                break;
            case R.id.btnActualizarInfoBancaria:
                actualizarDatosBancarios();
                break;
        }
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIEndPoints.UP_FOTO_PROFESIONAL,
                response -> {
                },
                error -> {
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);
                //Obtener el nombre de la imagen
                String nombre = ID_PROFESIONAL + "_perfilProfesional";
                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
                params.put("idProfesional", ID_PROFESIONAL);
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
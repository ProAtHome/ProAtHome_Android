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
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.ui.fragments.DatosFiscalesFragment;
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.cliente.AdminSQLiteOpenHelper;
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
import mx.openpay.android.validation.CardValidator;

public class EditarPerfilFragment extends Fragment {

    private String linkFoto = Constants.IP_80 + "/assets/lib/ActualizarFotoAndroid.php";
    private Unbinder mUnbinder;
    public static TextView tvNombre;
    public static TextView tvCorreo;
    public static TextInputEditText etCelular;
    public static TextInputEditText etTelefono;
    public static TextInputEditText etDireccion;
    public static TextInputEditText etDesc;
    public static TextInputEditText etNombreTitular;
    public static TextInputEditText etTarjeta;
    public static TextInputEditText etMes;
    public static TextInputEditText etAño;
    public static TextView tvAviso;
    public static ImageView imgAviso;
    public static ImageView ivFoto;
    public static MaterialCardView cardValoracion;
    public static final int RESULT_OK = -1;
    public int idCliente;
    private String correo;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    private String ID_CLIENTE = "";
    private WebServicesAPI webServicesAPI;
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
    @BindView(R.id.tvNombreTitular)
    TextView tvNombreTitular;
    @BindView(R.id.tvTarjeta)
    TextView tvTarjeta;
    @BindView(R.id.tvFecha)
    TextView tvFecha;
    @BindView(R.id.tvMes)
    TextView tvMes;
    @BindView(R.id.tvAño)
    TextView tvAño;
    @BindView(R.id.tvInfoBancaria)
    TextView tvInfoBancaria;
    @BindView(R.id.btnActualizarInfoBancaria)
    Button btnActualizarInfoBancaria;
    @BindView(R.id.btnActualizarFiscales)
    MaterialButton btnActualizarFiscales;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente, correo FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            this.idCliente = fila.getInt(0);
            this.ID_CLIENTE = String.valueOf(fila.getInt(0));
            this.correo = fila.getString(1);
        }else{
            baseDeDatos.close();
        }

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
                    tvNombreTitular.setVisibility(View.INVISIBLE);
                    tvTarjeta.setVisibility(View.INVISIBLE);
                    etAño.setVisibility(View.INVISIBLE);
                    tvFecha.setVisibility(View.INVISIBLE);
                    tvAño.setVisibility(View.INVISIBLE);
                    tvInfoBancaria.setVisibility(View.INVISIBLE);
                    tvMes.setVisibility(View.INVISIBLE);
                    etMes.setVisibility(View.INVISIBLE);
                    etTarjeta.setVisibility(View.INVISIBLE);
                    etNombreTitular.setVisibility(View.INVISIBLE);
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
                    tvNombreTitular.setVisibility(View.VISIBLE);
                    tvTarjeta.setVisibility(View.VISIBLE);
                    etAño.setVisibility(View.VISIBLE);
                    tvFecha.setVisibility(View.VISIBLE);
                    tvAño.setVisibility(View.VISIBLE);
                    tvInfoBancaria.setVisibility(View.VISIBLE);
                    tvMes.setVisibility(View.VISIBLE);
                    etMes.setVisibility(View.VISIBLE);
                    etTarjeta.setVisibility(View.VISIBLE);
                    etNombreTitular.setVisibility(View.VISIBLE);
                    btnActualizarInfoBancaria.setVisibility(View.VISIBLE);
                    btnActualizarFiscales.setVisibility(View.VISIBLE);
                    return true;
            }
            return true;
        });

        return root;

    }

    public boolean camposValidosBanco(){
        if(!etNombreTitular.getText().toString().trim().equalsIgnoreCase("") &&
                !etTarjeta.getText().toString().trim().equalsIgnoreCase("") &&
                    !etMes.getText().toString().trim().equalsIgnoreCase("") &&
                        !etAño.getText().toString().trim().equalsIgnoreCase(""))
            return true;
        else
            return false;
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
        etNombreTitular = getView().findViewById(R.id.etNombreTitular);
        etTarjeta = getView().findViewById(R.id.etTarjeta);
        etMes = getView().findViewById(R.id.etMes);
        etAño = getView().findViewById(R.id.etAño);
        ivFoto = getView().findViewById(R.id.ivFoto);
        tvAviso = getView().findViewById(R.id.tvAviso);
        imgAviso = getView().findViewById(R.id.imgAviso);
        cardValoracion = getView().findViewById(R.id.cardValoracion);

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        Cursor fila = baseDeDatos.rawQuery("SELECT idCliente FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){
            this.idCliente = fila.getInt(0);
            getReportes();
            getDatosPerfil();
            getDatosBanco();
        }else{
            baseDeDatos.close();
        }

        baseDeDatos.close();

    }

    private void getDatosBanco(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                    if(mensaje.getBoolean("existe")){
                        etNombreTitular.setText(mensaje.getString("nombreTitular"));
                        etTarjeta.setText(mensaje.getString("tarjeta"));
                        etMes.setText(mensaje.getString("mes"));
                        etAño.setText(mensaje.getString("ano"));
                    }else
                        infoMsg("¡AVISO!", "No tienes datos bancarios registrados", SweetAlert.WARNING_TYPE);
                }else
                    infoMsg("¡ERROR", jsonObject.get("mensaje").toString(), SweetAlert.ERROR_TYPE);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_DATOS_BANCO_CLIENTE + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void infoMsg(String titulo, String mensaje, int tipo){
        new SweetAlert(getContext(), tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    private void getReportes(){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta")){
                    JSONObject mensaje = jsonObject.getJSONObject("mensaje");
                    if(mensaje.getInt("reportes") == 0){
                        //ocultamos avisos
                        tvAviso.setVisibility(View.INVISIBLE);
                        imgAviso.setVisibility(View.INVISIBLE);
                        cardValoracion.setVisibility(View.INVISIBLE);
                    }else if(mensaje.getInt("reportes") > 0){
                        int numReportes = mensaje.getInt("reportes");
                        String descripcion = mensaje.getString("aviso");
                        //mostramos aviso
                        tvAviso.setVisibility(View.VISIBLE);
                        imgAviso.setVisibility(View.VISIBLE);
                        cardValoracion.setVisibility(View.VISIBLE);
                        tvAviso.setText("Aviso No. " + numReportes + ": " + descripcion);
                    }
                }else
                    Toast.makeText(getContext(), "Error al obtener reportes.", Toast.LENGTH_SHORT).show();
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.GET_REPORTES_CLIENTE + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
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
                if(!response.equals("null")){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        tvNombre.setText("Nombre: " + jsonObject.getString("nombre"));
                        tvCorreo.setText("Correo: " + jsonObject.getString("correo"));
                        etCelular.setText(jsonObject.getString("celular"));
                        etTelefono.setText(jsonObject.getString("telefonoLocal"));
                        etDireccion.setText(jsonObject.getString("direccion"));
                        etDesc.setText(jsonObject.getString("descripcion"));
                        setImageBitmap(jsonObject.getString("foto"));
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }else
                    errorMsg("Error en el perfil, intente ingresar más tarde.");
            }else
                errorMsg("Error del servidor, intente ingresar más tarde.");
        }, APIEndPoints.GET_PERFIL_CLIENTE + this.idCliente, WebServicesAPI.GET, null);
        webServicesAPI.execute();
    }

    public void errorMsg(String mensaje){
        new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, SweetAlert.CLIENTE)
                .setTitleText("¡ERROR!")
                .setContentText(mensaje)
                .show();
    }

    public void actualizarDatosBancarios(){
        if(camposValidosBanco()){
            if(CardValidator.validateHolderName(etNombreTitular.getText().toString())){
                if(CardValidator.validateNumber(etTarjeta.getText().toString())){
                    if(CardValidator.validateExpiryDate(Integer.parseInt(etMes.getText().toString()),
                            Integer.parseInt(etAño.getText().toString()))){
                                upCuentaCliente();
                    }else
                        mensaje("¡ERROR!", "Fecha de expiración no válida.", SweetAlert.ERROR_TYPE);
                }else
                    mensaje("¡ERROR!","Tarjeta no válida.", SweetAlert.ERROR_TYPE);
            }else
                mensaje("¡ERROR!", "Nombre del titular no válido.", SweetAlert.ERROR_TYPE);
        }else
            mensaje("¡ERROR!","Llena todos los campos correctamente.", SweetAlert.ERROR_TYPE);
    }

    private void upCuentaCliente(){
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idCliente", this.idCliente);
            parametrosPUT.put("nombreTitular", etNombreTitular.getText().toString());
            parametrosPUT.put("tarjeta", etTarjeta.getText().toString());
            parametrosPUT.put("mes", etMes.getText().toString());
            parametrosPUT.put("ano", etAño.getText().toString());
            WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
                if(response.equalsIgnoreCase("Actualización exitosa."))
                    mensaje("¡GENIAL!", "Datos actualizados correctamente.",  SweetAlert.SUCCESS_TYPE);
                else
                    mensaje("¡OH NO!", response, SweetAlert.WARNING_TYPE);
            }, APIEndPoints.UPDATE_CUENTA_CLIENTE, WebServicesAPI.PUT, parametrosPUT);
            webServicesAPI.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void actualizarFiscales(){
        Bundle bundle = new Bundle();
        bundle.putInt("tipoPerfil", Constants.TIPO_USUARIO_CLIENTE);
        bundle.putInt("idUsuario", this.idCliente);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DatosFiscalesFragment datosFiscalesFragment = new DatosFiscalesFragment();
        datosFiscalesFragment.setArguments(bundle);
        datosFiscalesFragment.show(fragmentTransaction, "DATOS FISCALES");
    }

    public void actualizarPerfil(){
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("idCliente", idCliente);
            parametros.put("celular", etCelular.getText().toString().trim());
            parametros.put("telefonoLocal", etTelefono.getText().toString().trim());
            parametros.put("direccion", etDireccion.getText().toString());
            parametros.put("descripcion", etDesc.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        webServicesAPI = new WebServicesAPI(response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("respuesta"))
                    mensaje("¡GENIAL!", jsonObject.getString("mensaje"), SweetAlert.SUCCESS_TYPE);
                else
                    mensaje("¡ERROR!", jsonObject.getString("mensaje"), SweetAlert.WARNING_TYPE);
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }, APIEndPoints.ACTUALIZAR_PERFIL, WebServicesAPI.PUT, parametros);
        webServicesAPI.execute();
        uploadImage();
    }

    @OnClick({R.id.btnFoto, R.id.btnActualizarFiscales, R.id.btnActualizarInfoBancaria, R.id.btnActualizarInfo})
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
            case R.id.btnActualizarInfo:
                actualizarPerfil();
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
                String nombre = ID_CLIENTE + "_perfil";
                //Creación de parámetros
                Map<String,String> params = new Hashtable<>();
                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
                params.put("idCliente", ID_CLIENTE);
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

    public void mensaje(String titulo, String mensaje, int tipo){
        new SweetAlert(getContext(), tipo, SweetAlert.CLIENTE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
package com.proathome.Views.cliente.navigator.editarPerfil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilPresenter;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilView;
import com.proathome.Presenters.cliente.EditarPerfilPresenterImpl;
import com.proathome.R;
import com.proathome.Views.fragments_compartidos.DatosFiscalesFragment;
import com.proathome.Utils.Constants;
import com.proathome.Utils.SharedPreferencesManager;
import com.proathome.Utils.SweetAlert;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mx.openpay.android.validation.CardValidator;

public class EditarPerfilFragment extends Fragment implements EditarPerfilView {

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
    private int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;
    private EditarPerfilPresenter editarPerfilPresenter;

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

        editarPerfilPresenter = new EditarPerfilPresenterImpl(this);

        this.idCliente = SharedPreferencesManager.getInstance(getContext()).getIDCliente();
        this.correo = SharedPreferencesManager.getInstance(getContext()).getCorreoCliente();

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

        editarPerfilPresenter.getReportes(this.idCliente, SharedPreferencesManager.getInstance(getContext()).getTokenCliente());
        editarPerfilPresenter.getDatosPerfil(this.idCliente, SharedPreferencesManager.getInstance(getContext()).getTokenCliente());
        editarPerfilPresenter.getDatosBanco(this.idCliente, SharedPreferencesManager.getInstance(getContext()).getTokenCliente());
    }

    public void actualizarDatosBancarios(){
        if(camposValidosBanco()){
            if(CardValidator.validateHolderName(etNombreTitular.getText().toString())){
                if(CardValidator.validateNumber(etTarjeta.getText().toString())){
                    if(CardValidator.validateExpiryDate(Integer.parseInt(etMes.getText().toString()),
                            Integer.parseInt(etAño.getText().toString()))){
                                upCuentaCliente();
                    }else
                        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Fecha de expiración no válida.", false, null, null);
                }else
                    SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Tarjeta no válida.", false, null, null);
            }else
                SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Nombre del titular no válido.", false, null, null);
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos correctamente.", false, null, null);
    }

    private void upCuentaCliente(){
        JSONObject parametrosPUT = new JSONObject();
        try {
            parametrosPUT.put("idCliente", this.idCliente);
            parametrosPUT.put("nombreTitular", etNombreTitular.getText().toString());
            parametrosPUT.put("tarjeta", etTarjeta.getText().toString());
            parametrosPUT.put("mes", etMes.getText().toString());
            parametrosPUT.put("ano", etAño.getText().toString());
            editarPerfilPresenter.updateCuentaCliente(parametrosPUT);
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
            editarPerfilPresenter.updatePerfil(parametros, getContext(), this.idCliente);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
            editarPerfilPresenter.getBitmapMedia(data, getContext().getContentResolver());
    }

    @Override
    public void showError(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡ERROR!", mensaje, false, null, null);
    }

    @Override
    public void setVisibilityReportes(boolean visibilityReportes, String mensaje) {
        if(visibilityReportes){
            tvAviso.setVisibility(View.VISIBLE);
            imgAviso.setVisibility(View.VISIBLE);
            cardValoracion.setVisibility(View.VISIBLE);
            tvAviso.setText(mensaje);
        }else{
            tvAviso.setVisibility(View.INVISIBLE);
            imgAviso.setVisibility(View.INVISIBLE);
            cardValoracion.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere, por favor...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void setDatosPerfil(JSONObject jsonObject) {
        try {
            tvNombre.setText("Nombre: " + jsonObject.getString("nombre"));
            tvCorreo.setText("Correo: " + jsonObject.getString("correo"));
            etCelular.setText(jsonObject.getString("celular"));
            etTelefono.setText(jsonObject.getString("telefonoLocal"));
            etDireccion.setText(jsonObject.getString("direccion"));
            etDesc.setText(jsonObject.getString("descripcion"));
            editarPerfilPresenter.getFotoPerfil(jsonObject.getString("foto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDatosBanco(JSONObject mensaje) {
        try {
            etNombreTitular.setText(mensaje.getString("nombreTitular"));
            etTarjeta.setText(mensaje.getString("tarjeta"));
            etMes.setText(mensaje.getString("mes"));
            etAño.setText(mensaje.getString("ano"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showErrorBanco(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.WARNING_TYPE, "¡AVISO!", mensaje, false, null, null);
    }

    @Override
    public void successUpdate(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", mensaje, false, null, null);
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        ivFoto.setImageBitmap(bitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
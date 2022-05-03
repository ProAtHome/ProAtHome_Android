package com.proathome.Views.profesional.navigator.editarPerfilProfesional;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilPresenter;
import com.proathome.Interfaces.profesional.EditarPerfil.EditarPerfilView;
import com.proathome.Presenters.profesional.EditarPerfilPresenterImpl;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilProfesionalFragment extends Fragment implements EditarPerfilView {

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
    public static CircleImageView ivFoto;
    public static MaterialCardView cardValoracion;
    private static final int PICK_IMAGE = 100;
    public static final int RESULT_OK = -1;
    public int idProfesional;
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

        editarPerfilPresenter = new EditarPerfilPresenterImpl(this);

        this.idProfesional = SharedPreferencesManager.getInstance(getContext()).getIDProfesional();
        this.correo = SharedPreferencesManager.getInstance(getContext()).getCorreoProfesional();

        btnActualizarInfo.setOnClickListener(view -> {
            JSONObject parametrosPUT = new JSONObject();
            try {
                parametrosPUT.put("idProfesional", this.idProfesional);
                parametrosPUT.put("celular", etCelular.getText().toString());
                parametrosPUT.put("telefonoLocal", etTelefono.getText().toString());
                parametrosPUT.put("direccion", etDireccion.getText().toString());
                parametrosPUT.put("descripcion", etDesc.getText().toString());
                editarPerfilPresenter.actualizarPerfil(parametrosPUT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editarPerfilPresenter.uploadImage(this.idProfesional, getContext());
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

        editarPerfilPresenter.getReportes(this.idProfesional, SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());
        editarPerfilPresenter.getDatosPerfil(this.idProfesional, SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());
        editarPerfilPresenter.getDatosBanco(this.idProfesional, SharedPreferencesManager.getInstance(getContext()).getTokenProfesional());
    }

    public void actualizarDatosBancarios(){
        if(!etTitular.getText().toString().trim().equalsIgnoreCase("") && !etBanco.getText().toString().trim().equalsIgnoreCase("")
            && !etClabe.getText().toString().trim().equalsIgnoreCase("")){
            JSONObject parametrosPUT = new JSONObject();
            try {
                parametrosPUT.put("idProfesional", this.idProfesional);
                parametrosPUT.put("nombreTitular", etTitular.getText().toString());
                parametrosPUT.put("banco",  etBanco.getText().toString());
                parametrosPUT.put("clabe", etClabe.getText().toString().trim());
                editarPerfilPresenter.updateBancoService(parametrosPUT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
            SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", "Llena todos los campos correctamente.", false, null, null);
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
            editarPerfilPresenter.getMediaGalery(getContext().getContentResolver(), data);
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
    public void showSuccess(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.SUCCESS_TYPE, "¡GENIAL!", mensaje, false, null, null);
    }

    @Override
    public void showError(String mensaje) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", mensaje, false, null, null);
    }

    @Override
    public void setVisibilityAvisos(boolean visivilityAvisos, String mensaje) {
        if(visivilityAvisos){
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
    public void setDatosBanco(JSONObject jsonDatos) {
        try{
            etTitular.setText(jsonDatos.getString("nombreTitular"));
            etBanco.setText(jsonDatos.getString("banco"));
            etClabe.setText(jsonDatos.getString("clabe"));
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(bitmap != null && ivFoto != null)
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
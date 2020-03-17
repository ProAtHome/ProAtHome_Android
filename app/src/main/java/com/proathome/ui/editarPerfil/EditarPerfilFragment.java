package com.proathome.ui.editarPerfil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.AdminSQLiteOpenHelper;
import com.proathome.controladores.CargarImagenTask;
import com.proathome.controladores.ServicioTaskBancoEstudiante;
import com.proathome.controladores.ServicioTaskPerfilEstudiante;
import com.proathome.controladores.ServicioTaskUpPerfilEstudiante;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel editarPerfilViewModel;
    private String linkRESTCargarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/perfilCliente";
    private String linkRESTDatosBancarios = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerDatosBancarios";
    private String linkRESTActualizarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/informacionPerfil";
    private String imageHttpAddress = "http://" + Constants.IP + "/ProAtHome/assets/img/fotoPerfil/";
    private Unbinder mUnbinder;
    private ServicioTaskPerfilEstudiante perfilEstudiante;
    private ServicioTaskBancoEstudiante bancoEstudiante;
    private ServicioTaskUpPerfilEstudiante actualizarPerfil;
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
    private Uri imageUri;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        editarPerfilViewModel = ViewModelProviders.of(this).get(EditarPerfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        btnActualizarInfo.setOnClickListener(view -> {


                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "sesion", null, 1);
                SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante, correo FROM sesion WHERE id = " + 1, null);

                if(fila.moveToFirst()){

                    int idEstudiante = fila.getInt(0);
                    String correo = fila.getString(1);
                    actualizarPerfil = new ServicioTaskUpPerfilEstudiante(getContext(), linkRESTActualizarPerfil, idEstudiante, etNombre.getText().toString(), correo, Integer.valueOf(etEdad.getText().toString()), etDesc.getText().toString());
                    actualizarPerfil.execute();

                }else{

                    baseDeDatos.close();

                }

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
        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante, foto FROM sesion WHERE id = " + 1, null);

        if(fila.moveToFirst()){

            int idEstudiante = 0;
            idEstudiante = fila.getInt(0);
            perfilEstudiante = new ServicioTaskPerfilEstudiante(getContext(), linkRESTCargarPerfil, idEstudiante);
            perfilEstudiante.execute();
            bancoEstudiante = new ServicioTaskBancoEstudiante(getContext(), linkRESTDatosBancarios, idEstudiante);
            bancoEstudiante.execute();
            CargarImagenTask cargarImagenTask = new CargarImagenTask(imageHttpAddress, fila.getString(1), Constants.FOTO_EDITAR_PERFIL);
            cargarImagenTask.execute();

        }else{

            baseDeDatos.close();

        }

        baseDeDatos.close();


    }

    public void abrirGaleria(){

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    @OnClick(R.id.btnFoto)
    public void onClickFoto(View view){

        abrirGaleria();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            ivFoto.setImageURI(imageUri);
        }
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }
}
package com.proathome.ui.editarPerfil;

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
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.proathome.R;
import com.proathome.controladores.ServicioTaskBancoEstudiante;
import com.proathome.controladores.ServicioTaskPerfilEstudiante;
import com.proathome.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel editarPerfilViewModel;
    private String linkRESTCargarPerfil = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/perfilCliente";
    private String linkRESTDatosBancarios = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerDatosBancarios";
    Unbinder mUnbinder;
    private ServicioTaskPerfilEstudiante perfilEstudiante;
    private ServicioTaskBancoEstudiante bancoEstudiante;
    public static TextInputEditText etNombre;
    public static TextInputEditText etEdad;
    public static TextInputEditText etDesc;
    public static TextInputEditText etDireccion;
    public static TextInputEditText etCuenta;
    public static TextInputEditText etBanco;
    public static TextInputEditText etTipoDePago;
    public static ImageView ivFoto;
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

        perfilEstudiante = new ServicioTaskPerfilEstudiante(getContext(), linkRESTCargarPerfil, 1);
        perfilEstudiante.execute();
        bancoEstudiante = new ServicioTaskBancoEstudiante(getContext(), linkRESTDatosBancarios, 1);
        bancoEstudiante.execute();

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mUnbinder.unbind();

    }
}
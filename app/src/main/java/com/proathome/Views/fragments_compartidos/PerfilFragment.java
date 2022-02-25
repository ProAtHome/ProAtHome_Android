package com.proathome.Views.fragments_compartidos;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilPresenter;
import com.proathome.Interfaces.fragments_compartidos.Perfil.PerfilView;
import com.proathome.Presenters.fragments_compartidos.PerfilPresenterImpl;
import com.proathome.R;
import com.proathome.Adapters.ComponentAdapterValoraciones;
import com.proathome.Views.cliente.fragments.DetallesFragment;
import com.proathome.Views.profesional.fragments.DetallesSesionProfesionalFragment;
import com.proathome.Utils.pojos.ComponentValoraciones;
import com.proathome.Utils.SweetAlert;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PerfilFragment extends DialogFragment implements PerfilView {

    private Unbinder mUnbinder;
    public static ComponentValoraciones componentValoraciones;
    public static ComponentAdapterValoraciones componentAdapterValoraciones;
    public static ShapeableImageView fotoPerfil;
    public static String nombre, correo;
    public static float valoracion;
    private Bundle bundle;
    public static int PERFIL_PROFESIONAL = 1, PERFIL_CLIENTE = 2;
    public static TextView tvNombre;
    public static TextView tvCoreo;
    public static TextView tvCalificacion;
    public static TextView tvDescripcion;
    private ProgressDialog progressDialog;
    private PerfilPresenter perfilPresenter;

    @BindView(R.id.toolbarPerfil)
    MaterialToolbar toolbar;
    @BindView(R.id.recyclerViewPerfil)
    RecyclerView recyclerView;

    public PerfilFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        perfilPresenter = new PerfilPresenterImpl(this);

        fotoPerfil = view.findViewById(R.id.foto);
        tvNombre = view.findViewById(R.id.nombre);
        tvCoreo = view.findViewById(R.id.correo);
        tvCalificacion = view.findViewById(R.id.calificacion);
        tvDescripcion = view.findViewById(R.id.descripcion);

        configAdapter();
        configRecyclerView();

        bundle = getArguments();

        if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE)
            perfilPresenter.getValoracion(DetallesSesionProfesionalFragment.idCliente, bundle.getInt("tipoPerfil"));
        else if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_PROFESIONAL)
            perfilPresenter.getValoracion(DetallesFragment.idProfesional, bundle.getInt("tipoPerfil"));

        perfilPresenter.getFotoPerfil(bundle.getInt("tipoPerfil"));

        if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE)
            toolbar.setTitle("Perfil - Cliente");
        else if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_PROFESIONAL)
            toolbar.setTitle("Perfil - Profesional");

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v ->{
            dismiss();
        });


        return view;
    }

    public void configAdapter(){
        componentAdapterValoraciones = new ComponentAdapterValoraciones(new ArrayList<>());
    }

    private void configRecyclerView(){
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setAdapter(componentAdapterValoraciones);
    }

    public static ComponentValoraciones getmInstance(String comentario, float valoracion){
        componentValoraciones = new ComponentValoraciones();
        componentValoraciones.setComentario(comentario);
        componentValoraciones.setValoracion(valoracion);

        return componentValoraciones;
    }

    @Override
    public void showError(String error) {
        SweetAlert.showMsg(getContext(), SweetAlert.ERROR_TYPE, "¡ERROR!", error, true, "OK", ()->{
            dismiss();
        });
    }

    @Override
    public void setAdapter(String comentario, float valoracion) {
        componentAdapterValoraciones.add(PerfilFragment.getmInstance(comentario, valoracion));
    }

    @Override
    public void setInfoPerfil(String nombre, String correo, String descripcion) {
        tvNombre.setText(nombre);
        tvCoreo.setText(correo);
        tvDescripcion.setText(descripcion);
    }

    @Override
    public void setFoto(Bitmap bitmap) {
        fotoPerfil.setImageBitmap(bitmap);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getContext(), "Cargando", "Espere...");
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        mUnbinder.unbind();
    }

}
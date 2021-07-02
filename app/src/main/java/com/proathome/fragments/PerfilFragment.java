package com.proathome.fragments;

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
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterValoraciones;
import com.proathome.servicios.profesor.ServicioTaskFotoDetalles;
import com.proathome.servicios.valoracion.ServicioTaskValoracion;
import com.proathome.utils.ComponentValoraciones;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PerfilFragment extends DialogFragment {

    private Unbinder mUnbinder;
    public static ComponentValoraciones componentValoraciones;
    public static ComponentAdapterValoraciones componentAdapterValoraciones;
    public static ShapeableImageView foto;
    public static String nombre, correo;
    public static float valoracion;
    public static int PERFIL_PROFESOR_EN_ESTUDIANTE = 3, PERFIL_ESTUDIANTE_EN_PROFESOR = 4, PERFIL_PROFESOR = 1, PERFIL_ESTUDIANTE = 2;
    public static TextView tvNombre;
    public static TextView tvCoreo;
    public static TextView tvCalificacion;
    public static TextView descripcion;
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

        foto = view.findViewById(R.id.foto);
        tvNombre = view.findViewById(R.id.nombre);
        tvCoreo = view.findViewById(R.id.correo);
        tvCalificacion = view.findViewById(R.id.calificacion);
        descripcion = view.findViewById(R.id.descripcion);

        configAdapter();
        configRecyclerView();

        Bundle bundle = getArguments();

        if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_ESTUDIANTE) {
            ServicioTaskValoracion valoracion = new ServicioTaskValoracion(DetallesSesionProfesorFragment.idEstudiante, ServicioTaskValoracion.PERFIL_ESTUDIANTE);
            valoracion.execute();
            ServicioTaskFotoDetalles detalles = new ServicioTaskFotoDetalles(getContext(), DetallesSesionProfesorFragment.fotoNombre, PerfilFragment.PERFIL_ESTUDIANTE_EN_PROFESOR);
            detalles.execute();
        }else if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_PROFESOR){
            ServicioTaskValoracion valoracion = new ServicioTaskValoracion(DetallesFragment.idProfesor, ServicioTaskValoracion.PERFIL_PROFESOR);
            valoracion.execute();
            ServicioTaskFotoDetalles detalles = new ServicioTaskFotoDetalles(getContext(), DetallesFragment.fotoNombre, PerfilFragment.PERFIL_PROFESOR_EN_ESTUDIANTE);
            detalles.execute();
        }

        if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_ESTUDIANTE) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
            toolbar.setTitle("Perfil - ESTUDIANTE");
        }else if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_PROFESOR){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
            toolbar.setTitle("Perfil - PROFESOR");
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
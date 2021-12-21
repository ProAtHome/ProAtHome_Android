package com.proathome.ui.fragments;

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
import com.proathome.servicios.api.APIEndPoints;
import com.proathome.servicios.api.WebServicesAPI;
import com.proathome.servicios.api.assets.WebServiceAPIAssets;
import com.proathome.utils.ComponentValoraciones;
import com.proathome.utils.SweetAlert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PerfilFragment extends DialogFragment {

    private Unbinder mUnbinder;
    public static ComponentValoraciones componentValoraciones;
    public static ComponentAdapterValoraciones componentAdapterValoraciones;
    public static ShapeableImageView fotoPerfil;
    public static String nombre, correo;
    public static float valoracion;
    private Bundle bundle;
    public static int PERFIL_PROFESIONAL_EN_CLIENTE = 3, PERFIL_CLIENTE_EN_PROFESIONAL = 4, PERFIL_PROFESIONAL = 1, PERFIL_CLIENTE = 2;
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

        fotoPerfil = view.findViewById(R.id.foto);
        tvNombre = view.findViewById(R.id.nombre);
        tvCoreo = view.findViewById(R.id.correo);
        tvCalificacion = view.findViewById(R.id.calificacion);
        descripcion = view.findViewById(R.id.descripcion);

        configAdapter();
        configRecyclerView();

        bundle = getArguments();

        if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE)
            getValoracion(DetallesSesionProfesionalFragment.idCliente);
        else if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_PROFESIONAL)
            getValoracion(DetallesFragment.idProfesional);

        setImageBitmap();

        if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
            toolbar.setTitle("Perfil - CLIENTE");
        }else if(bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_PROFESIONAL){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAzul));
            toolbar.setTitle("Perfil - PROFESIONAL");
        }
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v ->{
            dismiss();
        });


        return view;
    }


    private void setImageBitmap(){
        WebServiceAPIAssets webServiceAPIAssets = new WebServiceAPIAssets(response ->{
            fotoPerfil.setImageBitmap(response);
        }, APIEndPoints.FOTO_PERFIL, bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE ? DetallesSesionProfesionalFragment.fotoNombre : DetallesFragment.fotoNombre);
        webServiceAPIAssets.execute();
    }

    private void getValoracion(int idPerfil){
        WebServicesAPI webServicesAPI = new WebServicesAPI(response -> {
            int numValoraciones = 0;
            double sumaValoraciones = 0;
            try{
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray == null){
                    new SweetAlert(getContext(), SweetAlert.ERROR_TYPE, bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE ? SweetAlert.CLIENTE : SweetAlert.PROFESIONAL)
                            .setTitleText("¡ERROR!")
                            .setContentText("Ocurrió un problema, intenta más tarde.")
                            .setConfirmButton("OK", listener->{
                                listener.dismissWithAnimation();
                                dismiss();
                            })
                            .show();
                }else{
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getBoolean("valoraciones")){//Valoraciones del profesional
                            if(!jsonObject.getBoolean("error")){
                                //Hay error.
                                //Obtener promedio
                                numValoraciones++;
                                sumaValoraciones += Double.parseDouble(jsonObject.get("valoracion").toString());
                                componentAdapterValoraciones.add(PerfilFragment.getmInstance(jsonObject.getString("comentario"), Float.parseFloat(jsonObject.get("valoracion").toString())));
                            }else
                                componentAdapterValoraciones.add(PerfilFragment.getmInstance("El usuario no tiene valoraciones aún.", 0.0f));
                        }else{//Perfil de profesional
                            tvNombre.setText(jsonObject.getString("nombre"));
                            tvCoreo.setText(jsonObject.getString("correo"));
                            descripcion.setText(jsonObject.getString("descripcion"));
                        }
                    }
                    //Promedio
                    double promedio = sumaValoraciones / numValoraciones;
                    if(numValoraciones == 0)
                        PerfilFragment.tvCalificacion.setText("0.00");
                    else
                        PerfilFragment.tvCalificacion.setText(String.format("%.2f", promedio));
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }, bundle.getInt("tipoPerfil") == PerfilFragment.PERFIL_CLIENTE ? APIEndPoints.GET_VALORACION_CLIENTE + idPerfil : APIEndPoints.GET_VALORACION_PROFESIONAL + idPerfil, WebServicesAPI.GET, null);
        webServicesAPI.execute();
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
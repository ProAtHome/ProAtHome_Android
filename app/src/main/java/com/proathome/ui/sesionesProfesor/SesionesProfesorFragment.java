package com.proathome.ui.sesionesProfesor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterGestionarProfesor;
import com.proathome.fragments.BuscarSesionFragment;
import com.proathome.servicios.profesor.AdminSQLiteOpenHelperProfesor;
import com.proathome.servicios.profesor.ServicioTaskSesionesProfesor;
import com.proathome.utils.Constants;
import com.proathome.utils.PermisosUbicacion;
import com.proathome.utils.SweetAlert;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesProfesorFragment extends Fragment {

    private Unbinder mUnbinder;
    public static ComponentAdapterGestionarProfesor myAdapter;
    public static LottieAnimationView lottieAnimationView;
    private String clasesHttpAddress = Constants.IP +
            "/ProAtHome/apiProAtHome/profesor/obtenerSesionesProfesorMatch/";
    private ServicioTaskSesionesProfesor sesionesTask;
    private int idProfesor = 0;
    @BindView(R.id.recyclerGestionarProfesor)
    RecyclerView recyclerView;
    @BindView(R.id.fabNuevaSesion)
    FloatingActionButton fabNuevaSesion;
    @BindView(R.id.fabActualizar)
    FloatingActionButton fabActualizar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sesiones_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(getContext(),
                "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);
        this.idProfesor = 0;
        if (fila.moveToFirst()) {
            this.idProfesor = fila.getInt(0);
            baseDeDatos.close();
        } else
            baseDeDatos.close();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        sesionesTask = new ServicioTaskSesionesProfesor(getContext(), clasesHttpAddress, this.idProfesor,
                Constants.SESIONES_GESTIONAR);
        sesionesTask.execute();
        configAdapter();
        configRecyclerView();
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterGestionarProfesor(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    private void showAlert() {
        PermisosUbicacion.showAlert(getContext(), SweetAlert.PROFESOR);
    }

    @OnClick({R.id.fabNuevaSesion, R.id.fabActualizar})
    public void onClicked(View view){
        switch (view.getId()){
            case R.id.fabNuevaSesion:
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    showAlert();
                } else {
                    BuscarSesionFragment nueva = new BuscarSesionFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    nueva.show(transaction, BuscarSesionFragment.TAG);
                }
                break;
            case R.id.fabActualizar:
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
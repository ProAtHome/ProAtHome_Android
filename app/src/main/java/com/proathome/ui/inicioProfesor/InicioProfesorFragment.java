package com.proathome.ui.inicioProfesor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterSesionesProfesor;
import com.proathome.servicios.profesor.AdminSQLiteOpenHelperProfesor;
import com.proathome.servicios.profesor.ServicioTaskSesionesProfesor;
import com.proathome.utils.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InicioProfesorFragment extends Fragment {

    public static ComponentAdapterSesionesProfesor myAdapter;
    private String clasesHttpAddress = Constants.IP +
            "/ProAtHome/apiProAtHome/profesor/obtenerSesionesProfesorMatch/";
    private ServicioTaskSesionesProfesor sesionesTask;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);
        AdminSQLiteOpenHelperProfesor admin = new AdminSQLiteOpenHelperProfesor(getContext(),
                "sesionProfesor", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesor FROM sesionProfesor WHERE id = " + 1, null);
        int idProfesor = 0;
        if (fila.moveToFirst()) {
            idProfesor = fila.getInt(0);
            sesionesTask = new ServicioTaskSesionesProfesor(getContext(), clasesHttpAddress, idProfesor,
                    Constants.SESIONES_INICIO);
            sesionesTask.execute();
            configAdapter();
            configRecyclerView();
            baseDeDatos.close();
        } else {
            baseDeDatos.close();
        }

        return root;
    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterSesionesProfesor(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
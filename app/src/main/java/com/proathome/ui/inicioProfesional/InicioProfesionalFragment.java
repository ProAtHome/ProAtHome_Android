package com.proathome.ui.inicioProfesional;

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
import com.proathome.adapters.ComponentAdapterSesionesProfesional;
import com.proathome.servicios.profesional.AdminSQLiteOpenHelperProfesional;
import com.proathome.servicios.profesional.ServicioTaskSesionesProfesional;
import com.proathome.utils.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InicioProfesionalFragment extends Fragment {

    public static ComponentAdapterSesionesProfesional myAdapter;
    private String serviciosHttpAddress = Constants.IP +
            "/ProAtHome/apiProAtHome/profesional/obtenerSesionesProfesionalMatch/";
    private ServicioTaskSesionesProfesional sesionesTask;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);
        AdminSQLiteOpenHelperProfesional admin = new AdminSQLiteOpenHelperProfesional(getContext(),
                "sesionProfesional", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idProfesional FROM sesionProfesional WHERE id = " + 1, null);
        int idProfesional = 0;
        if (fila.moveToFirst()) {
            idProfesional = fila.getInt(0);
            sesionesTask = new ServicioTaskSesionesProfesional(getContext(), serviciosHttpAddress, idProfesional,
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
        myAdapter = new ComponentAdapterSesionesProfesional(new ArrayList<>());
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
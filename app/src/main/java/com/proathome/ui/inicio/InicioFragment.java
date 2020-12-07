package com.proathome.ui.inicio;

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
import com.proathome.servicios.estudiante.AdminSQLiteOpenHelper;
import com.proathome.servicios.estudiante.ServicioTaskSesionesEstudiante;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapter;
import com.proathome.utils.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InicioFragment extends Fragment {

    public static ComponentAdapter myAdapter;
    private String clasesHttpAddress = "http://" + Constants.IP +
            ":8080/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    private ServicioTaskSesionesEstudiante sesionesTask;
    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);
        int idCliente = 0;
        if (fila.moveToFirst()) {
            idCliente = fila.getInt(0);
            sesionesTask = new ServicioTaskSesionesEstudiante(getContext(), clasesHttpAddress, idCliente,
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
        myAdapter = new ComponentAdapter(new ArrayList<>());
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
package com.proathome.ui.sesiones;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;
import com.proathome.adapters.ComponentAdapterGestionar;
import com.proathome.controladores.clase.ServicioSesionesPagadas;
import com.proathome.controladores.estudiante.AdminSQLiteOpenHelper;
import com.proathome.controladores.estudiante.ServicioTaskSesionesEstudiante;
import com.proathome.fragments.NuevaSesionFragment;
import com.proathome.fragments.PlanesFragment;
import com.proathome.utils.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesFragment extends Fragment {

    public static ComponentAdapterGestionar myAdapter;
    private String clasesHttpAddress = "http://" + Constants.IP + ":8080/ProAtHome/apiProAtHome/cliente/obtenerSesiones/";
    private ServicioTaskSesionesEstudiante sesionesTask;
    private Unbinder mUnbinder;
    private int idCliente = 0;
    public static boolean SESIONES_PAGADAS_FINALIZADAS = false, PLAN_ACTIVO = false;
    public static String PLAN = "";
    public static String FECHA_INICIO = "", FECHA_FIN = "";
    public static int MONEDERO = 0;
    public static LottieAnimationView lottieAnimationView;
    @BindView(R.id.recyclerGestionar)
    RecyclerView recyclerView;
    @BindView(R.id.fabNuevaSesion)
    FloatingActionButton fabNuevaSesion;
    @BindView(R.id.fabActualizar)
    FloatingActionButton fabActualizar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sesionesTask = new ServicioTaskSesionesEstudiante(getContext(), clasesHttpAddress, this.idCliente, Constants.SESIONES_GESTIONAR);
        sesionesTask.execute();
        configAdapter();
        configRecyclerView();
        ServicioSesionesPagadas servicioSesionesPagadas = new ServicioSesionesPagadas(this.idCliente);
        servicioSesionesPagadas.execute();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sesiones, container, false);
        lottieAnimationView = root.findViewById(R.id.animation_viewSesiones);
        mUnbinder = ButterKnife.bind(this, root);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(),"sesion", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT idEstudiante FROM sesion WHERE id = " + 1, null);
        if (fila.moveToFirst()) {
            this.idCliente = fila.getInt(0);
            baseDeDatos.close();
        } else {
            baseDeDatos.close();
        }

        ServicioSesionesPagadas servicioSesionesPagadas = new ServicioSesionesPagadas(this.idCliente);
        servicioSesionesPagadas.execute();

        return root;

    }

    public void configAdapter(){
        myAdapter = new ComponentAdapterGestionar(new ArrayList<>());
    }

    private void configRecyclerView(){
        recyclerView.setAdapter(myAdapter);
    }

    private void showAlert() {
        new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos tu permiso :)")
                .setPositiveButton("Dar permiso", (dialog, which) -> {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    Toast.makeText(getContext(), "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                })
                .setOnCancelListener(dialog -> {
                    Toast.makeText(getContext(), "Necesitamos el permiso ;/", Toast.LENGTH_LONG).show();
                })
                .show();
    }

    @OnClick({R.id.fabNuevaSesion, R.id.fabActualizar})
    public void onClicked(View view){

        switch (view.getId()){
            case R.id.fabNuevaSesion:
                /*TODO FLUJO_PLANES: Verificar que tengamos más de X sesiones PAGADAS Y FINALIZADAS (Antes de dar click) y guardar en Constante.
                   -> SI, ENTONES, ¿Hay un plan distinto a PARTICULAR activo? -> SI, ENTONCES, No mostramos los planes.
                                                                              -> NO, ENTONCES, Mostramos MODAL con PLANES.
                   -> NO, ENTONCES  Creamos Clase con PLAN -> PARTICULAR (Normal). */
                if(SesionesFragment.PLAN_ACTIVO && SesionesFragment.MONEDERO > 0){
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        showAlert();
                    } else {
                        if(SesionesFragment.SESIONES_PAGADAS_FINALIZADAS){
                            if(SesionesFragment.PLAN_ACTIVO){
                                //TODO FLUJO_EJECUTAR_PLAN: Pasar por Bundle el tipo de PLAN en nuevaSesionFragment.
                                NuevaSesionFragment nueva = new NuevaSesionFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                nueva.show(transaction, NuevaSesionFragment.TAG);
                                Toast.makeText(getContext(), "Con plan disitnto a PARTICULAR; = " + SesionesFragment.PLAN, Toast.LENGTH_LONG).show();
                            }else{
                                PlanesFragment planesFragment = new PlanesFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                planesFragment.show(transaction, "Planes Disponibles");
                                Toast.makeText(getContext(), "Mostramos planes.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            if(SesionesFragment.PLAN_ACTIVO){
                                NuevaSesionFragment nueva = new NuevaSesionFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                nueva.show(transaction, NuevaSesionFragment.TAG);
                                Toast.makeText(getContext(), "Con plan disitnto a PARTICULAR; = " + SesionesFragment.PLAN, Toast.LENGTH_LONG).show();
                            }else{
                                NuevaSesionFragment nueva = new NuevaSesionFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                nueva.show(transaction, NuevaSesionFragment.TAG);
                                Toast.makeText(getContext(), "Con plan PARTICULAR; = " + SesionesFragment.PLAN, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }else if(SesionesFragment.PLAN_ACTIVO && SesionesFragment.MONEDERO == 0){
                    Toast.makeText(getContext(), "Estas en plan activo pero ya no tienes monedas, acábatelas :).", Toast.LENGTH_LONG).show();
                }else{
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        showAlert();
                    } else {
                        if(SesionesFragment.SESIONES_PAGADAS_FINALIZADAS){
                            if(SesionesFragment.PLAN_ACTIVO){
                                //TODO FLUJO_EJECUTAR_PLAN: Pasar por Bundle el tipo de PLAN en nuevaSesionFragment.
                                NuevaSesionFragment nueva = new NuevaSesionFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                nueva.show(transaction, NuevaSesionFragment.TAG);
                                Toast.makeText(getContext(), "Con plan disitnto a PARTICULAR; = " + SesionesFragment.PLAN, Toast.LENGTH_LONG).show();
                            }else{
                                PlanesFragment planesFragment = new PlanesFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                planesFragment.show(transaction, "Planes Disponibles");
                                Toast.makeText(getContext(), "Mostramos planes.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            if(SesionesFragment.PLAN_ACTIVO){
                                NuevaSesionFragment nueva = new NuevaSesionFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                nueva.show(transaction, NuevaSesionFragment.TAG);
                                Toast.makeText(getContext(), "Con plan disitnto a PARTICULAR; = " + SesionesFragment.PLAN, Toast.LENGTH_LONG).show();
                            }else{
                                NuevaSesionFragment nueva = new NuevaSesionFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                nueva.show(transaction, NuevaSesionFragment.TAG);
                                Toast.makeText(getContext(), "Con plan PARTICULAR; = " + SesionesFragment.PLAN, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                break;
            case R.id.fabActualizar:
                getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
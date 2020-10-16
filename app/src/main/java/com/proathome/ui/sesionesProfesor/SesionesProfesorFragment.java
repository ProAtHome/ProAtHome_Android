package com.proathome.ui.sesionesProfesor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.proathome.R;
import com.proathome.fragments.BuscarSesionFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SesionesProfesorFragment extends Fragment {

    private Unbinder mUnbinder;
    public static LottieAnimationView lottieAnimationView;
    @BindView(R.id.fabNuevaSesion)
    FloatingActionButton fabNuevaSesion;
    @BindView(R.id.fabActualizar)
    FloatingActionButton fabActualizar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sesiones_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, root);
        lottieAnimationView = root.findViewById(R.id.animation_view);

        return root;
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
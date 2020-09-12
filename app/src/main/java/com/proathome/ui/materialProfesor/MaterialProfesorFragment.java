package com.proathome.ui.materialProfesor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.R;
import com.proathome.fragments.MaterialFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MaterialProfesorFragment extends Fragment {

    private Unbinder mUnbinder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_material_profesor, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.abrir)
    public void onClick(View view){
        MaterialFragment materialFragment = new MaterialFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.show(fragmentTransaction, "Material");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
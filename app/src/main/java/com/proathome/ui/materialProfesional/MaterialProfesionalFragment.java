package com.proathome.ui.materialProfesional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.proathome.R;
import com.proathome.ui.fragments.MaterialFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MaterialProfesionalFragment extends Fragment {

    private Unbinder mUnbinder;
    private Bundle bundle = new Bundle();
    private MaterialFragment materialFragment = new MaterialFragment();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_material_profesional, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.b1_b1)
    public void onClick1(View view){
        bundle.putString("idPDF", "1_1_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b1_b2)
    public void onClick2(View view){
        bundle.putString("idPDF", "1_1_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b1_b3)
    public void onClick3(View view){
        bundle.putString("idPDF", "1_1_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b1_b4)
    public void onClick4(View view){
        bundle.putString("idPDF", "1_1_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b1_b5)
    public void onClick5(View view){
        bundle.putString("idPDF", "1_1_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b1_b6)
    public void onClick6(View view){
        bundle.putString("idPDF", "1_1_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b2_b1)
    public void onClick7(View view){
        bundle.putString("idPDF", "1_2_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b2_b2)
    public void onClick8(View view){
        bundle.putString("idPDF", "1_2_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b2_b3)
    public void onClick9(View view){
        bundle.putString("idPDF", "1_2_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b2_b4)
    public void onClick10(View view){
        bundle.putString("idPDF", "1_2_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b2_b5)
    public void onClick11(View view){
        bundle.putString("idPDF", "1_2_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b2_b6)
    public void onClick12(View view){
        bundle.putString("idPDF", "1_2_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b1)
    public void onClick13(View view){
        bundle.putString("idPDF", "1_3_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b2)
    public void onClick14(View view){
        bundle.putString("idPDF", "1_3_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b3)
    public void onClick15(View view){
        bundle.putString("idPDF", "1_3_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b4)
    public void onClick16(View view){
        bundle.putString("idPDF", "1_3_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b5)
    public void onClick17(View view){
        bundle.putString("idPDF", "1_3_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b6)
    public void onClick18(View view){
        bundle.putString("idPDF", "1_3_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b3_b7)
    public void onClick19(View view){
        bundle.putString("idPDF", "1_3_7.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b1)
    public void onClick20(View view){
        bundle.putString("idPDF", "1_4_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b2)
    public void onClick21(View view){
        bundle.putString("idPDF", "1_4_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b3)
    public void onClick22(View view){
        bundle.putString("idPDF", "1_4_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b4)
    public void onClick23(View view){
        bundle.putString("idPDF", "1_4_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b5)
    public void onClick24(View view){
        bundle.putString("idPDF", "1_4_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b6)
    public void onClick25(View view){
        bundle.putString("idPDF", "1_4_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b4_b7)
    public void onClick26(View view){
        bundle.putString("idPDF", "1_4_7.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b1)
    public void onClick27(View view){
        bundle.putString("idPDF", "1_5_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b2)
    public void onClick28(View view){
        bundle.putString("idPDF", "1_5_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b3)
    public void onClick29(View view){
        bundle.putString("idPDF", "1_5_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b4)
    public void onClick30(View view){
        bundle.putString("idPDF", "1_5_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b5)
    public void onClick31(View view){
        bundle.putString("idPDF", "1_5_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b6)
    public void onClick32(View view){
        bundle.putString("idPDF", "1_5_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.b5_b7)
    public void onClick33(View view){
        bundle.putString("idPDF", "1_5_7.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b1)
    public void onClick34(View view){
        bundle.putString("idPDF", "2_1_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b2)
    public void onClick35(View view){
        bundle.putString("idPDF", "2_1_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b3)
    public void onClick36(View view){
        bundle.putString("idPDF", "2_1_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b4)
    public void onClick37(View view){
        bundle.putString("idPDF", "2_1_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b5)
    public void onClick38(View view){
        bundle.putString("idPDF", "2_1_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b6)
    public void onClick39(View view){
        bundle.putString("idPDF", "2_1_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i1_b7)
    public void onClick40(View view){
        bundle.putString("idPDF", "2_1_7.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i2_b1)
    public void onClick41(View view){
        bundle.putString("idPDF", "2_2_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i2_b2)
    public void onClick42(View view){
        bundle.putString("idPDF", "2_2_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i2_b3)
    public void onClick43(View view){
        bundle.putString("idPDF", "2_2_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i2_b4)
    public void onClick44(View view){
        bundle.putString("idPDF", "2_2_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i2_b5)
    public void onClick45(View view){
        bundle.putString("idPDF", "2_2_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i2_b6)
    public void onClick46(View view){
        bundle.putString("idPDF", "2_2_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i3_b1)
    public void onClick47(View view){
        bundle.putString("idPDF", "2_3_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i3_b2)
    public void onClick48(View view){
        bundle.putString("idPDF", "2_3_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i3_b3)
    public void onClick49(View view){
        bundle.putString("idPDF", "2_3_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i3_b4)
    public void onClick50(View view){
        bundle.putString("idPDF", "2_3_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i3_b5)
    public void onClick51(View view){
        bundle.putString("idPDF", "2_3_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i3_b6)
    public void onClick52(View view){
        bundle.putString("idPDF", "2_3_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i4_b1)
    public void onClick53(View view){
        bundle.putString("idPDF", "2_4_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i4_b2)
    public void onClick54(View view){
        bundle.putString("idPDF", "2_4_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i4_b3)
    public void onClick55(View view){
        bundle.putString("idPDF", "2_4_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i4_b4)
    public void onClick56(View view){
        bundle.putString("idPDF", "2_4_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i4_b5)
    public void onClick57(View view){
        bundle.putString("idPDF", "2_4_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i4_b6)
    public void onClick58(View view){
        bundle.putString("idPDF", "2_4_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i5_b1)
    public void onClick59(View view){
        bundle.putString("idPDF", "2_5_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i5_b2)
    public void onClick60(View view){
        bundle.putString("idPDF", "2_5_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i5_b3)
    public void onClick61(View view){
        bundle.putString("idPDF", "2_5_3.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i5_b4)
    public void onClick62(View view){
        bundle.putString("idPDF", "2_5_4.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i5_b5)
    public void onClick63(View view){
        bundle.putString("idPDF", "2_5_5.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.i5_b6)
    public void onClick64(View view){
        bundle.putString("idPDF", "2_5_6.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a1_b1)
    public void onClick65(View view){
        bundle.putString("idPDF", "3_1_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a1_b2)
    public void onClick66(View view){
        bundle.putString("idPDF", "3_1_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a2_b1)
    public void onClick67(View view){
        bundle.putString("idPDF", "3_2_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a2_b2)
    public void onClick68(View view){
        bundle.putString("idPDF", "3_2_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a3_b1)
    public void onClick69(View view){
        bundle.putString("idPDF", "3_3_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a3_b2)
    public void onClick70(View view){
        bundle.putString("idPDF", "3_3_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a4_b1)
    public void onClick71(View view){
        bundle.putString("idPDF", "3_4_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a4_b2)
    public void onClick72(View view){
        bundle.putString("idPDF", "3_4_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a5_b1)
    public void onClick73(View view){
        bundle.putString("idPDF", "3_5_1.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @OnClick(R.id.a5_b2)
    public void onClick74(View view){
        bundle.putString("idPDF", "3_5_2.pdf");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        materialFragment.setArguments(bundle);
        materialFragment.show(fragmentTransaction, "Material");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
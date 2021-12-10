package com.proathome.ui.fragments;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.barteksc.pdfviewer.PDFView;
import com.proathome.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MaterialFragment extends DialogFragment {

    private Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    PDFView pdfView;

    public MaterialFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        pdfView = view.findViewById(R.id.pdfv);
        pdfView.fromAsset(bundle.getString("idPDF")).load();

        toolbar.setTitle("Material Didáctico");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
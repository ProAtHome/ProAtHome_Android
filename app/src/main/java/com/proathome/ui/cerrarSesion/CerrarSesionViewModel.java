package com.proathome.ui.cerrarSesion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CerrarSesionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CerrarSesionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Cerramos sesión en ésta acción.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.proathome.ui.cerrarSesionProfesor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CerrarSesionProfesorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CerrarSesionProfesorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Cerramos sesión en ésta acción.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
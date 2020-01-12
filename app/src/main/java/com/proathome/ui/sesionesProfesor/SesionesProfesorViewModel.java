package com.proathome.ui.sesionesProfesor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SesionesProfesorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SesionesProfesorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento añadiremos y editaremos las Sesiones.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
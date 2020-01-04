package com.proathome.ui.sesiones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SesionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SesionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento añadiremos y editaremos las Sesiones.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
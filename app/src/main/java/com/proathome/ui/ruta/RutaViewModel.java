package com.proathome.ui.ruta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RutaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RutaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ruta de Aprendizaje.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
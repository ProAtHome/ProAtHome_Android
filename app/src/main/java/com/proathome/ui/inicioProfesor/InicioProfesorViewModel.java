package com.proathome.ui.inicioProfesor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioProfesorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InicioProfesorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento van las notificaciónes de Sesiónes.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
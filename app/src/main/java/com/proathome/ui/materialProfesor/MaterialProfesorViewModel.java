package com.proathome.ui.materialProfesor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MaterialProfesorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MaterialProfesorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento van el material de Estudio.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
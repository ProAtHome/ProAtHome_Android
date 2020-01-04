package com.proathome.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento van las notificaciónes de Sesiónes.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.proathome.ui.editarPerfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditarPerfilViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EditarPerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento editamos toda la información del Perfil.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
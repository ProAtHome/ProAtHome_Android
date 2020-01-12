package com.proathome.ui.editarPerfilProfesor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditarPerfilProfesorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EditarPerfilProfesorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("En éste fragmento editamos toda la información del Perfil.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
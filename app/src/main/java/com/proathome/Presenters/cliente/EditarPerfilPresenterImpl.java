package com.proathome.Presenters.cliente;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.proathome.Interactors.cliente.EditarPerfilInteractorImpl;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilInteractor;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilPresenter;
import com.proathome.Interfaces.cliente.EditarPerfil.EditarPerfilView;
import org.json.JSONObject;

public class EditarPerfilPresenterImpl implements EditarPerfilPresenter {

    private EditarPerfilView editarPerfilView;
    private EditarPerfilInteractor editarPerfilInteractor;

    public EditarPerfilPresenterImpl(EditarPerfilView editarPerfilView){
        this.editarPerfilView = editarPerfilView;
        editarPerfilInteractor = new EditarPerfilInteractorImpl(this);
    }

    @Override
    public void getReportes(int idCliente, String token) {
        editarPerfilInteractor.getReportes(idCliente, token);
    }

    @Override
    public void getDatosPerfil(int idCliente, String token) {
        editarPerfilInteractor.getDatosPerfil(idCliente, token);
    }

    @Override
    public void getDatosBanco(int idCliente, String token) {
        editarPerfilInteractor.getDatosBanco(idCliente, token);
    }

    @Override
    public void showError(String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.showError(mensaje);
    }

    @Override
    public void setVisibilityReportes(boolean visibilityReportes, String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.setVisibilityReportes(visibilityReportes, mensaje);
    }

    @Override
    public void showProgress() {
        if(editarPerfilView != null)
            editarPerfilView.showProgress();
    }

    @Override
    public void hideProgress() {
        if(editarPerfilView != null)
            editarPerfilView.hideProgress();
    }

    @Override
    public void setDatosPerfil(JSONObject jsonObject) {
        if(editarPerfilView != null)
            editarPerfilView.setDatosPerfil(jsonObject);
    }

    @Override
    public void setDatosBanco(JSONObject jsonObject) {
        if(editarPerfilView != null)
            editarPerfilView.setDatosBanco(jsonObject);
    }

    @Override
    public void showErrorBanco(String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.showErrorBanco(mensaje);
    }

    @Override
    public void updateCuentaCliente(JSONObject jsonObject) {
        editarPerfilInteractor.updateCuentaCliente(jsonObject);
    }

    @Override
    public void successUpdate(String mensaje) {
        if(editarPerfilView != null)
            editarPerfilView.successUpdate(mensaje);
    }

    @Override
    public void updatePerfil(JSONObject jsonObject, Context context, int idCliente) {
        editarPerfilInteractor.updatePerfil(jsonObject, context, idCliente);
    }

    @Override
    public void getBitmapMedia(Intent data, ContentResolver contentResolver) {
        editarPerfilInteractor.getBitmapMedia(data, contentResolver);
    }

    @Override
    public void setFotoBitmap(Bitmap bitmap) {
        if(editarPerfilView != null)
            editarPerfilView.setFotoBitmap(bitmap);
    }

    @Override
    public void getFotoPerfil(String foto) {
        editarPerfilInteractor.getFotoPerfil(foto);
    }

}

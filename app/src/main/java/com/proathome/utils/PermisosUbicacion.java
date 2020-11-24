package com.proathome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.proathome.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PermisosUbicacion {

    public static void showAlert(Context context, int tipoPerfil) {
        new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos el permiso de ubicación para ofrecerte una mejor experiencia.")
                .setPositiveButton("Dar permiso", (dialog, which) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    new SweetAlert(context, SweetAlert.ERROR_TYPE, tipoPerfil)
                            .setTitleText("¡OH NO!")
                            .setContentText("No podemos continuar sin el permiso de ubicación.")
                            .show();
                })
                .setOnCancelListener(dialog -> {
                    new SweetAlert(context, SweetAlert.ERROR_TYPE, tipoPerfil)
                            .setTitleText("¡OH NO!")
                            .setContentText("No podemos continuar sin el permiso de ubicación.")
                            .show();
                })
                .show();
    }

    public static void showAlert(Context context, Activity activity, int tipoPerfil) {
        new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setTitle("Permisos de Ubicación")
                .setMessage("Necesitamos el permiso de ubicación para ofrecerte una mejor experiencia.")
                .setPositiveButton("Dar permiso", (dialog, which) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    new SweetAlert(context, SweetAlert.ERROR_TYPE, tipoPerfil)
                            .setTitleText("¡OH NO!")
                            .setContentText("No podemos continuar sin el permiso de ubicación.")
                            .setConfirmButton("OK", sweetAlertDialog -> {
                                activity.finish();
                            })
                            .show();
                })
                .setOnCancelListener(dialog -> {
                    new SweetAlert(context, SweetAlert.ERROR_TYPE, tipoPerfil)
                            .setTitleText("¡OH NO!")
                            .setContentText("No podemos continuar sin el permiso de ubicación.")
                            .setConfirmButton("OK", sweetAlertDialog -> {
                                activity.finish();
                            })
                            .show();
                })
                .show();
    }

}

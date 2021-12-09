package com.proathome.servicios.profesional;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelperProfesional extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelperProfesional(@Nullable Context context, @Nullable String name,
                                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sesionProfesional(id int PRIMARY KEY, idProfesional int, rangoServicio int, correo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

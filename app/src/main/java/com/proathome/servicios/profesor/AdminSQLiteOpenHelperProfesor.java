package com.proathome.servicios.profesor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelperProfesor extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelperProfesor(@Nullable Context context, @Nullable String name,
                                         @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sesionProfesor(id int PRIMARY KEY, idProfesor int, rangoClase int, correo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

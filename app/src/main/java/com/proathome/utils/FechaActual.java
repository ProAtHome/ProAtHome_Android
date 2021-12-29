package com.proathome.utils;

import android.os.Build;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaActual {

    public static String getFechaActual(){
        String fechaActual = null;
        DateTimeFormatter dtf = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaActual = dtf.format(LocalDateTime.now());
        }

        return fechaActual;
    }

}

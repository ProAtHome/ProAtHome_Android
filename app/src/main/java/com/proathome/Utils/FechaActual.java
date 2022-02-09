package com.proathome.Utils;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FechaActual {

    public static String getFechaActual(){
        String fechaActual = null;
        DateTimeFormatter dtf = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaActual = dtf.format(LocalDateTime.now());
        }else
            fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        return fechaActual;
    }

}

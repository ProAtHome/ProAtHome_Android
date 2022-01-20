package com.proathome.ui.examen;

public class EvaluarRuta {

    private int aciertos;
    public static final int BASIC1 = 1;
    public static final int BASIC2 = 2;
    public static final int BASIC3 = 3;
    public static final int BASIC4 = 4;
    public static final int BASIC5 = 5;
    public static final int INTERMEDIATE1 = 6;
    public static final int INTERMEDIATE2 = 7;
    public static final int INTERMEDIATE3 = 8;
    public static final int INTERMEDIATE4 = 9;
    public static final int INTERMEDIATE5 = 10;
    public static final int ADVANCED1 = 1;
    public static final int ADVANCED2 = 2;
    public static final int ADVANCED3 = 3;
    public static final int ADVANCED4 = 4;
    public static final int ADVANCED5 = 5;

    public EvaluarRuta(int aciertos){
        this.aciertos = aciertos;
    }

    public int getRuta(){
        int ruta = 0;
        if(this.aciertos >= 0 && this.aciertos <= 4)
            ruta = EvaluarRuta.BASIC1;
        else if(this.aciertos >=5 && this.aciertos <= 8)
            ruta = EvaluarRuta.BASIC2;
        else if(this.aciertos >=9 && this.aciertos <= 12)
            ruta = EvaluarRuta.BASIC3;
        else if(this.aciertos >=13 && this.aciertos <= 16)
            ruta = EvaluarRuta.BASIC4;
        else if(this.aciertos >=17 && this.aciertos <= 20)
            ruta = EvaluarRuta.BASIC5;
        else if(this.aciertos >= 21 && this.aciertos <= 24)
            ruta = EvaluarRuta.INTERMEDIATE1;
        else if(this.aciertos >= 25 && this.aciertos <= 28)
            ruta = EvaluarRuta.INTERMEDIATE2;
        else if(this.aciertos >= 29 && this.aciertos <= 34)
            ruta = EvaluarRuta.INTERMEDIATE3;
        else if(this.aciertos >= 33 && this.aciertos <= 36)
            ruta = EvaluarRuta.INTERMEDIATE4;
        else if(this.aciertos >= 37 && this.aciertos <= 40)
            ruta = EvaluarRuta.INTERMEDIATE5;
        else if (this.aciertos >= 41 && this.aciertos <= 45)
            ruta = EvaluarRuta.ADVANCED1;
        else if (this.aciertos >= 46 && this.aciertos <= 50)
            ruta = EvaluarRuta.ADVANCED2;
        else if (this.aciertos >= 51 && this.aciertos <= 55)
            ruta = EvaluarRuta.ADVANCED3;
        else if (this.aciertos >= 56 && this.aciertos <= 60)
            ruta = EvaluarRuta.ADVANCED4;
        else if (this.aciertos >= 61 && this.aciertos <= 65)
            ruta = EvaluarRuta.ADVANCED5;

        return ruta;

    }

    public String getRutaString(int ruta){
        String rutaString = "";
        if(ruta == EvaluarRuta.BASIC1)
            rutaString = "BASIC 1";
        else if(ruta == EvaluarRuta.BASIC2)
            rutaString = "BASIC 2";
        else if(ruta == EvaluarRuta.BASIC3)
            rutaString = "BASIC 3";
        else if(ruta == EvaluarRuta.BASIC4)
            rutaString = "BASIC 4";
        else if(ruta == EvaluarRuta.BASIC5)
            rutaString = "BASIC 5";
        else if(ruta == EvaluarRuta.INTERMEDIATE1)
            rutaString = "INTERMEDIATE 1";
        else if(ruta == EvaluarRuta.INTERMEDIATE2)
            rutaString = "INTERMEDIATE 2";
        else if(ruta == EvaluarRuta.INTERMEDIATE3)
            rutaString = "INTERMEDIATE 3";
        else if(ruta == EvaluarRuta.INTERMEDIATE4)
            rutaString = "INTERMEDIATE 4";
        else if(ruta == EvaluarRuta.INTERMEDIATE5)
            rutaString = "INTERMEDIATE 5";
        else if(ruta == EvaluarRuta.ADVANCED1)
            rutaString = "ADVANCED 1";
        else if(ruta == EvaluarRuta.ADVANCED2)
            rutaString = "ADVANCED 2";
        else if(ruta == EvaluarRuta.ADVANCED3)
            rutaString = "ADVANCED 3";
        else if(ruta == EvaluarRuta.ADVANCED5)
            rutaString = "ADVANCED 4";
        else if(ruta == EvaluarRuta.ADVANCED5)
            rutaString = "ADVANCED 5";

        return rutaString;

    }

    public int getSeccion(){
        int seccion = 0;
        if(this.aciertos >= 1 && this.aciertos <= 4)
            seccion = 1;
        else if(this.aciertos >=5 && this.aciertos <= 8)
            seccion = 1;
        else if(this.aciertos >=9 && this.aciertos <= 12)
            seccion = 1;
        else if(this.aciertos >=13 && this.aciertos <= 16)
            seccion = 1;
        else if(this.aciertos >=17 && this.aciertos <= 20)
            seccion = 1;
        else if(this.aciertos >= 21 && this.aciertos <= 24)
            seccion = 2;
        else if(this.aciertos >= 25 && this.aciertos <= 28)
            seccion = 2;
        else if(this.aciertos >= 29 && this.aciertos <= 34)
            seccion = 2;
        else if(this.aciertos >= 33 && this.aciertos <= 36)
            seccion = 2;
        else if(this.aciertos >= 37 && this.aciertos <= 40)
            seccion = 2;
        else if (this.aciertos >= 41 && this.aciertos <= 45)
            seccion = 3;
        else if (this.aciertos >= 46 && this.aciertos <= 50)
            seccion = 3;
        else if (this.aciertos >= 51 && this.aciertos <= 55)
            seccion = 3;
        else if (this.aciertos >= 56 && this.aciertos <= 60)
            seccion = 3;
        else if (this.aciertos >= 61 && this.aciertos <= 65)
            seccion = 3;

        return seccion;
    }

    public int getNivel(){
        int nivel = 0;
        if(this.aciertos >= 1 && this.aciertos <= 4)
            nivel = 1;
        else if(this.aciertos >=5 && this.aciertos <= 8)
            nivel = 2;
        else if(this.aciertos >=9 && this.aciertos <= 12)
            nivel = 3;
        else if(this.aciertos >=13 && this.aciertos <= 16)
            nivel = 4;
        else if(this.aciertos >=17 && this.aciertos <= 20)
            nivel = 5;
        else if(this.aciertos >= 21 && this.aciertos <= 24)
            nivel = 1;
        else if(this.aciertos >= 25 && this.aciertos <= 28)
            nivel = 2;
        else if(this.aciertos >= 29 && this.aciertos <= 34)
            nivel = 3;
        else if(this.aciertos >= 33 && this.aciertos <= 36)
            nivel = 4;
        else if(this.aciertos >= 37 && this.aciertos <= 40)
            nivel = 5;
        else if (this.aciertos >= 41 && this.aciertos <= 45)
            nivel = 1;
        else if (this.aciertos >= 46 && this.aciertos <= 50)
            nivel = 2;
        else if (this.aciertos >= 51 && this.aciertos <= 55)
            nivel = 3;
        else if (this.aciertos >= 56 && this.aciertos <= 60)
            nivel = 4;
        else if (this.aciertos >= 61 && this.aciertos <= 65)
            nivel = 5;

        return nivel;
    }

    public int getBloque(){
        return 1;
    }

}

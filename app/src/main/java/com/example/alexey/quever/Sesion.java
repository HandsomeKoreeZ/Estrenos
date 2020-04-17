package com.example.alexey.quever;

public class Sesion {
    private Cine cine;
    private Data dat;

    Sesion(){};

    Sesion (Cine c, Data d){
        this.cine = c;
        this.dat = d;
    }

    public Cine getCine() {
        return cine;
    }

    public void setCine(Cine cine) {
        this.cine = cine;
    }

    public Data getDat() {
        return dat;
    }

    public void setDat(Data dat) {
        this.dat = dat;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "cine=" + cine +
                "\ndat=" + dat +
                '}';
    }
}

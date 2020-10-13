package com.example.alexey.quever.Entities;



/**
 * date and time entitie that relate to movie
 */
public class Data {
    private String addressCine;
    private String titolPeli;
    private String dat;
    private String times;

    Data(){};

    public Data(String ad, String ti, String dat, String time){
        this.addressCine = ad;
        this.titolPeli = ti;
        this.dat = dat;
        this.times = time;
    }

    public String getAddressCine() {
        return addressCine;
    }

    public void setAddressCine(String addressCine) {
        this.addressCine = addressCine;
    }

    public String getTitolPeli() {
        return titolPeli;
    }

    public void setTitolPeli(String titolPeli) {
        this.titolPeli = titolPeli;
    }

    public String getDat() {
        return dat;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "Data{" +
                "addressCine='" + addressCine + '\'' +
                ", titolPeli='" + titolPeli + '\'' +
                ", dat='" + dat + '\'' +
                ", times='" + times + '\'' +
                '}';
    }
}

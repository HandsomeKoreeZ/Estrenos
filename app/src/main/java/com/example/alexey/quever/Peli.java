package com.example.alexey.quever;

import java.io.Serializable;

/**
 * Created by alexey on 21.05.2018.
 */

public class Peli implements Serializable {
    private String titol;
    private String genere;
    private String director;
    private String imagen;
    private String url;

    public Peli(){}

    public Peli(String titol, String genere, String director, String imagen, String url) {
        this.titol = titol;
        this.genere = genere;
        this.director = director;
        this.imagen = imagen;
        this.url = url;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Peli{" +
                "titol='" + titol + '\'' +
                ", genere='" + genere + '\'' +
                ", director='" + director + '\'' +
                ", imagen='" + imagen + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

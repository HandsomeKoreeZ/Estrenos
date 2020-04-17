package com.example.alexey.quever;

public class Cine {
    private String address;
    private String nombre;

    public Cine(){};

    public Cine(String nombre,String address) {
        this.address = address;
        this.nombre = nombre;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Cine{" +
                "address='" + address + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

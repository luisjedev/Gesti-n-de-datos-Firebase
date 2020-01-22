package com.example.herodex;

import android.net.Uri;


public class Heroe {

    private String id,nombre,rol;
    private int dificultad;
    private Uri url_heroe;

    public Heroe() {
        this.id="";
        this.nombre="";
        this.rol="";
        this.dificultad=0;
        this.url_heroe=null;
    }

    public Heroe(String nombre, String rol, int dificultad) {
        this.id="";
        this.nombre = nombre;
        this.rol = rol;
        this.dificultad = dificultad;
        this.url_heroe = null;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public int getDificultad() {
        return dificultad;
    }

    public Uri getUrl_heroe() {
        return url_heroe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(String posicion) {
        this.rol = posicion;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public void setUrl_heroe(Uri url_heroe) {
        this.url_heroe = url_heroe;
    }
}

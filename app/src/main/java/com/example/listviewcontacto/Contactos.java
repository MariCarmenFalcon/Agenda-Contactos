package com.example.listviewcontacto;

import android.net.Uri;

import java.io.Serializable;

public class Contactos implements Serializable {

    public int id;
    private Uri imagenUri;
    public String nombre;
    public String num;

    public Contactos(int id, Uri imagenUri, String nombre, String num) {
        this.id = id;
        this.imagenUri  = imagenUri;
        this.nombre = nombre;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getImagenUri() {
        return imagenUri;
    }

    public void setImagenUri(Uri imagenUri) {
        this.imagenUri = imagenUri;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}

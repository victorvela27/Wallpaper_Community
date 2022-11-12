package com.example.wallpaper_community.CategoriasAdmin.MusicaA;

public class Musica {

    private String imagen;
    private String nombre;
    private int vistas;

    public Musica() {
    }

    public Musica(String imagen, String nombre, int vistas) {
        this.imagen = imagen;
        this.nombre = nombre;

        this.vistas = vistas;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

}

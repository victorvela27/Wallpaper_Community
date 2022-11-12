package com.example.wallpaper_community.Categorias.Cat_FireBase;

public class CategioriaF {

    String categoria;
    String imagen;

    public CategioriaF() {
    }

    public CategioriaF(String categoria, String imagen) {
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

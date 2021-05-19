package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Capitulo {
    private String nombre;
    private int nVotos;
    private Double puntuacion;
    private List<Comentario> listaComentarios;

    public Capitulo() {
    }

    public Capitulo(String nombre, Double puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.nVotos = 0;
        this.listaComentarios = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getnVotos() {
        return nVotos;
    }

    public void setnVotos(int nVotos) {
        this.nVotos = nVotos;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }
}

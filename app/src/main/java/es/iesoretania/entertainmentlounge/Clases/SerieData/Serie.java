package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Serie {
    private String nombre;
    private String descripcion;
    private String genero;
    private Double puntuacion;
    private List<String> plataformas = new ArrayList<>();
    List<Temporada> temporadas = new ArrayList<>();

    public Serie() {
    }

    public Serie(String nombre, String descripcion, String genero, Double puntuacion, List<String> plataformas, List<Temporada> temporadas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.genero = genero;
        this.puntuacion = puntuacion;
        this.plataformas = plataformas;
        this.temporadas = temporadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<String> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
}

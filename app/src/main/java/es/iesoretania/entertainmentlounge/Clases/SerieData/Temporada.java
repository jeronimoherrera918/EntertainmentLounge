package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Temporada {
    private List<Capitulo> capitulos = new ArrayList<>();
    private int nVotos;
    private Double puntuacion;
    private Double puntuacionTotal;

    public Temporada() {
    }

    public Temporada(List<Capitulo> capitulos, Double puntuacion) {
        this.capitulos = capitulos;
        this.nVotos = 0;
        this.puntuacion = puntuacion;
        this.puntuacionTotal = 0.0;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Double getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(Double puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    public int getnVotos() {
        return nVotos;
    }

    public void setnVotos(int nVotos) {
        this.nVotos = nVotos;
    }
}

package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Temporada {
    private List<Capitulo> capitulos = new ArrayList<>();
    private Double puntuacion;

    public Temporada() {
    }

    public Temporada(List<Capitulo> capitulos, Double puntuacion) {
        this.capitulos = capitulos;
        this.puntuacion = puntuacion;
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
}

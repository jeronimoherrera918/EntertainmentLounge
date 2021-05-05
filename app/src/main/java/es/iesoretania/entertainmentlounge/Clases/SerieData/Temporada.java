package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Temporada {
    private List<Capitulo> capitulos = new ArrayList<>();

    public Temporada() {
    }

    public Temporada(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }
}

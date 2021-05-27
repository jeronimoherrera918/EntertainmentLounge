package es.iesoretania.entertainmentlounge.Clases.SaveSerieData;

import java.util.ArrayList;
import java.util.List;

public class SaveTemporadaSerie {
    private List<Integer> capitulos_vistos = new ArrayList<>();
    private List<Double> capitulos_puntuacion = new ArrayList<>();
    private boolean vistaCompleta;

    public SaveTemporadaSerie() {
    }

    public SaveTemporadaSerie(List<Integer> capitulos_vistos, List<Double> capitulos_puntuacion) {
        this.capitulos_vistos = capitulos_vistos;
        this.capitulos_puntuacion = capitulos_puntuacion;
        this.vistaCompleta = false;
    }

    public List<Integer> getCapitulos_vistos() {
        return capitulos_vistos;
    }

    public void setCapitulos_vistos(List<Integer> capitulos_vistos) {
        this.capitulos_vistos = capitulos_vistos;
    }

    public List<Double> getCapitulos_puntuacion() {
        return capitulos_puntuacion;
    }

    public void setCapitulos_puntuacion(List<Double> capitulos_puntuacion) {
        this.capitulos_puntuacion = capitulos_puntuacion;
    }

    public boolean isVistaCompleta() {
        return vistaCompleta;
    }

    public void setVistaCompleta(boolean vistaCompleta) {
        this.vistaCompleta = vistaCompleta;
    }
}

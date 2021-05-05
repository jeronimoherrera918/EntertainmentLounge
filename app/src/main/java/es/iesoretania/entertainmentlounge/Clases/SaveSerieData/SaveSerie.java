package es.iesoretania.entertainmentlounge.Clases.SaveSerieData;

import java.util.ArrayList;

public class SaveSerie {
    private String id_serie;
    private ArrayList<SaveTemporadaSerie> temporadas = new ArrayList<>();

    public SaveSerie() {
    }

    public SaveSerie(String id_serie, ArrayList<SaveTemporadaSerie> temporadas) {
        this.id_serie = id_serie;
        this.temporadas = temporadas;
    }

    public String getId_serie() {
        return id_serie;
    }

    public void setId_serie(String id_serie) {
        this.id_serie = id_serie;
    }

    public ArrayList<SaveTemporadaSerie> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(ArrayList<SaveTemporadaSerie> temporadas) {
        this.temporadas = temporadas;
    }
}

package es.iesoretania.entertainmentlounge.Clases.SerieData;

public class Capitulo {
    private String nombre;
    private Double puntuacion;

    public Capitulo() {
    }

    public Capitulo(String nombre, Double puntuacion) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
    }
}

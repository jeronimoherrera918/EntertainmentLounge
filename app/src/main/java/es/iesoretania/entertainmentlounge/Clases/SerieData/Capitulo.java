package es.iesoretania.entertainmentlounge.Clases.SerieData;

public class Capitulo {
    private String nombre;
    private int nVotos;
    private Double puntuacion;

    public Capitulo() {
    }

    public Capitulo(String nombre, int nVotos, Double puntuacion) {
        this.nombre = nombre;
        this.nVotos = nVotos;
        this.puntuacion = puntuacion;
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
}

package es.iesoretania.entertainmentlounge.Clases.SerieData;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Serie implements Parcelable {
    private String id_serie;
    private String nombre;
    private String descripcion;
    private String genero;
    private Double puntuacion;
    private Double puntuacionTotal;
    private List<String> plataformas = new ArrayList<>();
    List<Temporada> temporadas = new ArrayList<>();

    public Serie() {
    }

    public Serie(String id_serie, String nombre, String descripcion, String genero, Double puntuacion, List<String> plataformas, List<Temporada> temporadas) {
        this.id_serie = id_serie;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.genero = genero;
        this.puntuacion = puntuacion;
        this.puntuacionTotal = 0.0;
        this.plataformas = plataformas;
        this.temporadas = temporadas;
    }

    protected Serie(Parcel in) {
        id_serie = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        genero = in.readString();
        if (in.readByte() == 0) {
            puntuacion = null;
        } else {
            puntuacion = in.readDouble();
        }
        if (in.readByte() == 0) {
            puntuacionTotal = null;
        } else {
            puntuacionTotal = in.readDouble();
        }
        plataformas = in.createStringArrayList();
    }

    public static final Creator<Serie> CREATOR = new Creator<Serie>() {
        @Override
        public Serie createFromParcel(Parcel in) {
            return new Serie(in);
        }

        @Override
        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };

    public String getId_serie() {
        return id_serie;
    }

    public void setId_serie(String id_serie) {
        this.id_serie = id_serie;
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

    public Double getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(Double puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_serie);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(genero);
        if (puntuacion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(puntuacion);
        }
        if (puntuacionTotal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(puntuacionTotal);
        }
        dest.writeStringList(plataformas);
    }
}

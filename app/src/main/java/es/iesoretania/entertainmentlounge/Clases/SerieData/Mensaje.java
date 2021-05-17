package es.iesoretania.entertainmentlounge.Clases.SerieData;

import com.google.firebase.Timestamp;

public class Mensaje {
    private String idMensaje;
    private String idEmisor;
    private String idReceptor;
    private String mensaje;
    private Timestamp fecha;

    public Mensaje() {
    }

    public Mensaje(String idMensaje, String idEmisor, String idReceptor, String mensaje, Timestamp fecha) {
        this.idMensaje = idMensaje;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}

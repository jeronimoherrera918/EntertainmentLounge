package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String idReceptor;
    private List<Mensaje> listaMensajes = new ArrayList<>();

    public Chat() {
    }

    public Chat(String idReceptor, List<Mensaje> listaMensajes) {
        this.idReceptor = idReceptor;
        this.listaMensajes = listaMensajes;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public List<Mensaje> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(List<Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }
}

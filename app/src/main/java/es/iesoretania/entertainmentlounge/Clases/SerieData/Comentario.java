package es.iesoretania.entertainmentlounge.Clases.SerieData;

import java.util.ArrayList;
import java.util.List;

public class Comentario {
    private String comentario;
    private String id_usuario;
    private String id_comentario;
    private Integer nLikes;
    private List<String> ids_likes;

    public Comentario() {
    }

    public Comentario(String comentario, String id_usuario, String id_comentario) {
        this.comentario = comentario;
        this.id_usuario = id_usuario;
        this.id_comentario = id_comentario;
        this.nLikes = 0;
        ids_likes = new ArrayList<>();
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(String id_comentario) {
        this.id_comentario = id_comentario;
    }

    public Integer getnLikes() {
        return nLikes;
    }

    public void setnLikes(Integer nLikes) {
        this.nLikes = nLikes;
    }

    public List<String> getIds_likes() {
        return ids_likes;
    }

    public void setIds_likes(List<String> ids_likes) {
        this.ids_likes = ids_likes;
    }
}

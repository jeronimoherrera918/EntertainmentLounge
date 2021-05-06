package es.iesoretania.entertainmentlounge.Clases.SerieData;

public class Comentario {
    private String comentario;
    private String id_usuario;
    private int num_likes;

    public Comentario() {
    }

    public Comentario(String comentario, String id_usuario, int num_likes) {
        this.comentario = comentario;
        this.id_usuario = id_usuario;
        this.num_likes = num_likes;
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

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }
}

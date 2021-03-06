package es.iesoretania.entertainmentlounge.Clases;

import java.util.ArrayList;
import java.util.List;

public class Usuario{
    private String email;
    private String nickname;
    private String nombre_completo;
    private String fechaNacimiento;
    private String fotoPerfil;
    private List<String> listaAmigos;

    public Usuario() {

    }

    public Usuario(String email, String nickname, String nombre_completo, String fechaNacimiento, String fotoPerfil) {
        this.email = email;
        this.nickname = nickname;
        this.nombre_completo = nombre_completo;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.listaAmigos = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<String> getListaAmigos() {
        return listaAmigos;
    }

    public void setListaAmigos(List<String> listaAmigos) {
        this.listaAmigos = listaAmigos;
    }
}

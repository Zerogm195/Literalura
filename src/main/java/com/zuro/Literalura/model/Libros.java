package com.zuro.Literalura.model;

import jakarta.persistence.*;

import java.util.OptionalDouble;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    private String autor;
    private String idiomas;
    private Integer numerodeDescargas;

    public Libros(){};

    //

    public Libros(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        this.autor = datosLibros.autores() != null && !datosLibros.autores().isEmpty() ? datosLibros.autores().get(0).nombre() : "Desconocido";
        this.idiomas = datosLibros.idiomas() != null && !datosLibros.idiomas().isEmpty() ? datosLibros.idiomas().get(0) : "Desconocido";
        this.numerodeDescargas = datosLibros.numerodeDescargas() != null ? datosLibros.numerodeDescargas() : 0;
    }

    //Getters and Setters

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public int getNumerodeDescargas() {
        return numerodeDescargas;
    }
    public void setNumerodeDescargas(int numerodeDescargas) {
        this.numerodeDescargas = numerodeDescargas;
    }
    public String getIdiomas() {
        return idiomas;
    }
    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

//

    @Override
    public String toString() {
        return "Titulo: " + titulo + "\n"+
                "Autor: " + autor + "\n"+
                "Numero de descargas: " + numerodeDescargas +  "\n"+
                "Idioma: " + idiomas.replace("es","Español");
    }


}

package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LibroCatalogado {
    String isbn;
    String titulo;
    String autor;
    int numEjemplares;


    public LibroCatalogado() {}


    public LibroCatalogado(Libro libro) {
        this.isbn = libro.getIsbn();
        this.titulo = libro.getTitulo();
        this.autor = libro.getAutor();
        this.numEjemplares = 1; // El primer ejemplar
    }


    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getNumEjemplares() { return numEjemplares; }
    public void setNumEjemplares(int numEjemplares) { this.numEjemplares = numEjemplares; }

    public void incrementarEjemplares(int cantidad) {
        this.numEjemplares += cantidad;
    }
    public void decrementarEjemplares(int cantidad) {
        this.numEjemplares -= cantidad;
    }
}

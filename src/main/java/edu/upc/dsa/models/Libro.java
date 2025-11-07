package edu.upc.dsa.models;
import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement
    public class Libro {
        String id;
        String isbn;
        String titulo;
        String editorial;
        int anoPublicacion;
        String autor;
        String tematica;

        public Libro() {}

        public Libro(String id, String isbn, String titulo, String autor) {
            this.id = id;
            this.isbn = isbn;
            this.titulo = titulo;
            this.autor = autor;
        }


        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getEditorial() { return editorial; }
        public void setEditorial(String editorial) { this.editorial = editorial; }
        public int getAnoPublicacion() { return anoPublicacion; }
        public void setAnoPublicacion(int anoPublicacion) { this.anoPublicacion = anoPublicacion; }
        public String getAutor() { return autor; }
        public void setAutor(String autor) { this.autor = autor; }
        public String getTematica() { return tematica; }
        public void setTematica(String tematica) { this.tematica = tematica; }
    }


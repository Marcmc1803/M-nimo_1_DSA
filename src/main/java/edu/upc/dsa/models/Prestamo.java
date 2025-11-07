package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Prestamo {
    String id;
    String lectorId;
    String libroIsbn;
    String fechaPrestamo;
    String fechaDevolucion;
    String estado;

    // Constructor vacío (OBLIGATORIO)
    public Prestamo() {}

    public Prestamo(String id, String lectorId, String libroIsbn) {
        this.id = id;
        this.lectorId = lectorId;
        this.libroIsbn = libroIsbn;
        this.fechaPrestamo = new Date().toString();
        long quinceDias = 1000L * 60 * 60 * 24 * 15;
        this.fechaDevolucion = new Date(System.currentTimeMillis() + quinceDias).toString();
        this.estado = "En trámite";
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLectorId() { return lectorId; }
    public void setLectorId(String lectorId) { this.lectorId = lectorId; }
    public String getLibroIsbn() { return libroIsbn; }
    public void setLibroIsbn(String libroIsbn) { this.libroIsbn = libroIsbn; }
    public String getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public String getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(String fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

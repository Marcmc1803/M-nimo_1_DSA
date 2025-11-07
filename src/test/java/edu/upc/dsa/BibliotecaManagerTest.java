package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Libro;
import edu.upc.dsa.models.LibroCatalogado;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BibliotecaManagerTest {

    BibliotecaManager bm;

    @Before
    public void setUp() {


        this.bm = BibliotecaManagerImpl.getInstance();

        this.bm.agregarLector(new Lector("lector1", "Marc", "Martín", "5678JG45Q"));

        for (int i = 1; i <= 10; i++) {
            this.bm.almacenarLibro(new Libro("ID" + i, "ISBN" + i, "Libro " + i, "Autor"));
        }


        this.bm.almacenarLibro(new Libro("ID11", "ISBN456", "Libro 11", "Autor"));
        this.bm.almacenarLibro(new Libro("ID12", "ISBN789", "Libro 12", "Autor"));
    }

    @After
    public void tearDown() {
        this.bm.reiniciar();
    }

    @Test
    public void testAlmacenarLibro() {
        assertEquals(2, this.bm.totalMontones());
        assertEquals(12, this.bm.totalLibrosAlmacen());

        for (int i = 13; i <= 20; i++) {
            this.bm.almacenarLibro(new Libro("ID" + i, "ISBNXXX", "Libro " + i, "Autor"));
        }
        assertEquals(2, this.bm.totalMontones());
        assertEquals(20, this.bm.totalLibrosAlmacen());

        this.bm.almacenarLibro(new Libro("ID21", "ISBNYYY", "Libro 21", "Autor"));
        assertEquals(3, this.bm.totalMontones());
        assertEquals(21, this.bm.totalLibrosAlmacen());
    }

    @Test
    public void testCatalogarLibro() throws AlmacenVacioException {



        assertEquals("Libro 10", this.bm.catalogarLibro().getTitulo());
        assertEquals("Libro 9", this.bm.catalogarLibro().getTitulo());
        for (int i = 0; i < 7; i++) this.bm.catalogarLibro();
        assertEquals("Libro 1", this.bm.catalogarLibro().getTitulo());

        assertEquals(1, this.bm.totalMontones());

        assertEquals("Libro 12", this.bm.catalogarLibro().getTitulo());
        assertEquals("Libro 11", this.bm.catalogarLibro().getTitulo());

        assertEquals(0, this.bm.totalMontones());
        assertEquals(0, this.bm.totalLibrosAlmacen());
    }

    @Test
    public void testCatalogarLibroDuplicado() throws AlmacenVacioException {

        this.bm.reiniciar();

        this.bm.almacenarLibro(new Libro("ID1", "ISBN123", "Ready Player One", "Anonimo"));
        this.bm.almacenarLibro(new Libro("ID2", "ISBN123", "Daredevil:Born Again", "Andrés"));
        this.bm.almacenarLibro(new Libro("ID3", "ISBN456", "Recetas históricas", "Yaya"));

        assertEquals(1, this.bm.totalMontones());
        assertEquals(0, this.bm.totalLibrosCatalogados());

        this.bm.catalogarLibro();
        assertEquals(1, this.bm.totalLibrosCatalogados());
        assertEquals(1, this.bm.obtenerLibroCatalogado("ISBN456").getNumEjemplares());

        this.bm.catalogarLibro();
        assertEquals(2, this.bm.totalLibrosCatalogados());
        assertEquals(1, this.bm.obtenerLibroCatalogado("ISBN123").getNumEjemplares());

        assertEquals("Daredevil:Born Again", this.bm.obtenerLibroCatalogado("ISBN123").getTitulo());


        this.bm.catalogarLibro();
        assertEquals(2, this.bm.totalLibrosCatalogados());
        assertEquals(2, this.bm.obtenerLibroCatalogado("ISBN123").getNumEjemplares());

        assertEquals("Daredevil:Born Again", this.bm.obtenerLibroCatalogado("ISBN123").getTitulo());
    }

    @Test
    public void testPrestarLibro() throws Exception {
        LibroCatalogado libroCat = this.bm.catalogarLibro();
        String isbn = libroCat.getIsbn();

        assertEquals(1, this.bm.obtenerLibroCatalogado(isbn).getNumEjemplares());

        this.bm.prestarLibro("p1", "lector1", isbn);

        assertEquals(0, this.bm.obtenerLibroCatalogado(isbn).getNumEjemplares());
        assertEquals(1, this.bm.consultarPrestamosLector("lector1").size());
    }

    @Test(expected = SinEjemplaresException.class)
    public void testPrestarLibroErrorSinEjemplares() throws Exception {
        LibroCatalogado libroCat = this.bm.catalogarLibro();
        String isbn = libroCat.getIsbn();

        this.bm.prestarLibro("p1", "lector1", isbn);
        assertEquals(0, this.bm.obtenerLibroCatalogado(isbn).getNumEjemplares());

        this.bm.prestarLibro("p2", "lector1", isbn);
    }
}
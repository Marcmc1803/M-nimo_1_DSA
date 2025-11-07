package edu.upc.dsa;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Libro;
import edu.upc.dsa.models.LibroCatalogado;
import edu.upc.dsa.models.Prestamo;
import org.apache.log4j.Logger;

import java.util.*;

public class BibliotecaManagerImpl implements BibliotecaManager {


    private static BibliotecaManager instance;
    private static final Logger logger = Logger.getLogger(BibliotecaManagerImpl.class);


    protected Map<String, Lector> lectores;
    protected Map<String, LibroCatalogado> catalogo;
    protected List<Prestamo> prestamos;
    protected Queue<Stack<Libro>> almacen;


    private BibliotecaManagerImpl() {
        this.lectores = new HashMap<>();
        this.catalogo = new HashMap<>();
        this.prestamos = new ArrayList<>();
        this.almacen = new LinkedList<>();
    }
// Singleton
    public static BibliotecaManager getInstance() {
        if (instance == null) {
            instance = new BibliotecaManagerImpl();
        }
        return instance;
    }

    @Override
    public Lector agregarLector(Lector lector) {
        logger.info("agregarLector(" + lector + ")");
        this.lectores.put(lector.getId(), lector);
        logger.info("Lector añadido/actualizado: " + lector.getNombre());
        return lector;
    }

    @Override
    public void almacenarLibro(Libro libro) {
        logger.info("almacenarLibro(" + libro + ")");

        Stack<Libro> ultimoMonton = ((LinkedList<Stack<Libro>>) this.almacen).peekLast();

        if (ultimoMonton == null || ultimoMonton.size() == 10) {
            logger.info("Estamos creando un nuevo montón...");
            ultimoMonton = new Stack<>();
            this.almacen.add(ultimoMonton);
        }

        ultimoMonton.push(libro);
        logger.info("Libro " + libro.getId() + " apilado. Tamaño del montón: " + ultimoMonton.size());
    }

    @Override
    public LibroCatalogado catalogarLibro() throws AlmacenVacioException {
        logger.info("catalogarLibro()");

        Stack<Libro> primerMonton = this.almacen.peek();

        if (primerMonton == null) {
            logger.error("El almacén está vacío. No se puede catalogar.");
            throw new AlmacenVacioException();
        }

        Libro libroACatalogar = primerMonton.pop();
        logger.info("Catalogando libro: " + libroACatalogar.getId() + " (ISBN: " + libroACatalogar.getIsbn() + ")");

        if (primerMonton.isEmpty()) {
            logger.info("El montón ha quedado vacío. Eliminando montón de la cola.");
            this.almacen.poll();
        }

        LibroCatalogado libroCat = this.catalogo.get(libroACatalogar.getIsbn());

        if (libroCat == null) {
            logger.info("Lo añadimos al catálogo.");
            libroCat = new LibroCatalogado(libroACatalogar);
            this.catalogo.put(libroCat.getIsbn(), libroCat);
        } else {
            logger.info("ISBN repetido, Sumamos ejemplares.");
            libroCat.incrementarEjemplares(1);
        }

        logger.info("Libro catalogado, ejemplares de este ISBN: " + libroCat.getNumEjemplares());
        return libroCat;
    }

    @Override
    public Prestamo prestarLibro(String idPrestamo, String idLector, String isbnLibro) throws LectorNoEncontradoException, LibroNoEncontradoException, SinEjemplaresException {
        logger.info("prestarLibro(lectorId: " + idLector + ", isbn: " + isbnLibro + ")");

        Lector lector = this.lectores.get(idLector);
        if (lector == null) {
            logger.error("Lector no encontrado: " + idLector);
            throw new LectorNoEncontradoException();
        }

        LibroCatalogado libro = this.catalogo.get(isbnLibro);
        if (libro == null) {
            logger.error("Libro (ISBN) no encontrado en el catálogo: " + isbnLibro);
            throw new LibroNoEncontradoException();
        }

        if (libro.getNumEjemplares() == 0) {
            logger.error("No quedan ejemplares con este ISBN: " + isbnLibro);
            throw new SinEjemplaresException();
        }

        libro.decrementarEjemplares(1);
        Prestamo prestamo = new Prestamo(idPrestamo, idLector, isbnLibro);
        this.prestamos.add(prestamo);

        logger.info("Préstamo " + idPrestamo + " Hecho. Quedan : " + libro.getNumEjemplares());
        return prestamo;
    }

    @Override
    public List<Prestamo> consultarPrestamosLector(String idLector) {
        logger.info("consultarPrestamosLector(idLector: " + idLector + ")");
        List<Prestamo> prestamosLector = new ArrayList<>();

        for (Prestamo p : this.prestamos) {
            if (p.getLectorId().equals(idLector)) {
                prestamosLector.add(p);
            }
        }

        logger.info("Se han encontrado " + prestamosLector.size() + " préstamos para el lector " + idLector);
        return prestamosLector;
    }

    // Métodos que he creado
    @Override
    public Lector obtenerLector(String idLector) { return this.lectores.get(idLector); }
    @Override
    public LibroCatalogado obtenerLibroCatalogado(String isbn) { return this.catalogo.get(isbn); }
    @Override
    public int totalLectores() { return this.lectores.size(); }
    @Override
    public int totalLibrosAlmacen() {
        int total = 0;
        for (Stack<Libro> monton : this.almacen) {
            total += monton.size();
        }
        return total;
    }
    @Override
    public int totalMontones() { return this.almacen.size(); }
    @Override
    public int totalLibrosCatalogados() { return this.catalogo.size(); }
    @Override
    public void reiniciar() {
        logger.info("reiniciar()");
        this.lectores.clear();
        this.catalogo.clear();
        this.prestamos.clear();
        this.almacen.clear();
        logger.info("Sistema reiniciado.");
    }
}
package edu.upc.dsa;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Libro;
import edu.upc.dsa.models.LibroCatalogado;
import edu.upc.dsa.models.Prestamo;

import java.util.List;
//He añadido las excepciones separadas en el exceptions ya que juntas me daba error
public interface BibliotecaManager {

    Lector agregarLector(Lector lector);
    void almacenarLibro(Libro libro);
    LibroCatalogado catalogarLibro() throws AlmacenVacioException;
    Prestamo prestarLibro(String idPrestamo, String idLector, String isbnLibro) throws LectorNoEncontradoException, LibroNoEncontradoException, SinEjemplaresException;
    List<Prestamo> consultarPrestamosLector(String idLector);


    Lector obtenerLector(String idLector);
    LibroCatalogado obtenerLibroCatalogado(String isbn);
    int totalLectores();
    int totalLibrosAlmacen();
    int totalMontones();
    int totalLibrosCatalogados();
    void reiniciar();
}

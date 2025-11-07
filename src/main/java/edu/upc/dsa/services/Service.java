package edu.upc.dsa.services;

import edu.upc.dsa.BibliotecaManager;
import edu.upc.dsa.BibliotecaManagerImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Lector;
import edu.upc.dsa.models.Libro;
import edu.upc.dsa.models.LibroCatalogado;
import edu.upc.dsa.models.Prestamo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/biblioteca", description = "Endpoint del Servicio de Biblioteca")
@Path("/biblioteca")
public class Service {

    private BibliotecaManager bm;

    public Service() {
        this.bm = BibliotecaManagerImpl.getInstance();

        if (this.bm.totalLectores() == 0) {
            // Datos inventados para probar la API
            this.bm.agregarLector(new Lector("lector1", "Marc", "Martín", "54756996A"));
            this.bm.agregarLector(new Lector("lector2", "Axel", "Blaze", "465575757B"));
            this.bm.almacenarLibro(new Libro("L1", "ISBN123", "Inazuma Eleven", "Nakata"));
        }
    }

    // Añadimos o actualizamos lector
    @POST
    @Path("/lectores")
    @ApiOperation(value = "Añadir o actualizar un lector", notes = "Añade un lector nuevo o actualiza sus datos si el ID ya existe")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lector añadido/actualizado", response = Lector.class)
    })
    public Response agregarLector(Lector lector) {
        if (lector.getId() == null || lector.getNombre() == null) {
            return Response.status(400).entity("Faltan datos ").build();
        }
        Lector l = this.bm.agregarLector(lector);
        return Response.status(201).entity(l).build();
    }

    // Almacenamos un libro
    @POST
    @Path("/almacen/libros")
    @ApiOperation(value = "Almacenar un libro", notes = "Añade un libro al almacén (montones)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Libro almacenado", response = Libro.class)
    })
    public Response almacenarLibro(Libro libro) {
        if (libro.getId() == null || libro.getIsbn() == null) {
            return Response.status(400).entity("Faltan datos ").build();
        }
        this.bm.almacenarLibro(libro);
        return Response.status(201).entity(libro).build();
    }

    // Catalogar un libro
    @GET 
    @Path("/catalogo/catalogar")
    @ApiOperation(value = "Catalogar el siguiente libro", notes = "Saca el siguiente libro del almacén y lo añade al catálogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Libro catalogado", response = LibroCatalogado.class),
            @ApiResponse(code = 404, message = "Almacén vacío")
    })
    public Response catalogarLibro() {
        try {
            LibroCatalogado lc = this.bm.catalogarLibro();
            return Response.status(200).entity(lc).build();
        } catch (AlmacenVacioException e) {
            return Response.status(404).entity("Almacén vacío").build();
        }
    }

    // Prestamos un libro
    @POST
    @Path("/prestamos")
    @ApiOperation(value = "Realizar un préstamo", notes = "Crea un nuevo préstamo de un libro a un lector.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Préstamo realizado", response = Prestamo.class),
            @ApiResponse(code = 404, message = "Lector o Libro no encontrado"),
            @ApiResponse(code = 409, message = "Sin ejemplares disponibles (Conflicto)")
    })
    public Response prestarLibro(Prestamo prestamoRequest) {

        
        if (prestamoRequest.getLectorId() == null || prestamoRequest.getLibroIsbn() == null) {
            return Response.status(400).entity("Faltan datos (lectorId, libroIsbn)").build();
        }
        try {

            String idPrestamo = (prestamoRequest.getId() != null) ? prestamoRequest.getId() : "P" + System.currentTimeMillis();
            
            Prestamo p = this.bm.prestarLibro(idPrestamo, prestamoRequest.getLectorId(), prestamoRequest.getLibroIsbn());

            return Response.status(201).entity(p).build();
        } catch (LectorNoEncontradoException e) {
            return Response.status(404).entity("Lector no encontrado").build();
        } catch (LibroNoEncontradoException e) {
            return Response.status(404).entity("Libro no encontrado en el catálogo").build();
        } catch (SinEjemplaresException e) {
            return Response.status(409).entity("No quedan ejemplares").build();
        }
    }

    //Consultamos los préstamos que ha hecho un lector
    @GET
    @Path("/prestamos/lector/{idLector}")
    @ApiOperation(value = "Consultar préstamos de un lector", notes = "Retorna la lista de préstamos de un lector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista OK", response = Prestamo.class, responseContainer = "List")
    })
    public Response consultarPrestamosLector(@PathParam("idLector") String idLector) {
        List<Prestamo> prestamos = this.bm.consultarPrestamosLector(idLector);
        GenericEntity<List<Prestamo>> entity = new GenericEntity<List<Prestamo>>(prestamos) {};
        return Response.status(200).entity(entity).build();
    }
}
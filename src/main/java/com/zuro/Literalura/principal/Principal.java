package com.zuro.Literalura.principal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuro.Literalura.model.DatosLibros;
import com.zuro.Literalura.model.Libros;
import com.zuro.Literalura.repository.LibrosRepository;
import com.zuro.Literalura.service.ConsumoAPI;
import com.zuro.Literalura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books";
    private LibrosRepository repositorio;
    private ConvierteDatos conversor = new ConvierteDatos();

    public Principal(LibrosRepository repository) {
        this.repositorio = repository;
    }

    public void muestraMenu() throws JsonProcessingException {

        boolean ciclo = true;

        while (ciclo){

            var menu = """
                              \n
                              1 - Consultar libro por título
                              
                              0 - Salir\n""";

            System.out.println(menu);
            var opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){

                case 1:
                    consultarLibrosporTitulo();
                    break;

                case 2:
                    consultarLibrosporAutor();
                    break;
                case 3:
                    System.out.println("Escribe el código del idioma (ej. 'en' para inglés):");
                    String idioma = teclado.nextLine();
                    listarLibrosPorIdioma(idioma);
                    break;
                case 4:
                    System.out.println("Escribe el año de inicio:");
                    int anioInicio = teclado.nextInt();
                    System.out.println("Escribe el año de fin:");
                    int anioFin = teclado.nextInt();
                    listarLibrosPorRangoDeAnos(anioInicio, anioFin);
                    break;
                case 0:
                    System.out.println("\nSaliendo de la aplicación\n");
                    ciclo = false;
                    break;
                default :
                    System.out.println("Opción no válida, intenta nuevamente.");
            }

        }

    }

    private void consultarLibrosporAutor() throws JsonProcessingException {
        System.out.println("Escribe el nombre del autor que quieres buscar:\n");
        var autorName = teclado.nextLine();

        Optional<List<DatosLibros>> optionalLibros = obtenerDatosAutor(autorName);

        if (optionalLibros.isPresent()) {
            List<DatosLibros> librosAutor = optionalLibros.get();

            for (DatosLibros datos : librosAutor) {

                Optional<Libros> libroExistente = repositorio.findByTitulo(datos.titulo());

                if (libroExistente.isPresent()) {

                    System.out.println("El libro \"" + datos.titulo() + "\" ya está agregado.");

                } else {

                    Libros nuevoLibro = new Libros(datos);

                    repositorio.save(nuevoLibro);


                    System.out.println("Se ha añadido el libro \"" + datos.titulo() + "\" satisfactoriamente.");
                }

            }

        } else {

            System.out.println("No se encontraron libros para el autor especificado.");

        }
    }


    private Optional<List<DatosLibros>> obtenerDatosAutor(String autorName) throws JsonProcessingException {

        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + autorName.replace(" ", "+"));

        System.out.println(json);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode lectura = objectMapper.readTree(json);

        JsonNode resultados = lectura.path("results");

        if (resultados.isEmpty() || resultados.isNull()) {

            System.out.println("No se encontraron libros para el autor especificado.");
            return Optional.empty();

        }

        List<DatosLibros> librosAutor = new ArrayList<>();

        for (JsonNode nodoLibro : resultados) {

            DatosLibros libro = objectMapper.treeToValue(nodoLibro, DatosLibros.class);

            librosAutor.add(libro);

        }

        return Optional.of(librosAutor);
    }


    private Optional <DatosLibros> obtenerDatosLibros() throws JsonProcessingException {

        System.out.println("Escribe el nombre del libro que quieres buscar:\n");

        var libroName = teclado.nextLine();

        var json =  consumoApi.obtenerDatos(URL_BASE + "?search=" + libroName.replace(" ", "+"));

        System.out.println(json);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode lectura = objectMapper.readTree(json);
        JsonNode resultadopath = lectura.path("results").get(0);

        if (resultadopath == null || resultadopath.isNull()) {
            return Optional.empty();
        }

        DatosLibros datos = objectMapper.treeToValue(resultadopath, DatosLibros.class);

        return Optional.of(datos);

    }

    private void consultarLibrosporTitulo() throws JsonProcessingException {

        Optional<DatosLibros> optionalDatos = obtenerDatosLibros();

        if (optionalDatos.isPresent()) {

            String titulo = optionalDatos.get().titulo();

            Optional<Libros> libroExistente = repositorio.findByTitulo(titulo);

            if (libroExistente.isPresent()) {

                System.out.println("Ese libro ya está agregado");

            } else {

                Libros libros = new Libros(optionalDatos.get());

                repositorio.save(libros);

                System.out.println("Se ha añadido " + titulo + " satisfactoriamente");

                System.out.println(libros);

            }

        } else {

            System.out.println("\nNo se encontraron datos para el libro especificado.");

        }

    }

    private void listarLibrosPorIdioma(String idioma) throws JsonProcessingException {
        String url = URL_BASE + "?languages=" + idioma;
        var json = consumoApi.obtenerDatos(url);
        System.out.println(json);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode lectura = objectMapper.readTree(json);
        JsonNode resultados = lectura.path("results");

        if (resultados.isEmpty() || resultados.isNull()) {
            System.out.println("No se encontraron libros para el idioma especificado.");
            return;
        }

        List<DatosLibros> librosIdioma = new ArrayList<>();
        for (JsonNode nodoLibro : resultados) {
            DatosLibros libro = objectMapper.treeToValue(nodoLibro, DatosLibros.class);
            librosIdioma.add(libro);
            System.out.println("Título: " + libro.titulo() + ", Idiomas: " + libro.idiomas());
        }
    }

    private void listarLibrosPorRangoDeAnos(int añoInicio, int añoFin) throws JsonProcessingException {
        String url = URL_BASE + "?author_year_start=" + añoInicio + "&author_year_end=" + añoFin;
        var json = consumoApi.obtenerDatos(url);
        System.out.println(json);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode lectura = objectMapper.readTree(json);
        JsonNode resultados = lectura.path("results");

        if (resultados.isEmpty() || resultados.isNull()) {
            System.out.println("No se encontraron libros para el rango de años especificado.");
            return;
        }

        List<DatosLibros> librosPorAnos = new ArrayList<>();
        for (JsonNode nodoLibro : resultados) {
            DatosLibros libro = objectMapper.treeToValue(nodoLibro, DatosLibros.class);
            librosPorAnos.add(libro);
            System.out.println("Título: " + libro.titulo() + ", Autor(es): " + libro.autores() + ", Años de vida: "
                    + libro.autores().get(0).anioNacimiento() + " - " + libro.autores().get(0).anioMuerte());
        }
    }


}

package ar.edu.utn.frbb.tup.persistence.DAO;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseDao<T> {

    // Método que inicializa un archivo con encabezado
    public void inicializarArchivo(String encabezado, String rutaArchivo) {
        Path path = Paths.get(rutaArchivo); // Utiliza Paths para trabajar con rutas de archivos
        if (Files.notExists(path)) { // Verifica si el archivo no existe
            try {
                Files.write(path, encabezado.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo", e); // Maneja la excepción con más contexto
            }
        }
    }

    // Metodo para guardar información en el archivo
    public void saveInfo(String info, String rutaArchivo) {
        try {
            // Escribe la información en el archivo, agregando una nueva línea
            Files.write(Paths.get(rutaArchivo), info.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e); // Maneja la excepción lanzando una RuntimeException
        }
    }

    // Metodo para eliminar información del archivo basado en un ID
    public void deleteInfo(long id, String rutaArchivo) {
        try {
            // Lee todas las líneas del archivo
            List<String> lines = Files.readAllLines(Paths.get(rutaArchivo));
            String encabezado = lines.get(0); // Obtiene el encabezado

            // Filtra las líneas que no coinciden con el ID a eliminar
            List<String> filteredLines = lines.stream()
                    .skip(1) // Salta el encabezado
                    .filter(line -> Long.parseLong(line.split(",")[0]) != id)
                    .collect(Collectors.toList());

            writeFileContent(rutaArchivo, encabezado, filteredLines); // Escribe las líneas filtradas de vuelta al archivo
            // Escribe las líneas filtradas de vuelta al archivo
//            Files.write(Paths.get(rutaArchivo),
//                    (encabezado + System.lineSeparator() + String.join(System.lineSeparator(), filteredLines)).getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la información con ID: " + id, e); // Maneja la excepción con más contexto
        }
    }

    public void writeFileContent(String rutaArchivo, String encabezado, List<String> filteredLines) {
        try {
            StringBuilder content = new StringBuilder(encabezado);

            if (!filteredLines.isEmpty()) {
                content.append(System.lineSeparator())
                        .append(String.join(System.lineSeparator(), filteredLines));
            }

            Files.write(Paths.get(rutaArchivo), content.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en el archivo: " + rutaArchivo, e);
        }
    }

    public T findInfo(long id, String rutaArchivo){
        try {
            File file = new File(rutaArchivo);

            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String linea; //Leo el encabezado
            linea = reader.readLine(); //Salto encabezado

            while ((linea = reader.readLine()) != null) { //Leo todas las lineas de info hasta la ultima
                String[] datos = linea.split(",");

                if (Long.parseLong(datos[0]) == id){ //Si cumple condicion significa que la info buscada fue encontrada
                    reader.close();
                    return parseDatosToObject(datos);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    // Metodo para encontrar toda la información en el archivo
    public List<T> findAllInfo(String rutaArchivo) {
        List<T> info = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(rutaArchivo))) { // Usa try-with-resources para manejar recursos
            reader.readLine(); // Salta el encabezado
            info = reader.lines() // Procesa las líneas usando Streams
                    .map(line -> parseDatosToObject(line.split(","))) // Convierte cada línea en un objeto T
                    .collect(Collectors.toList()); // Colecciona los objetos en una lista
        } catch (IOException e) {
            throw new RuntimeException("Error al leer toda la información del archivo: " + rutaArchivo, e); // Maneja la excepción con más contexto
        }
        return info; // Devuelve la lista de objetos
    }

    // Metodo abstracto para convertir datos en un objeto T
    public abstract T parseDatosToObject(String[] datos);
}

package ar.edu.utn.frbb.tup.persistence.DAO;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseDao<T> {

    // Metodo que inicializa un archivo con encabezado
    public void inicializarArchivo(String encabezado, String rutaArchivo) {
        Path path = Paths.get(rutaArchivo);
        if (Files.notExists(path)) {
            try {
                Files.write(path, encabezado.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo", e);
            }
        }
    }

    // Metodo para guardar información en el archivo
    public void saveInfo(String info, String rutaArchivo) {
        try {
            Files.write(Paths.get(rutaArchivo), info.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo para eliminar información del archivo basado en un ID
    public void deleteInfo(long id, String rutaArchivo) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(rutaArchivo));
            String encabezado = lines.get(0);


            List<String> filteredLines = lines.stream()
                    .skip(1)
                    .filter(line -> Long.parseLong(line.split(",")[0]) != id)
                    .collect(Collectors.toList());

            writeFileContent(rutaArchivo, encabezado, filteredLines);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la información con ID: " + id, e);
        }
    }

    // Metodo para actualizar información en el archivo
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

    // Metodo para encontrar información en el archivo basado en un ID
    public T findInfo(long id, String rutaArchivo){
        try {
            File file = new File(rutaArchivo);

            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String linea;
            linea = reader.readLine();

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");

                if (Long.parseLong(datos[0]) == id){
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
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(rutaArchivo))) {
            reader.readLine();
            info = reader.lines()
                    .map(line -> parseDatosToObject(line.split(",")))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error al leer toda la información del archivo: " + rutaArchivo, e);
        }
        return info;
    }

    // Metodo abstracto para convertir datos en un objeto T
    public abstract T parseDatosToObject(String[] datos);
}
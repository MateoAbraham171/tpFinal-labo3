package ar.edu.utn.frbb.tup.persistence.DAO;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.*;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CuentaDao extends BaseDao<Cuenta> {
    private static final String RUTA_ARCHIVO = "src/main/java/ar/edu/utn/frbb/tup/persistence/data/cuentas.txt";

    public void inicializarCuentas(){
        String encabezado = "CBU, DNI titular, estado, saldo, fecha creacion, tipo de cuenta, tipo de moneda";
        inicializarArchivo(encabezado, RUTA_ARCHIVO);
    }

    public void saveCuenta(Cuenta cuenta){
        StringBuilder sb = new StringBuilder();
        sb.append(cuenta.getCBU()).append(",")
                .append(cuenta.getDniTitular()).append(",")
                .append(cuenta.getEstado()).append(",")
                .append(cuenta.getBalance()).append(",")
                .append(cuenta.getFechaCreacion()).append(",")
                .append(cuenta.getTipoCuenta()).append(",")
                .append(cuenta.getMoneda());

        saveInfo(sb.toString(), RUTA_ARCHIVO);
    }

    public void deleteCuenta(long CBU){
        deleteInfo(CBU, RUTA_ARCHIVO);
    }

    public Cuenta findCuenta(long CBU){
        return findInfo(CBU, RUTA_ARCHIVO);
    }

    public List<Cuenta> findAllCuentas() {
        return findAllInfo(RUTA_ARCHIVO);
    }

    public Cuenta findCuentaDelCliente(long cbu, long dni) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            // Saltamos la primera línea (encabezado)
            reader.readLine();

            return reader.lines()
                    .map(linea -> linea.split(","))
                    .filter(datos -> Long.parseLong(datos[0]) == cbu && Long.parseLong(datos[1]) == dni)
                    .findFirst()
                    .map(this::parseDatosToObjet)
                    .orElse(null);
        }
    }

    public List<Long> getRelacionesDni(long dni) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            // Saltamos la primera línea (encabezado)
            reader.readLine();

            return reader.lines()
                    .map(linea -> linea.split(","))
                    .filter(datos -> Long.parseLong(datos[1]) == dni)
                    .map(datos -> Long.parseLong(datos[0]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo de relaciones", e);
        }
    }

    public Set<Cuenta> findAllCuentasDelCliente(long dni) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            // Saltamos la primera línea (encabezado)
            reader.readLine();

            return reader.lines()
                    .map(linea -> linea.split(","))
                    .filter(datos -> Long.parseLong(datos[1]) == dni)
                    .map(this::parseDatosToObjet)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo de cuentas", e);
        }
    }

    public void updateCuenta(Cuenta cuentaActualizada) {
        List<String> cuentasActualizadas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String encabezado = reader.readLine(); // Leer el encabezado
            cuentasActualizadas.add(encabezado); // Agregar el encabezado al principio

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                long cbu = Long.parseLong(datos[0]);

                if (cbu == cuentaActualizada.getCBU()) {
                    // Actualizar la cuenta si el CBU coincide
                    StringBuilder sb = new StringBuilder();
                    sb.append(cuentaActualizada.getCBU()).append(",")
                            .append(cuentaActualizada.getDniTitular()).append(",")
                            .append(cuentaActualizada.getEstado()).append(",")
                            .append(cuentaActualizada.getBalance()).append(",")
                            .append(cuentaActualizada.getFechaCreacion()).append(",")
                            .append(cuentaActualizada.getTipoCuenta()).append(",")
                            .append(cuentaActualizada.getMoneda());
                    cuentasActualizadas.add(sb.toString());
                } else {
                    // Mantener la línea original si no es la cuenta a actualizar
                    cuentasActualizadas.add(linea);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo de cuentas", e);
        }

        // Escribir las cuentas actualizadas de vuelta al archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (String cuentaLinea : cuentasActualizadas) {
                writer.write(cuentaLinea);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error al escribir en el archivo de cuentas", e);
        }
    }


    //Funcion para parsear los datos leidos del archivo a un objeto tipo 'Cuenta'
    @Override
    public Cuenta parseDatosToObjet(String[] datos){
        Cuenta cuenta = new Cuenta();
        cuenta.setCBU(Long.parseLong(datos[0]));
        cuenta.setDniTitular(Long.parseLong(datos[1]));
        cuenta.setEstado(Boolean.parseBoolean(datos[2]));
        cuenta.setBalance(Double.parseDouble(datos[3]));
        cuenta.setFechaCreacion(LocalDate.parse(datos[4]));
        cuenta.setTipoCuenta(TipoCuenta.valueOf(datos[5]));
        cuenta.setMoneda(TipoMoneda.valueOf(datos[6]));
        return cuenta;
    }
}
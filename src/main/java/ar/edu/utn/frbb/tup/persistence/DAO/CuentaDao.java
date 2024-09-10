package ar.edu.utn.frbb.tup.persistence.DAO;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.*;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDate;
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
        String sb = "\n" + cuenta.getCBU() + "," +
                cuenta.getDniTitular() + "," +
                cuenta.getEstado() + "," +
                cuenta.getBalance() + "," +
                cuenta.getFechaCreacion() + "," +
                cuenta.getTipoCuenta() + "," +
                cuenta.getMoneda();

        saveInfo(sb, RUTA_ARCHIVO);
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

    public Cuenta findCuentaDelCliente(long cbu, long dni) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            reader.readLine();

            return reader.lines()
                    .map(linea -> linea.split(","))
                    .filter(datos -> Long.parseLong(datos[0]) == cbu && Long.parseLong(datos[1]) == dni)
                    .findFirst()
                    .map(this::parseDatosToObject)
                    .orElse(null);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo de cuentas", e);
        }
    }

    public List<Long> getCBUsVinculadosPorDni(long dni) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
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
            reader.readLine();

            return reader.lines()
                    .map(linea -> linea.split(","))
                    .filter(datos -> Long.parseLong(datos[1]) == dni)
                    .map(this::parseDatosToObject)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo de cuentas", e);
        }
    }

    public void updateCuenta(Cuenta cuentaActualizada) {
        deleteCuenta(cuentaActualizada.getCBU());
        saveCuenta(cuentaActualizada);
    }

    //Funcion para parsear los datos leidos del archivo a un objeto tipo 'Cuenta'
    @Override
    public Cuenta parseDatosToObject(String[] datos){
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
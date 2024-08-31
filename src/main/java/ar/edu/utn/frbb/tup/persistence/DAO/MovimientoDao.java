package ar.edu.utn.frbb.tup.persistence.DAO;

import ar.edu.utn.frbb.tup.model.Movimiento;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MovimientoDao extends BaseDao<Movimiento> {
    private final String RUTA_ARCHIVO = "src/main/java/ar/edu/utn/frbb/tup/persistence/data/movimientos.txt";

    public void inicializarMovimientos() {
        String encabezado = "CBU Origen, Fecha de Operacion, Hora de Operacion, Tipo de Operacion, Monto";
        inicializarArchivo(encabezado, RUTA_ARCHIVO);
    }

    public void saveMovimiento(String tipoOperacion, double monto, long cbu) {
        Movimiento mov = new Movimiento();
        mov.setCBU(cbu);
        mov.setFechaOperacion(LocalDate.now());
        mov.setHoraOperacion(LocalTime.now().withNano(0));
        mov.setTipoOperacion(tipoOperacion);
        mov.setMonto(monto);

        String infoAGuardar = String.format("%d,%s,%s,%s,%.2f",
                mov.getCBU(),
                mov.getFechaOperacion(),
                mov.getHoraOperacion(),
                mov.getTipoOperacion(),
                mov.getMonto());

        saveInfo(infoAGuardar, RUTA_ARCHIVO);
    }

    public void deleteMovimiento(long cbu) {
        deleteInfo(cbu, RUTA_ARCHIVO);
    }

    public List<Movimiento> findMovimientos(long CBU) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            // Saltamos la primera línea (encabezado)
            reader.readLine();

            return reader.lines()
                    .map(linea -> linea.split(","))
                    .filter(datos -> Long.parseLong(datos[0]) == CBU)
                    .map(this::parseDatosToObjet)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer el archivo de movimientos", e);
        }
    }

    @Override
    public Movimiento parseDatosToObjet(String[] datos){
        Movimiento movimiento = new Movimiento();

        movimiento.setCBU(Long.parseLong(datos[0]));
        movimiento.setFechaOperacion(LocalDate.parse(datos[1]));
        movimiento.setHoraOperacion(LocalTime.parse(datos[2]));
        movimiento.setTipoOperacion(datos[3]);
        movimiento.setMonto(Double.parseDouble(datos[4]));

        return movimiento;
    }
}

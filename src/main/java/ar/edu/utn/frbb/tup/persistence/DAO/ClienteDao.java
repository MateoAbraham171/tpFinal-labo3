package ar.edu.utn.frbb.tup.persistence.DAO;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;

@Repository
public class ClienteDao extends BaseDao<Cliente> {
    private static final String RUTA_ARCHIVO = "src/main/java/ar/edu/utn/frbb/tup/persistence/data/cliente.txt";

    private final CuentaDao cuentaDao;

    public ClienteDao(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }

    public void inicializarClientes() {
        String encabezado = "DNI, Nombre, Apellido, Fecha nacimiento, Banco, Tipo Persona, Fecha alta, Mail, Direccion";
        inicializarArchivo(encabezado, RUTA_ARCHIVO);
    }

    public void saveCliente(Cliente cliente) {
        StringBuilder sb = new StringBuilder();
        sb.append(cliente.getDni()).append(",")
                .append(cliente.getNombre()).append(",")
                .append(cliente.getApellido()).append(",")
                .append(cliente.getFechaNacimiento()).append(",")
                .append(cliente.getBanco()).append(",")
                .append(cliente.getTipoPersona()).append(",")
                .append(cliente.getFechaAlta()).append(",")
                .append(cliente.getMail()).append(",")
                .append(cliente.getDireccion());

        saveInfo(sb.toString(), RUTA_ARCHIVO);
    }

    public void deleteCliente(Long dni) {
        deleteInfo(dni, RUTA_ARCHIVO);
    }

    public Cliente findCliente(Long dni) {
        Cliente cliente = findInfo(dni, RUTA_ARCHIVO);

        if (cliente == null) {
            return null;
        }

        Set<Cuenta> cuentas = cuentaDao.findAllCuentasDelCliente(dni);

        if (!cuentas.isEmpty()) {
            cliente.setCuentas(cuentas);
        }

        return cliente;
    }

    public List<Cliente> findAllClientes() {
        List<Cuenta> cuentas = cuentaDao.findAllCuentas();
        List<Cliente> clientes = findAllInfo(RUTA_ARCHIVO);

        if (!cuentas.isEmpty()) {
            Map<Long, Set<Cuenta>> cuentasPorCliente = cuentas.stream()
                    .collect(Collectors.groupingBy(Cuenta::getDniTitular, Collectors.toSet()));

            clientes.forEach(cliente ->
                    cliente.setCuentas(cuentasPorCliente.getOrDefault(cliente.getDni(), new HashSet<>()))
            );
        }
        return clientes;
    }

    @Override
    public Cliente parseDatosToObjet(String[] datos) {
        Cliente cliente = new Cliente();

        cliente.setDni(Long.parseLong(datos[0]));
        cliente.setNombre(datos[1]);
        cliente.setApellido(datos[2]);
        cliente.setFechaNacimiento(LocalDate.parse(datos[3]));
        cliente.setBanco(datos[4]);
        cliente.setTipoPersona(TipoPersona.valueOf(datos[5]));
        cliente.setFechaAlta(LocalDate.parse(datos[6]));
        cliente.setMail(datos[7]);
        cliente.setDireccion(datos[8]);

        return cliente;
    }
}
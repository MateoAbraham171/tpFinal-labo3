package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteMenorException;
import ar.edu.utn.frbb.tup.exception.ClientesException.FechaNacimientoInvalidaException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.time.LocalDate;

@Service
public class CrearCliente {
    private final ClienteDao clienteDao;
    private static final LocalDate FECHA_NACIMIENTO_MINIMA = LocalDate.of(1900, 1, 1);

    public CrearCliente(ClienteDao clienteDao) { this.clienteDao = clienteDao; }

    public Cliente CrearCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorException, FechaNacimientoInvalidaException {
        Cliente cliente = new Cliente(clienteDto);

        Cliente clienteEncontrado = clienteDao.findCliente(cliente.getDni());
        if (clienteEncontrado != null) {
            throw new ClienteAlreadyExistsException("Error: Ya existe un cliente con ese DNI");
        }

        validarFechaNacimiento(cliente.getFechaNacimiento());
        esMayor(cliente);

        clienteDao.saveCliente(cliente);

        return cliente;
    }

    private static void validarFechaNacimiento(LocalDate fechaNacimiento) throws FechaNacimientoInvalidaException {
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new FechaNacimientoInvalidaException("La fecha de nacimiento no puede ser futura.");
        }
        if (fechaNacimiento.isBefore(FECHA_NACIMIENTO_MINIMA)) {
            throw new FechaNacimientoInvalidaException("La fecha de nacimiento debe ser a partir del " + FECHA_NACIMIENTO_MINIMA + ".");
        }
    }

    private void esMayor(Cliente cliente) throws ClienteMenorException {
        Period periodo = Period.between(cliente.getFechaNacimiento(), LocalDate.now());

        if (periodo.getYears() < 18) {
            throw new ClienteMenorException("Solo se permiten clientes mayores de 18 años");
        }
    }
}

package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesException.*;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final CrearCliente crearCliente;
    private final EliminarCliente eliminarCliente;
    private final ModificarCliente modificarCliente;
    private final MostrarTodosClientes mostrarTodosClientes;
    private final MostrarCliente mostrarCliente;
    private final ClienteDao clienteDao;

    public ClienteService(CrearCliente crearCliente,
                          EliminarCliente eliminarCliente,
                          ModificarCliente modificarCliente,
                          MostrarCliente mostrarCliente,
                          MostrarTodosClientes mostrarTodosClientes,
                          ClienteDao clienteDao) {
        this.crearCliente = crearCliente;
        this.eliminarCliente = eliminarCliente;
        this.modificarCliente = modificarCliente;
        this.mostrarCliente = mostrarCliente;
        this.mostrarTodosClientes = mostrarTodosClientes;
        this.clienteDao = clienteDao;
    }

    public void inicializarClientes() {
        clienteDao.inicializarClientes();
    }

    public Cliente crearCliente(ClienteDto clienteDto) throws ClienteMenorException, FechaNacimientoInvalidaException, ClienteAlreadyExistsException {
        return crearCliente.CrearCliente(clienteDto);
    }

    public Cliente eliminarCliente(long dni) throws ClienteNoEncontradoException {
        return eliminarCliente.eliminarCliente(dni);
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws ClienteMenorException, ClienteNoEncontradoException {
        return modificarCliente.modificarCliente(clienteDto);
    }

    public Cliente mostrarCliente(long dni) throws ClienteNoEncontradoException {
        return mostrarCliente.mostrarCliente(dni);
    }

    public List<Cliente> mostrarTodosClientes() throws NoExistenClientesException {
        return mostrarTodosClientes.mostrarTodosClientes();
    }
}

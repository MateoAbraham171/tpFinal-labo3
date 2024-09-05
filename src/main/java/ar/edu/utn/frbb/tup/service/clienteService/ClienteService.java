package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    private final CreadorDeCliente creadorDeCliente;
    private final EliminadorDeCliente eliminadorDeCliente;
    private final ModificadorDeCliente modificadorDeCliente;
    private final MostradorDeTodosClientes mostradorDeTodosClientes;
    private final MostradosDeCliente mostradosDeCliente;
    private final ClienteDao clienteDao;

    public ClienteService(CreadorDeCliente creadorDeCliente,
                          EliminadorDeCliente eliminadorDeCliente,
                          ModificadorDeCliente modificadorDeCliente,
                          MostradosDeCliente mostradosDeCliente,
                          MostradorDeTodosClientes mostradorDeTodosClientes,
                          ClienteDao clienteDao) {
        this.creadorDeCliente = creadorDeCliente;
        this.eliminadorDeCliente = eliminadorDeCliente;
        this.modificadorDeCliente = modificadorDeCliente;
        this.mostradosDeCliente = mostradosDeCliente;
        this.mostradorDeTodosClientes = mostradorDeTodosClientes;
        this.clienteDao = clienteDao;
    }

    public void inicializarClientes() {
        clienteDao.inicializarClientes();
    }

    public Cliente crearCliente(ClienteDto clienteDto) throws ConflictException {
        return creadorDeCliente.crearCliente(clienteDto);
    }

    public Cliente eliminarCliente(long dni) throws NotFoundException {
        return eliminadorDeCliente.eliminarCliente(dni);
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws NotFoundException {
        return modificadorDeCliente.modificarCliente(clienteDto);
    }

    public Cliente mostrarCliente(long dni) throws NotFoundException {
        return mostradosDeCliente.mostrarCliente(dni);
    }

    public List<Cliente> mostrarTodosClientes() throws NotFoundException {
        return mostradorDeTodosClientes.mostrarTodosClientes();
    }
}

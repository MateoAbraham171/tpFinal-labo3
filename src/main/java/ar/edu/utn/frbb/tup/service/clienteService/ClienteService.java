package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
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
    private final MostradorDeCliente mostradorDeCliente;
    private final ClienteDao clienteDao;

    public ClienteService(CreadorDeCliente creadorDeCliente,
                          EliminadorDeCliente eliminadorDeCliente,
                          ModificadorDeCliente modificadorDeCliente,
                          MostradorDeCliente mostradorDeCliente,
                          MostradorDeTodosClientes mostradorDeTodosClientes,
                          ClienteDao clienteDao) {
        this.creadorDeCliente = creadorDeCliente;
        this.eliminadorDeCliente = eliminadorDeCliente;
        this.modificadorDeCliente = modificadorDeCliente;
        this.mostradorDeCliente = mostradorDeCliente;
        this.mostradorDeTodosClientes = mostradorDeTodosClientes;
        this.clienteDao = clienteDao;
    }

    public void inicializarClientes() {
        clienteDao.inicializarClientes();
    }

    public Cliente crearCliente(ClienteDto clienteDto) throws ConflictException, BadRequestException {
        return creadorDeCliente.crearCliente(clienteDto);
    }

    public Cliente eliminarCliente(long dni) throws NotFoundException {
        return eliminadorDeCliente.eliminarCliente(dni);
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws NotFoundException, BadRequestException {
        return modificadorDeCliente.modificarCliente(clienteDto);
    }

    public Cliente mostrarCliente(long dni) throws NotFoundException {
        return mostradorDeCliente.mostrarCliente(dni);
    }

    public List<Cliente> mostrarTodosClientes() throws NotFoundException {
        return mostradorDeTodosClientes.mostrarTodosClientes();
    }
}

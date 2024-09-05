package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import org.springframework.stereotype.Service;

@Service
public class CreadorDeCliente {
    private final ClienteDao clienteDao;

    public CreadorDeCliente(ClienteDao clienteDao) { this.clienteDao = clienteDao; }

    public Cliente crearCliente(ClienteDto clienteDto) throws ConflictException {
        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.findCliente(cliente.getDni()) != null)
            throw new ClienteAlreadyExistsException();

        clienteDao.saveCliente(cliente);

        return cliente;
    }
}

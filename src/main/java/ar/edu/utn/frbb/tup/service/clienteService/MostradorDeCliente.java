package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.model.Cliente;
import org.springframework.stereotype.Service;

@Service
public class MostradorDeCliente {
    private final ClienteDao clienteDao;

    public MostradorDeCliente(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente mostrarCliente(long dni) throws NotFoundException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        return cliente;
    }
}

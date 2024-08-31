package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.model.Cliente;
import org.springframework.stereotype.Service;

@Service
public class MostrarCliente {
    private final ClienteDao clienteDao;

    public MostrarCliente(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente mostrarCliente(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        return cliente;
    }
}

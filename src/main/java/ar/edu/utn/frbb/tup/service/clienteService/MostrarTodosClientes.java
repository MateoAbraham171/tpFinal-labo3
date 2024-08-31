package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesException.NoExistenClientesException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MostrarTodosClientes {
    private final ClienteDao clienteDao;

    public MostrarTodosClientes(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public List<Cliente> mostrarTodosClientes() throws NoExistenClientesException {
        List<Cliente> clientes = clienteDao.findAllClientes();

        if (clientes.isEmpty()) {
            throw new NoExistenClientesException();
        }

        return clientes;
    }
}

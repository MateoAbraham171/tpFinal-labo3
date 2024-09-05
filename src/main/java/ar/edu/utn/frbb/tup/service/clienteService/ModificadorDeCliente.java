package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import org.springframework.stereotype.Service;

@Service
public class ModificadorDeCliente {
    private final ClienteDao clienteDao;

    public ModificadorDeCliente(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws NotFoundException {
        Cliente clienteModificado = new Cliente(clienteDto);
        Cliente cliente = clienteDao.findCliente(clienteModificado.getDni());

        if (cliente == null)
            throw new ClienteNoEncontradoException(clienteModificado.getDni());

        clienteDao.deleteCliente(cliente.getDni());
        clienteDao.saveCliente(clienteModificado);

        return clienteModificado;
    }
}

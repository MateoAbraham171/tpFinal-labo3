package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteMenorException;
import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ModificarCliente {
    private final ClienteDao clienteDao;

    public ModificarCliente(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws ClienteMenorException, ClienteNoEncontradoException {
        Cliente clienteModificado = new Cliente(clienteDto);

        if (clienteModificado.getFechaNacimiento() != null) esMayor(clienteModificado);

        Cliente cliente = clienteDao.findCliente(clienteModificado.getDni());

        if (cliente == null) {
            throw new ClienteNoEncontradoException(clienteModificado.getDni());
        }

        clienteDao.deleteCliente(clienteModificado.getDni());

        actualizarCliente(cliente, clienteModificado);

        clienteDao.saveCliente(cliente);

        return cliente;
    }

    private void actualizarCliente(Cliente cliente, Cliente clienteModificado) {
        //Solo se modifica lo que el usuario escribio, si el usuario no especifica el nombre no se va a modificar
        if (clienteModificado.getNombre() != null) cliente.setNombre(clienteModificado.getNombre());
        if (clienteModificado.getApellido() != null) cliente.setApellido(clienteModificado.getApellido());
        if (clienteModificado.getDireccion() != null) cliente.setDireccion(clienteModificado.getDireccion());
        if (clienteModificado.getTipoPersona() != null) cliente.setTipoPersona(clienteModificado.getTipoPersona());
        if (clienteModificado.getBanco() != null) cliente.setBanco(clienteModificado.getBanco());
        if (clienteModificado.getMail() != null) cliente.setMail(clienteModificado.getMail());
        if (clienteModificado.getFechaNacimiento() != null) cliente.setFechaNacimiento(clienteModificado.getFechaNacimiento());
    }

    private void esMayor(Cliente cliente) throws ClienteMenorException {
        Period periodo = Period.between(cliente.getFechaNacimiento(), LocalDate.now());

        if (periodo.getYears() < 18){
            throw new ClienteMenorException("Tiene que ser mayor de edad para ser cliente");
        }
    }
}

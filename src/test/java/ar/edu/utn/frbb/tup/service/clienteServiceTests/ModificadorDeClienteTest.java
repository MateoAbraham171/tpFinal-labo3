package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.service.clienteService.ModificadorDeCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModificadorDeClienteTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private ClienteDto clienteDto;

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks private ModificadorDeCliente modificadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = generadorDeObjetosParaTests.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testModificarClienteSuccess() throws NotFoundException, BadRequestException {
        ClienteDto clienteConModificaciones = generadorDeObjetosParaTests.getClienteDto("Gino", 85876925L);
        Cliente cliente = new Cliente(clienteDto);
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(cliente);
        Cliente clienteModificado = modificadorDeCliente.modificarCliente(clienteConModificaciones);
        assertNotNull(clienteModificado);
        assertNotEquals(cliente.getNombre(), clienteModificado.getNombre());
        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).deleteCliente(cliente.getDni());
        verify(clienteDao, times(1)).saveCliente(clienteModificado);
    }

    @Test
    public void testModificadorDeClienteNotFound() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);
        assertThrows(ClienteNoEncontradoException.class, () -> modificadorDeCliente.modificarCliente(clienteDto));
        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(0)).deleteCliente(clienteDto.getDni());
    }
}
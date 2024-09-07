package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.AdminTest;
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
    private final AdminTest adminTest = new AdminTest();
    private ClienteDto clienteDto;

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks private ModificadorDeCliente modificadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = adminTest.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testModificadorDeClienteSuccess() throws NotFoundException {
        ClienteDto clienteConModificaciones = adminTest.getClienteDto("Peperino", 85876925L);
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(cliente);

        Cliente clienteModificado = modificadorDeCliente.modificarCliente(clienteConModificaciones);

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).deleteCliente(cliente.getDni());
        verify(clienteDao, times(1)).saveCliente(clienteModificado);

        assertNotNull(clienteModificado);
        assertNotEquals(cliente.getNombre(), clienteModificado.getNombre());
    }

    @Test
    public void testModificadorDeClienteNotFound() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> modificadorDeCliente.modificarCliente(clienteDto));

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(0)).deleteCliente(clienteDto.getDni());
    }
}

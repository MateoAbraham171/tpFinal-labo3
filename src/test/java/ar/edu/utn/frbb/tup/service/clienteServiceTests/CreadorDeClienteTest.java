package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.AdminTest;
import ar.edu.utn.frbb.tup.service.clienteService.CreadorDeCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreadorDeClienteTest {
    private final AdminTest adminTest = new AdminTest();
    private ClienteDto clienteDto;

    @Mock private ClienteDao clienteDao;

    @InjectMocks private CreadorDeCliente creadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = adminTest.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testCrearClienteSuccess() throws ConflictException {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);

        Cliente clienteResultado = creadorDeCliente.crearCliente(clienteDto);

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).saveCliente(clienteResultado);

        assertEquals(clienteDto.getDni(), clienteResultado.getDni());
        assertNotNull(clienteResultado);
    }

    @Test
    public void testClienteAlreadyExistsException() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> creadorDeCliente.crearCliente(clienteDto));

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(0)).saveCliente(any(Cliente.class));
    }
}
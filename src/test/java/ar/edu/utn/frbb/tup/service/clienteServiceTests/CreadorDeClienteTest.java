package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
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
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private ClienteDto clienteDto;

    @Mock private ClienteDao clienteDao;

    @InjectMocks private CreadorDeCliente creadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = generadorDeObjetosParaTests.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testCrearClienteSuccess() throws ConflictException, BadRequestException {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);
        Cliente clienteCreado = creadorDeCliente.crearCliente(clienteDto);
        assertNotNull(clienteCreado);
        assertEquals(clienteDto.getDni(), clienteCreado.getDni());
        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).saveCliente(clienteCreado);

    }

    @Test
    public void testClienteAlreadyExistsException() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente());
        assertThrows(ClienteAlreadyExistsException.class, () -> creadorDeCliente.crearCliente(clienteDto));
        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(0)).saveCliente(any(Cliente.class));
    }
}
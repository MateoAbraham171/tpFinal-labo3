package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.AdminTest;
import ar.edu.utn.frbb.tup.service.clienteService.MostradorDeCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MostradorDeClienteTest {
    private ClienteDto clienteDto;
    private final AdminTest adminTest = new AdminTest();

    @Mock private ClienteDao clienteDao;

    @InjectMocks private MostradorDeCliente mostradorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = adminTest.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testMostradorDeClienteSuccess() throws NotFoundException {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));

        Cliente clienteEncontrado = mostradorDeCliente.mostrarCliente(clienteDto.getDni());

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());

        assertEquals(clienteDto.getDni(), clienteEncontrado.getDni());
        assertNotNull(clienteEncontrado);
    }

    @Test
    public void testMostradorDeClienteNotFound() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> mostradorDeCliente.mostrarCliente(clienteDto.getDni()));

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
    }
}

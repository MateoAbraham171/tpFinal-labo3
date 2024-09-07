package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.NoExistenClientesException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.AdminTest;
import ar.edu.utn.frbb.tup.service.clienteService.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    private ClienteDto clienteDto;
    private final AdminTest adminTest = new AdminTest();

    @Mock private CreadorDeCliente creadorDeCliente;
    @Mock private EliminadorDeCliente eliminadorDeCliente;
    @Mock private ModificadorDeCliente modificadorDeCliente;
    @Mock private MostradorDeCliente mostradorDeCliente;
    @Mock private MostradorDeTodosClientes mostradorDeTodosClientes;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        clienteDto = adminTest.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testCrearClienteSuccess() throws ConflictException {
        Cliente cliente = new Cliente(clienteDto);
        when(creadorDeCliente.crearCliente(clienteDto)).thenReturn(cliente);

        Cliente clienteCreado = clienteService.crearCliente(clienteDto);

        assertNotNull(clienteCreado);
        assertEquals(cliente, clienteCreado);
        verify(creadorDeCliente).crearCliente(clienteDto);
    }

    //en este tengo mis dudas, no se si esta del todo bien
    @Test
    public void testCrearClienteFail() throws ConflictException {
        when(creadorDeCliente.crearCliente(clienteDto)).thenThrow(new ClienteAlreadyExistsException());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.crearCliente(clienteDto));

        verify(creadorDeCliente, times(1)).crearCliente(clienteDto);
    }

    @Test
    public void testEliminarClienteSuccess() throws NotFoundException {
        when(eliminadorDeCliente.eliminarCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));

        Cliente clienteEliminado = clienteService.eliminarCliente(clienteDto.getDni());

        verify(eliminadorDeCliente, times(1)).eliminarCliente(clienteDto.getDni());
        assertNotNull(clienteEliminado);
        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());
    }

    @Test
    public void testEliminarClienteFail() throws NotFoundException {
        when(eliminadorDeCliente.eliminarCliente(clienteDto.getDni())).thenThrow(new ClienteNoEncontradoException(clienteDto.getDni()));

        assertThrows(NotFoundException.class, () -> clienteService.eliminarCliente(clienteDto.getDni()));

        verify(eliminadorDeCliente, times(1)).eliminarCliente(clienteDto.getDni());
    }

    @Test
    public void testModificarCliente() throws NotFoundException {
        Cliente cliente = new Cliente(clienteDto);
        when(modificadorDeCliente.modificarCliente(clienteDto)).thenReturn(cliente);

        Cliente clienteModificado = clienteService.modificarCliente(clienteDto);

        verify(modificadorDeCliente, times(1)).modificarCliente(clienteDto);
        assertNotNull(clienteModificado);
        assertEquals(clienteDto.getDni(), clienteModificado.getDni());
    }

    @Test
    public void testModificarClienteFail() throws NotFoundException {
        when(modificadorDeCliente.modificarCliente(clienteDto)).thenThrow(new ClienteNoEncontradoException(clienteDto.getDni()));

        assertThrows(ClienteNoEncontradoException.class, () -> clienteService.modificarCliente(clienteDto));

        verify(modificadorDeCliente, times(1)).modificarCliente(clienteDto);
    }

    @Test
    public void testMostrarClienteSucces() throws NotFoundException {
        Cliente cliente = new Cliente(clienteDto);
        when(mostradorDeCliente.mostrarCliente(clienteDto.getDni())).thenReturn(cliente);

        Cliente clienteMostrado = clienteService.mostrarCliente(clienteDto.getDni());

        verify(mostradorDeCliente, times(1)).mostrarCliente(clienteDto.getDni());
        assertNotNull(clienteMostrado);
        assertEquals(clienteDto.getDni(), clienteMostrado.getDni());
    }

    @Test
    public void testMostrarClienteFail() throws NotFoundException {
        when(mostradorDeCliente.mostrarCliente(clienteDto.getDni())).thenThrow(new ClienteNoEncontradoException(clienteDto.getDni()));

        assertThrows(ClienteNoEncontradoException.class, () -> clienteService.mostrarCliente(clienteDto.getDni()));

        verify(mostradorDeCliente, times(1)).mostrarCliente(clienteDto.getDni());
    }

    @Test
    public void testMostrarTodosClientesSuccess() throws NotFoundException {
        when(mostradorDeTodosClientes.mostrarTodosClientes()).thenReturn(adminTest.getListaDeClientes());

        assertNotNull(clienteService.mostrarTodosClientes());
        verify(mostradorDeTodosClientes, times(1)).mostrarTodosClientes();
    }

    @Test
    public void testMostrarTodosClientesFail() throws NotFoundException {
        when(mostradorDeTodosClientes.mostrarTodosClientes()).thenThrow(new NoExistenClientesException());

        assertThrows(NoExistenClientesException.class, () -> clienteService.mostrarTodosClientes());

        verify(mostradorDeTodosClientes, times(1)).mostrarTodosClientes();
    }
}

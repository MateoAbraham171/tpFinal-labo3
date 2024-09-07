package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.NoExistenClientesException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.service.clienteService.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    private ClienteDto clienteDto;
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();

    @Mock private CreadorDeCliente creadorDeCliente;
    @Mock private EliminadorDeCliente eliminadorDeCliente;
    @Mock private ModificadorDeCliente modificadorDeCliente;
    @Mock private MostradorDeCliente mostradorDeCliente;
    @Mock private MostradorDeTodosClientes mostradorDeTodosClientes;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        clienteDto = generadorDeObjetosParaTests.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testCrearClienteServiceSuccess() throws ConflictException {
        Cliente cliente = new Cliente(clienteDto);
        when(creadorDeCliente.crearCliente(clienteDto)).thenReturn(cliente);

        Cliente clienteCreado = clienteService.crearCliente(clienteDto);

        assertNotNull(clienteCreado);
        assertEquals(cliente, clienteCreado);
        verify(creadorDeCliente, times(1)).crearCliente(clienteDto);
    }

    @Test
    public void testCrearClienteServiceFail() throws ConflictException {
        when(creadorDeCliente.crearCliente(clienteDto)).thenThrow(new ClienteAlreadyExistsException());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.crearCliente(clienteDto));

        verify(creadorDeCliente, times(1)).crearCliente(clienteDto);
    }

    @Test
    public void testEliminarClienteServiceSuccess() throws NotFoundException {
        when(eliminadorDeCliente.eliminarCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));

        Cliente clienteEliminado = clienteService.eliminarCliente(clienteDto.getDni());

        assertNotNull(clienteEliminado);
        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());

        verify(eliminadorDeCliente, times(1)).eliminarCliente(clienteDto.getDni());
    }

    @Test
    public void testEliminarClienteServiceFail() throws NotFoundException {
        when(eliminadorDeCliente.eliminarCliente(clienteDto.getDni())).thenThrow(new ClienteNoEncontradoException(clienteDto.getDni()));

        assertThrows(NotFoundException.class, () -> clienteService.eliminarCliente(clienteDto.getDni()));

        verify(eliminadorDeCliente, times(1)).eliminarCliente(clienteDto.getDni());
    }

    @Test
    public void testModificarClienteServiceSuccess() throws NotFoundException {
        when(modificadorDeCliente.modificarCliente(clienteDto)).thenReturn(new Cliente(clienteDto));

        Cliente clienteModificado = clienteService.modificarCliente(clienteDto);

        assertNotNull(clienteModificado);
        assertEquals(clienteDto.getDni(), clienteModificado.getDni());

        verify(modificadorDeCliente, times(1)).modificarCliente(clienteDto);
    }

    @Test
    public void testModificarClienteServiceFail() throws NotFoundException {
        when(modificadorDeCliente.modificarCliente(clienteDto)).thenThrow(new ClienteNoEncontradoException(clienteDto.getDni()));

        assertThrows(ClienteNoEncontradoException.class, () -> clienteService.modificarCliente(clienteDto));

        verify(modificadorDeCliente, times(1)).modificarCliente(clienteDto);
    }

    @Test
    public void testMostrarClienteServiceSuccess() throws NotFoundException {
        when(mostradorDeCliente.mostrarCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));

        Cliente clienteMostrado = clienteService.mostrarCliente(clienteDto.getDni());

        assertNotNull(clienteMostrado);
        assertEquals(clienteDto.getDni(), clienteMostrado.getDni());

        verify(mostradorDeCliente, times(1)).mostrarCliente(clienteDto.getDni());
    }

    @Test
    public void testMostrarClienteServiceFail() throws NotFoundException {
        when(mostradorDeCliente.mostrarCliente(clienteDto.getDni())).thenThrow(new ClienteNoEncontradoException(clienteDto.getDni()));

        assertThrows(ClienteNoEncontradoException.class, () -> clienteService.mostrarCliente(clienteDto.getDni()));

        verify(mostradorDeCliente, times(1)).mostrarCliente(clienteDto.getDni());
    }

    @Test
    public void testMostrarTodosClientesServiceSuccess() throws NotFoundException {
        List<Cliente> listaGenerada = generadorDeObjetosParaTests.getListaDeClientes();
        when(mostradorDeTodosClientes.mostrarTodosClientes()).thenReturn(listaGenerada);

        List<Cliente> listaClientesObtenida = clienteService.mostrarTodosClientes();

        assertNotNull(listaClientesObtenida);
        assertEquals(listaClientesObtenida, listaGenerada);

        verify(mostradorDeTodosClientes, times(1)).mostrarTodosClientes();
    }

    @Test
    public void testMostrarTodosClientesServiceFail() throws NotFoundException {
        when(mostradorDeTodosClientes.mostrarTodosClientes()).thenThrow(new NoExistenClientesException());

        assertThrows(NoExistenClientesException.class, () -> clienteService.mostrarTodosClientes());

        verify(mostradorDeTodosClientes, times(1)).mostrarTodosClientes();
    }
}

package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.NoExistenClientesException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.service.AdminTest;
import ar.edu.utn.frbb.tup.service.clienteService.MostradorDeTodosClientes;
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
public class MostradorDeTodosClientesTest {
    private final AdminTest adminTest = new AdminTest();

    @Mock private ClienteDao clienteDao;

    @InjectMocks private MostradorDeTodosClientes mostradorDeTodosClientes;

    //este setup puede quedar vacio???
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testMostradorDeTodosLosClientesSuccess() throws NotFoundException {
        List<Cliente> listaDeClientes = adminTest.getListaDeClientes();

        when(clienteDao.findAllClientes()).thenReturn(listaDeClientes);
        List<Cliente> clientesParaMostrar = mostradorDeTodosClientes.mostrarTodosClientes();

        verify(clienteDao, times(1)).findAllClientes();

        assertNotNull(clientesParaMostrar);
        assertEquals(listaDeClientes, clientesParaMostrar);
    }

    @Test
    public void testNoHayClientesParaMostrar() {
        when(clienteDao.findAllClientes()).thenReturn(adminTest.getListaDeClientesVacia());

        assertThrows(NoExistenClientesException.class , () -> mostradorDeTodosClientes.mostrarTodosClientes());

        verify(clienteDao, times(1)).findAllClientes();
    }
}

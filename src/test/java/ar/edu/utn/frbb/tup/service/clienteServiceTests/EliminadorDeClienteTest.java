package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.AdminTest;
import ar.edu.utn.frbb.tup.service.clienteService.EliminadorDeCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EliminadorDeClienteTest {
    private ClienteDto clienteDto;
    private final AdminTest adminTest = new AdminTest();

    @Mock private ClienteDao clienteDao;
    @Mock private CuentaDao cuentaDao;
    @Mock private MovimientoDao movimientoDao;

    @InjectMocks private EliminadorDeCliente eliminadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = adminTest.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testEliminarClienteSuccess() throws NotFoundException {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));

        Cliente clienteEliminado = eliminadorDeCliente.eliminarCliente(clienteDto.getDni());

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).deleteCliente(clienteDto.getDni());

        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());
        assertNotNull(clienteEliminado);
    }
}

package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.AdminTest;
import ar.edu.utn.frbb.tup.service.clienteService.EliminadorDeCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EliminadorDeClienteTest {
    private ClienteDto clienteDto;
    private final AdminTest adminTest = new AdminTest();

    @Mock private ClienteDao clienteDao;
    @Mock private CuentaDao cuentaDao;

    @InjectMocks private EliminadorDeCliente eliminadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = adminTest.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testEliminadorDeClienteSinCuentasSuccess() throws NotFoundException {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));

        Cliente clienteEliminado = eliminadorDeCliente.eliminarCliente(clienteDto.getDni());

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).deleteCliente(clienteDto.getDni());

        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());
        assertNotNull(clienteEliminado);
    }

    //esta bien testear tanto en un solo test???
    @Test
    public void testEliminadorDeClienteConCuentasSuccess() throws NotFoundException {
        Cuenta cuenta = adminTest.getCuenta(clienteDto.getDni(), TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS);
        List<Long> clienteCBUs = new ArrayList<>();

        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));
        when(cuentaDao.getCBUsVinculadosPorDni(clienteDto.getDni())).thenReturn(clienteCBUs);

        clienteCBUs.add(cuenta.getCBU());
        when(cuentaDao.findCuentaDelCliente(cuenta.getCBU(), clienteDto.getDni())).thenReturn(cuenta);

        Cliente clienteEliminado = eliminadorDeCliente.eliminarCliente(clienteDto.getDni());
        assertNotNull(clienteEliminado);
        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());

        //el metodo findCliente se llama dos veces, al verificar que un cliente no existe y
        //al momento de borrar una cuenta
        verify(clienteDao, times(2)).findCliente(clienteDto.getDni());
        verify(cuentaDao, times(1)).getCBUsVinculadosPorDni(clienteDto.getDni());
        verify(clienteDao, times(1)).deleteCliente(clienteDto.getDni());
    }

    @Test
    public void testEliminadorDeClienteNotFound() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> eliminadorDeCliente.eliminarCliente(clienteDto.getDni()));

        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(0)).deleteCliente(clienteDto.getDni());
    }
}

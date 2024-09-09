package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
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
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();

    @Mock private ClienteDao clienteDao;
    @Mock private CuentaDao cuentaDao;

    @InjectMocks private EliminadorDeCliente eliminadorDeCliente;

    @BeforeEach
    public void setUp() {
        clienteDto = generadorDeObjetosParaTests.getClienteDto("Mateo", 85876925L);
    }

    @Test
    public void testEliminarClienteSinCuentasSuccess() throws NotFoundException, BadRequestException {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));
        Cliente clienteEliminado = eliminadorDeCliente.eliminarCliente(clienteDto.getDni());
        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());
        assertNotNull(clienteEliminado);
        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(1)).deleteCliente(clienteDto.getDni());
    }

    @Test
    public void testEliminarClienteConCuentasSuccess() throws NotFoundException, BadRequestException {
        Cuenta cuenta1 = generadorDeObjetosParaTests.getCuenta(clienteDto.getDni(), TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS);
        Cuenta cuenta2 = generadorDeObjetosParaTests.getCuenta(clienteDto.getDni(), TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES).setCBU(234567);
        List<Long> clienteCBUs = new ArrayList<>();
        clienteCBUs.add(cuenta1.getCBU());
        clienteCBUs.add(cuenta2.getCBU());
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(new Cliente(clienteDto));
        when(cuentaDao.getCBUsVinculadosPorDni(clienteDto.getDni())).thenReturn(clienteCBUs);
        when(cuentaDao.findCuentaDelCliente(cuenta1.getCBU(), clienteDto.getDni())).thenReturn(cuenta1);
        when(cuentaDao.findCuentaDelCliente(cuenta2.getCBU(), clienteDto.getDni())).thenReturn(cuenta2);
        Cliente clienteEliminado = eliminadorDeCliente.eliminarCliente(clienteDto.getDni());
        assertNotNull(clienteEliminado);
        assertEquals(clienteDto.getDni(), clienteEliminado.getDni());
        verify(clienteDao, times(3)).findCliente(clienteDto.getDni());
        verify(cuentaDao, times(1)).getCBUsVinculadosPorDni(clienteDto.getDni());
        verify(cuentaDao, times(1)).deleteCuenta(cuenta1.getCBU());
        verify(cuentaDao, times(1)).deleteCuenta(cuenta2.getCBU());
        verify(clienteDao, times(1)).deleteCliente(clienteDto.getDni());
    }

    @Test
    public void testEliminarClienteNotFound() {
        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null);
        assertThrows(ClienteNoEncontradoException.class, () -> eliminadorDeCliente.eliminarCliente(clienteDto.getDni()));
        verify(clienteDao, times(1)).findCliente(clienteDto.getDni());
        verify(clienteDao, times(0)).deleteCliente(clienteDto.getDni());
    }
}
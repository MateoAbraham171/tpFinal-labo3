package ar.edu.utn.frbb.tup.service.cuentaServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.cuentaService.EliminadorDeCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EliminadorDeCuentaTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock private ClienteDao clienteDao;
    @Mock private CuentaDao cuentaDao;

    @InjectMocks private EliminadorDeCuenta eliminadorDeCuenta;

    @BeforeEach
    public void setUp() {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testEliminarCuentaSucces() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findCuentaDelCliente(cuenta.getCBU(), cuentaDto.getDniTitular())).thenReturn(cuenta);
        Cuenta cuentaEliminada = eliminadorDeCuenta.eliminarCuenta(cuentaDto.getDniTitular(), cuenta.getCBU());
        assertNotNull(cuentaEliminada);
        assertEquals(cuentaDto.getDniTitular(), cuentaEliminada.getDniTitular());
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findCuentaDelCliente(cuenta.getCBU(), cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).deleteCuenta(cuenta.getCBU());
    }

    @Test
    public void testEliminarCuentaClienteNoEncontradoException() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(null);
        assertThrows(ClienteNoEncontradoException.class, () -> eliminadorDeCuenta.eliminarCuenta(cuentaDto.getDniTitular(), cuenta.getCBU()));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).findCuentaDelCliente(cuenta.getCBU(), cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).deleteCuenta(cuenta.getCBU());
    }

    @Test
    public void testEliminarCuentaCuentaNoEncontradaException() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findCuentaDelCliente(cuenta.getCBU(), cuentaDto.getDniTitular())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> eliminadorDeCuenta.eliminarCuenta(cuentaDto.getDniTitular(), cuenta.getCBU()));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findCuentaDelCliente(cuenta.getCBU(), cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).deleteCuenta(cuenta.getCBU());
    }
}
package ar.edu.utn.frbb.tup.service.operacionServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import ar.edu.utn.frbb.tup.service.OperacionService.RetiradorDeSaldo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RetiradorDeSaldoTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock CuentaDao cuentaDao;
    @Mock MovimientoDao movimientoDao;

    @InjectMocks RetiradorDeSaldo retiradorDeSaldo;

    @BeforeEach
    public void setUp() throws BadRequestException {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testRetirarSaldoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        cuenta.setBalance(500);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta);
        Operacion operacion = retiradorDeSaldo.retiro(cuenta.getCBU(),monto, "Retiro");
        assertNotNull(operacion);
        assertEquals(cuenta.getCBU(), operacion.getCbu());
        assertEquals(400, operacion.getSaldoActual());
        assertEquals(100, operacion.getMonto());
        assertEquals("Retiro", operacion.getTipoOperacion());
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(1)).updateCuenta(cuenta);
        verify(movimientoDao, times(1)).saveMovimiento("Retiro", monto.monto(), cuenta.getCBU());
    }

    @Test
    public void testRetirarSaldoFailCuentaNoEncontrada() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        cuenta.setBalance(500);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro"));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(0)).updateCuenta(cuenta);
        verify(movimientoDao, times(0)).saveMovimiento("Retiro", monto.monto(), cuenta.getCBU());
    }

    @Test
    public void testRetirarSaldoFailCuentaDeBaja() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        cuenta.setBalance(500);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta.setEstado(false));
        assertThrows(CuentaDeBajaException.class, () -> retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro"));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(0)).updateCuenta(cuenta);
        verify(movimientoDao, times(0)).saveMovimiento("Retiro", monto.monto(), cuenta.getCBU());
    }

    @Test
    public void testRetirarSaldoFailSaldoInsuficiente() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta.setBalance(0));
        assertThrows(NoAlcanzaException.class, () -> retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro"));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(0)).updateCuenta(cuenta);
        verify(movimientoDao, times(0)).saveMovimiento("Retiro", monto.monto(), cuenta.getCBU());
    }
}

package ar.edu.utn.frbb.tup.service.operacionServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import ar.edu.utn.frbb.tup.service.OperacionService.DepositadorDeSaldo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositadorDeSaldoTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock CuentaDao cuentaDao;
    @Mock MovimientoDao movimientoDao;

    @InjectMocks DepositadorDeSaldo depositadorDeSaldo;

    @BeforeEach
    public void setUp() throws BadRequestException {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testDepositarSaldoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        cuenta.setBalance(500);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta);
        Operacion operacion = depositadorDeSaldo.deposito(cuenta.getCBU(),monto, "Deposito");
        assertNotNull(operacion);
        assertEquals(cuenta.getCBU(), operacion.getCbu());
        assertEquals(600, operacion.getSaldoActual());
        assertEquals(100, operacion.getMonto());
        assertEquals("Deposito", operacion.getTipoOperacion());
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(1)).updateCuenta(cuenta);
        verify(movimientoDao, times(1)).saveMovimiento("Deposito", monto.monto(), cuenta.getCBU());
    }

    @Test
    public void testDepositarSaldoFailCuentaNoEncontrada() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> depositadorDeSaldo.deposito(cuenta.getCBU(),monto, "Deposito"));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(0)).updateCuenta(cuenta);
        verify(movimientoDao, times(0)).saveMovimiento("Deposito", monto.monto(), cuenta.getCBU());
    }

    @Test
    public void testDepositarSaldoFailCuentaDeBaja() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta.setEstado(false));
        assertThrows(CuentaDeBajaException.class, () -> depositadorDeSaldo.deposito(cuenta.getCBU(),monto, "Deposito"));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(cuentaDao, times(0)).updateCuenta(cuenta);
        verify(movimientoDao, times(0)).saveMovimiento("Deposito", monto.monto(), cuenta.getCBU());
    }
}
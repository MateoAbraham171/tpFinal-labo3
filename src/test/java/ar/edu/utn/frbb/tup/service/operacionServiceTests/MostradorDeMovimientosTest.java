package ar.edu.utn.frbb.tup.service.operacionServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.exception.OperacionesExceptions.NoHayMovimientosException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.OperacionService.MostradorDeMovimientos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MostradorDeMovimientosTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock CuentaDao cuentaDao;
    @Mock MovimientoDao movimientoDao;

    @InjectMocks MostradorDeMovimientos mostradorDeMovimientos;

    @BeforeEach
    public void setUp() throws BadRequestException {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testMostrarMovimientosDeCuentaSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        List<Movimiento> movimientos = generadorDeObjetosParaTests.getListaDeMovimientos();
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta);
        when(movimientoDao.findMovimientos(cuenta.getCBU())).thenReturn(movimientos);
        List<Movimiento> movimientosDelCbu = mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU());
        assertNotNull(movimientosDelCbu);
        assertEquals(movimientos, movimientosDelCbu);
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(movimientoDao, times(1)).findMovimientos(cuenta.getCBU());
    }

    @Test
    public void testMostrarMovimientosDeCuentaNoEncontrada() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU()));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(movimientoDao, times(0)).findMovimientos(cuenta.getCBU());
    }

    @Test
    public void testMostrarMovimientosDeCuentaDeBaja() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta.setEstado(false));
        assertThrows(CuentaDeBajaException.class, () -> mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU()));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(movimientoDao, times(0)).findMovimientos(cuenta.getCBU());
    }

    @Test
    public void testNoHayMovimientos() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta);
        when(movimientoDao.findMovimientos(cuenta.getCBU())).thenReturn(emptyList());
        assertThrows(NoHayMovimientosException.class, () -> mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU()));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
        verify(movimientoDao, times(1)).findMovimientos(cuenta.getCBU());
    }
}
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
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.OperacionService.ConsultorDeSaldo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultorDeSaldoTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock CuentaDao cuentaDao;

    @InjectMocks ConsultorDeSaldo consultorDeSaldo;

    @BeforeEach
    public void setUp() throws BadRequestException {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testConsultarSaldoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta);
        Operacion operacion = consultorDeSaldo.consultarSaldo(cuenta.getCBU());
        assertNotNull(operacion);
        assertEquals(cuenta.getCBU(), operacion.getCbu());
        assertEquals(cuenta.getBalance(), operacion.getSaldoActual());
        assertEquals("Consulta", operacion.getTipoOperacion());
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
    }

    @Test
    public void testConsultarSaldoCuentaNoEncontrada() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> consultorDeSaldo.consultarSaldo(cuenta.getCBU()));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
    }

    @Test
    public void testConsultarSaldoCuentaDeBaja() throws BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        cuenta.setEstado(false);
        when(cuentaDao.findCuenta(cuenta.getCBU())).thenReturn(cuenta);
        assertThrows(CuentaDeBajaException.class, () -> consultorDeSaldo.consultarSaldo(cuenta.getCBU()));
        verify(cuentaDao, times(1)).findCuenta(cuenta.getCBU());
    }
}
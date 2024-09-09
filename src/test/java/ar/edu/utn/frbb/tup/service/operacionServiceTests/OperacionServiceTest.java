package ar.edu.utn.frbb.tup.service.operacionServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.exception.OperacionesExceptions.NoHayMovimientosException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import ar.edu.utn.frbb.tup.service.OperacionService.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperacionServiceTest {
    private CuentaDto cuentaDto;
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();

    @Mock private ConsultorDeSaldo consultorDeSaldo;
    @Mock private DepositadorDeSaldo depositadorDeSaldo;
    @Mock private MostradorDeMovimientos mostradorDeMovimientos;
    @Mock private RetiradorDeSaldo retiradorDeSaldo;
    @Mock private TransferService transferService;

    @BeforeEach
    public void setUp() {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "C", "P");
    }

    @Test
    public void testConsultarSaldoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto).setBalance(1000);
        Operacion operacionConsulta = new Operacion()
                .setTipoOperacion("Consulta")
                .setSaldoActual(cuenta.getBalance())
                .setCbu(cuenta.getCBU());
        when(consultorDeSaldo.consultarSaldo(cuenta.getCBU())).thenReturn(operacionConsulta);
        Operacion operacion = consultorDeSaldo.consultarSaldo(cuenta.getCBU());
        assertNotNull(operacion);
        assertEquals(cuenta.getBalance(), operacion.getSaldoActual());
        assertEquals("Consulta", operacion.getTipoOperacion());
        assertEquals(cuenta.getCBU(), operacion.getCbu());
        verify(consultorDeSaldo, times(1)).consultarSaldo(cuenta.getCBU());
    }

    @Test
    public void testConsultarSaldoFailCuentaNoEncontrada() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(consultorDeSaldo.consultarSaldo(cuenta.getCBU())).thenThrow(new CuentaNoEncontradaException(cuenta.getCBU()));
        assertThrows(CuentaNoEncontradaException.class, () -> consultorDeSaldo.consultarSaldo(cuenta.getCBU()));
        verify(consultorDeSaldo, times(1)).consultarSaldo(cuenta.getCBU());
    }

    @Test
    public void testConsultarSaldoFailCuentaDeBaja() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(consultorDeSaldo.consultarSaldo(cuenta.getCBU())).thenThrow(new CuentaDeBajaException(cuenta.getCBU()));
        assertThrows(CuentaDeBajaException.class, () -> consultorDeSaldo.consultarSaldo(cuenta.getCBU()));
        verify(consultorDeSaldo, times(1)).consultarSaldo(cuenta.getCBU());
    }

    @Test
    public void testDepositarSaldoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto).setBalance(0);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        Operacion operacionDeposito = new Operacion()
                .setTipoOperacion("Deposito")
                .setMonto(monto.monto())
                .setSaldoActual(cuenta.getBalance() + monto.monto())
                .setCbu(cuenta.getCBU());
        when(depositadorDeSaldo.deposito(cuenta.getCBU(), monto, operacionDeposito.getTipoOperacion())).thenReturn(operacionDeposito);
        Operacion operacion = depositadorDeSaldo.deposito(cuenta.getCBU(), monto, operacionDeposito.getTipoOperacion());
        assertNotNull(operacion);
        assertEquals(100, operacion.getSaldoActual());
        assertEquals(100, operacion.getMonto());
        assertEquals("Deposito", operacion.getTipoOperacion());
        assertEquals(cuenta.getCBU(), operacion.getCbu());
        verify(depositadorDeSaldo, times(1)).deposito(cuenta.getCBU(), monto, operacionDeposito.getTipoOperacion());
    }

    @Test
    public void testDepositarSaldoFailCuentaNoEncontrada() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(depositadorDeSaldo.deposito(cuenta.getCBU(), monto, "Deposito")).thenThrow(new CuentaNoEncontradaException(cuenta.getCBU()));
        assertThrows(CuentaNoEncontradaException.class, () -> depositadorDeSaldo.deposito(cuenta.getCBU(), monto, "Deposito"));
        verify(depositadorDeSaldo, times(1)).deposito(cuenta.getCBU(), monto, "Deposito");
    }

    @Test
    public void testDepositarSaldoFailCuentaDeBaja() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(depositadorDeSaldo.deposito(cuenta.getCBU(), monto, "Deposito")).thenThrow(new CuentaDeBajaException(cuenta.getCBU()));
        assertThrows(CuentaDeBajaException.class, () -> depositadorDeSaldo.deposito(cuenta.getCBU(), monto, "Deposito"));
        verify(depositadorDeSaldo, times(1)).deposito(cuenta.getCBU(), monto, "Deposito");
    }

    @Test
    public void testMostrarMovimientosSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        List<Movimiento> listaDeMovimientosEsperados = generadorDeObjetosParaTests.getListaDeMovimientos();
        when(mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU())).thenReturn(listaDeMovimientosEsperados);
        List<Movimiento> movimientos = mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU());
        assertNotNull(movimientos);
        assertEquals(2, movimientos.size());
        assertTrue(movimientos.containsAll(listaDeMovimientosEsperados));
        verify(mostradorDeMovimientos, times(1)).mostrarMovimientosDeCuenta(cuenta.getCBU());
    }

    @Test
    public void testMostrarMovimientosFailCuentaNoEncontrada() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU())).thenThrow(new CuentaNoEncontradaException(cuenta.getCBU()));
        assertThrows(CuentaNoEncontradaException.class, () -> mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU()));
        verify(mostradorDeMovimientos, times(1)).mostrarMovimientosDeCuenta(cuenta.getCBU());
    }

    @Test
    public void testMostrarMovimientosFailCuentaDeBaja() throws ConflictException, NotFoundException, BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU())).thenThrow(new CuentaDeBajaException(cuenta.getCBU()));
        assertThrows(CuentaDeBajaException.class, () -> mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU()));
        verify(mostradorDeMovimientos, times(1)).mostrarMovimientosDeCuenta(cuenta.getCBU());
    }

    @Test
    public void testMostrarMovimientosFailNoHayMovimientos() throws ConflictException, NotFoundException, BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU())).thenThrow(new NoHayMovimientosException());
        assertThrows(NoHayMovimientosException.class, () -> mostradorDeMovimientos.mostrarMovimientosDeCuenta(cuenta.getCBU()));
        verify(mostradorDeMovimientos, times(1)).mostrarMovimientosDeCuenta(cuenta.getCBU());
    }

    @Test
    public void testRetirarSaldoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto).setBalance(1000);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        Operacion operacionRetiro = new Operacion()
                .setTipoOperacion("Retiro")
                .setMonto(monto.monto())
                .setSaldoActual(cuenta.getBalance() - monto.monto())
                .setCbu(cuenta.getCBU());
        when(retiradorDeSaldo.retiro(cuenta.getCBU(), monto, operacionRetiro.getTipoOperacion())).thenReturn(operacionRetiro);
        Operacion operacion = retiradorDeSaldo.retiro(cuenta.getCBU(), monto, operacionRetiro.getTipoOperacion());
        assertNotNull(operacion);
        assertEquals(900, operacion.getSaldoActual());
        assertEquals(100, operacion.getMonto());
        assertEquals("Retiro", operacion.getTipoOperacion());
        assertEquals(cuenta.getCBU(), operacion.getCbu());
        verify(retiradorDeSaldo, times(1)).retiro(cuenta.getCBU(), monto, operacionRetiro.getTipoOperacion());
    }

    @Test
    public void testRetirarSaldoFailCuentaNoEncontrada() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro")).thenThrow(new CuentaNoEncontradaException(cuenta.getCBU()));
        assertThrows(CuentaNoEncontradaException.class, () -> retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro"));
        verify(retiradorDeSaldo, times(1)).retiro(cuenta.getCBU(), monto, "Retiro");
    }

    @Test
    public void testRetirarSaldoFailCuentaDeBaja() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        when(retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro")).thenThrow(new CuentaDeBajaException(cuenta.getCBU()));
        assertThrows(CuentaDeBajaException.class, () -> retiradorDeSaldo.retiro(cuenta.getCBU(), monto, "Retiro"));
        verify(retiradorDeSaldo, times(1)).retiro(cuenta.getCBU(), monto, "Retiro");
    }

    @Test
    public void testTransferenciaMismoBancoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuentaOrigen = new Cuenta(cuentaDto).setBalance(1000);
        Cuenta cuentaDestino = new Cuenta(cuentaDto).setBalance(0);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        TransferDto transferenciaObjeto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), monto.monto());
        Operacion operacionTransferencia = new Operacion()
                .setTipoOperacion("Transferencia")
                .setMonto(monto.monto())
                .setSaldoActual(cuentaOrigen.getBalance() - monto.monto())
                .setCbu(cuentaOrigen.getCBU());
        when(transferService.transferencia(transferenciaObjeto)).thenReturn(operacionTransferencia);
        Operacion operacion = transferService.transferencia(transferenciaObjeto);
        assertNotNull(operacion);
        assertEquals(900, operacion.getSaldoActual());
        assertEquals(100, operacion.getMonto());
        assertEquals("Transferencia", operacion.getTipoOperacion());
        assertEquals(cuentaOrigen.getCBU(), operacion.getCbu());
        verify(transferService, times(1)).transferencia(transferenciaObjeto);
    }

    @Test
    public void testTransferenciaDistintoBancoSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuentaOrigen = new Cuenta(cuentaDto).setBalance(1000);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        TransferDto transferenciaObjeto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), 111111, monto.monto());
        Operacion operacionTransferencia = new Operacion()
                .setTipoOperacion("Transferencia")
                .setMonto(monto.monto())
                .setSaldoActual(cuentaOrigen.getBalance() - monto.monto())
                .setCbu(cuentaOrigen.getCBU());
        when(transferService.transferencia(transferenciaObjeto)).thenReturn(operacionTransferencia);
        Operacion operacion = transferService.transferencia(transferenciaObjeto);
        assertNotNull(operacion);
        assertEquals(900, operacion.getSaldoActual());
        assertEquals(100, operacion.getMonto());
        assertEquals("Transferencia", operacion.getTipoOperacion());
        assertEquals(cuentaOrigen.getCBU(), operacion.getCbu());
        verify(transferService, times(1)).transferencia(transferenciaObjeto);
    }

    @Test
    public void testTransferenciaFailCuentaOrigenNoEncontrada() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuentaDestino = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        TransferDto transferenciaObjeto = generadorDeObjetosParaTests.getTransferDto(111111, cuentaDestino.getCBU(), monto.monto());
        when(transferService.transferencia(transferenciaObjeto)).thenThrow(new CuentaNoEncontradaException(111111));
        assertThrows(CuentaNoEncontradaException.class, () -> transferService.transferencia(transferenciaObjeto));
        verify(transferService, times(1)).transferencia(transferenciaObjeto);
    }

    @Test
    public void testTransferenciaFailCuentaOrigenDeBaja() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuentaOrigen = new Cuenta(cuentaDto).setEstado(false);
        Cuenta cuentaDestino = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        TransferDto transferenciaObjeto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), monto.monto());
        when(transferService.transferencia(transferenciaObjeto)).thenThrow(new CuentaDeBajaException(cuentaOrigen.getCBU()));
        assertThrows(CuentaDeBajaException.class, () -> transferService.transferencia(transferenciaObjeto));
        verify(transferService, times(1)).transferencia(transferenciaObjeto);
    }

    @Test
    public void testTransferenciaDistintoBancoFailCuentaDestinoNoEncontrada() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuentaOrigen = new Cuenta(cuentaDto);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        TransferDto transferenciaObjeto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), 222222, monto.monto());
        when(transferService.transferencia(transferenciaObjeto)).thenThrow(new CuentaNoEncontradaException(222222));
        assertThrows(CuentaNoEncontradaException.class, () -> transferService.transferencia(transferenciaObjeto));
        verify(transferService, times(1)).transferencia(transferenciaObjeto);
    }

    @Test
    public void testTransferenciaFailNoAlcanza() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuentaOrigen = new Cuenta(cuentaDto).setBalance(50);
        Cuenta cuentaDestino = new Cuenta(cuentaDto).setBalance(0);
        MontoDeOperacionDto monto = new MontoDeOperacionDto(100);
        TransferDto transferenciaObjeto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), monto.monto());
        when(transferService.transferencia(transferenciaObjeto)).thenThrow(new NoAlcanzaException(cuentaOrigen.getCBU(), monto.monto()));
        assertThrows(NoAlcanzaException.class, () -> transferService.transferencia(transferenciaObjeto));
        verify(transferService, times(1)).transferencia(transferenciaObjeto);
    }
}
package ar.edu.utn.frbb.tup.service.operacionServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDistintaMonedaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import ar.edu.utn.frbb.tup.service.OperacionService.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private TransferDto transferDto;

    @Mock CuentaDao cuentaDao;
    @Mock MovimientoDao movimientoDao;

    @InjectMocks TransferService transferService;

    @Test
    public void testTransferenciaMismoBancoSuccess() throws ConflictException, NotFoundException {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);
        Cuenta cuentaDestino = generadorDeObjetosParaTests.getCuenta(87654321L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES).setCBU(654321);
        cuentaOrigen.setBalance(500);
        cuentaDestino.setBalance(0);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(cuentaDestino.getCBU())).thenReturn(cuentaDestino);
        Operacion transferenciaRealizada = transferService.transferencia(transferDto);
        assertNotNull(transferenciaRealizada);
        assertEquals(cuentaOrigen.getCBU(), transferenciaRealizada.getCbu());
        assertEquals(400, cuentaOrigen.getBalance());
        assertEquals(100, cuentaDestino.getBalance());
        assertEquals(100, transferenciaRealizada.getMonto());
        assertEquals("Transferencia saliente exitosa!! :)", transferenciaRealizada.getTipoOperacion());
        verify(cuentaDao, times(2)).findCuenta(cuentaOrigen.getCBU());
        verify(cuentaDao, times(2)).findCuenta(cuentaDestino.getCBU());
        verify(movimientoDao, times(1)).saveMovimiento("Transferencia saliente", transferenciaRealizada.getMonto(), cuentaOrigen.getCBU());
    }

    @Test
    public void testTransferenciaBanelcoSuccess() throws ConflictException, NotFoundException {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), 111111, 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(111111)).thenReturn(null);
        Operacion transferenciaRealizada = transferService.transferencia(transferDto);
        assertNotNull(transferenciaRealizada);
        assertEquals(cuentaOrigen.getCBU(), transferenciaRealizada.getCbu());
        assertEquals(99900, cuentaOrigen.getBalance());
        assertEquals(100, transferenciaRealizada.getMonto());
        assertEquals("Transferencia saliente exitosa!! :)", transferenciaRealizada.getTipoOperacion());
        verify(cuentaDao, times(1)).findCuenta(111111);
        verify(cuentaDao, times(2)).findCuenta(cuentaOrigen.getCBU());
        verify(movimientoDao, times(1)).saveMovimiento("Transferencia saliente", transferenciaRealizada.getMonto(), cuentaOrigen.getCBU());
    }

    @Test
    public void testTransferenciaBanelcoFail() {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), 222222, 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(222222)).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> transferService.transferencia(transferDto));
        verify(cuentaDao, times(1)).findCuenta(222222);
        verify(cuentaDao, times(1)).findCuenta(cuentaOrigen.getCBU());
    }

    @Test
    public void testTransferenciaFailCuentaOrigenNoEncontrada() {
        transferDto = generadorDeObjetosParaTests.getTransferDto(12345678, 222222, 100);
        when(cuentaDao.findCuenta(12345678)).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> transferService.transferencia(transferDto));
        verify(cuentaDao, times(1)).findCuenta(12345678);
    }

    @Test
    public void testTransferenciaFailCuentaOrigenDeBaja() {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES).setEstado(false);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), 222222, 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        assertThrows(CuentaDeBajaException.class, () -> transferService.transferencia(transferDto));
        verify(cuentaDao, times(1)).findCuenta(cuentaOrigen.getCBU());
    }

    @Test
    public void testTransferenciaFailNoAlcanza() {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);
        Cuenta cuentaDestino = generadorDeObjetosParaTests.getCuenta(87654321L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES).setCBU(654321);
        cuentaOrigen.setBalance(50);
        cuentaDestino.setBalance(0);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        assertThrows(NoAlcanzaException.class, () -> transferService.transferencia(transferDto));
        verify(cuentaDao, times(1)).findCuenta(cuentaOrigen.getCBU());
    }

    @Test
    public void testTransferenciaFailCuentaDestinoDeBaja() {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);
        Cuenta cuentaDestino = generadorDeObjetosParaTests.getCuenta(87654321L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES).setCBU(654321).setEstado(false);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(cuentaDestino.getCBU())).thenReturn(cuentaDestino);
        assertThrows(CuentaDeBajaException.class, () -> transferService.transferencia(transferDto));
        verify(cuentaDao, times(1)).findCuenta(cuentaOrigen.getCBU());
        verify(cuentaDao, times(1)).findCuenta(cuentaDestino.getCBU());
    }

    @Test
    public void testTransferenciaFailCuentaDistintaMoneda() {
        Cuenta cuentaOrigen = generadorDeObjetosParaTests.getCuenta(12345678L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);
        Cuenta cuentaDestino = generadorDeObjetosParaTests.getCuenta(87654321L, TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS).setCBU(654321);
        transferDto = generadorDeObjetosParaTests.getTransferDto(cuentaOrigen.getCBU(), cuentaDestino.getCBU(), 100);
        when(cuentaDao.findCuenta(cuentaOrigen.getCBU())).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(cuentaDestino.getCBU())).thenReturn(cuentaDestino);
        assertThrows(CuentaDistintaMonedaException.class, () -> transferService.transferencia(transferDto));
        verify(cuentaDao, times(1)).findCuenta(cuentaOrigen.getCBU());
        verify(cuentaDao, times(1)).findCuenta(cuentaDestino.getCBU());
    }
}
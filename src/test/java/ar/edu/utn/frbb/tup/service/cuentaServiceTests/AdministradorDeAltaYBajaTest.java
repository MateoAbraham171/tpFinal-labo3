package ar.edu.utn.frbb.tup.service.cuentaServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.cuentaService.AdministradorDeAltaYBaja;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdministradorDeAltaYBajaTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock private ClienteDao clienteDao;
    @Mock private CuentaDao cuentaDao;

    @InjectMocks private AdministradorDeAltaYBaja administradorDeAltaYBaja;

    @BeforeEach
    public void setUp() {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testGestionarEstadoSuccess() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(clienteDao.findCliente(85876925L)).thenReturn(new Cliente());
        when(cuentaDao.findCuentaDelCliente(cuenta.getCBU(), cuenta.getDniTitular())).thenReturn(cuenta);
        administradorDeAltaYBaja.gestionarEstado(85876925L, cuenta.getCBU(), false);
        assertFalse(cuenta.getEstado());
        verify(clienteDao, times(1)).findCliente(85876925L);
        verify(cuentaDao, times(1)).findCuentaDelCliente(cuenta.getCBU(), cuenta.getDniTitular());
        verify(cuentaDao, times(1)).deleteCuenta(cuenta.getCBU());
        verify(cuentaDao, times(1)).saveCuenta(cuenta);
    }

    @Test
    public void testGestionarEstadoClienteNoEncontradoException() {
        when(clienteDao.findCliente(85876925L)).thenReturn(null);
        assertThrows(ClienteNoEncontradoException.class, () -> administradorDeAltaYBaja.gestionarEstado(85876925L, 123456789L, false));
        verify(clienteDao, times(1)).findCliente(85876925L);
    }

    @Test
    public void testGestionarEstadoCuentaNoEncontradaException() {
        when(clienteDao.findCliente(85876925L)).thenReturn(new Cliente());
        when(cuentaDao.findCuentaDelCliente(123456789L, 85876925L)).thenReturn(null);
        assertThrows(CuentaNoEncontradaException.class, () -> administradorDeAltaYBaja.gestionarEstado(85876925L, 123456789L, false));
        verify(clienteDao, times(1)).findCliente(85876925L);
        verify(cuentaDao, times(1)).findCuentaDelCliente(123456789L, 85876925L);
    }
}
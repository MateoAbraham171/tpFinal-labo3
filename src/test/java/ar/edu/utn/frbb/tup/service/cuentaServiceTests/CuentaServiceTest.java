package ar.edu.utn.frbb.tup.service.cuentaServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.cuentaService.AdministradorDeAltaYBaja;
import ar.edu.utn.frbb.tup.service.cuentaService.CreadorDeCuenta;
import ar.edu.utn.frbb.tup.service.cuentaService.EliminadorDeCuenta;
import ar.edu.utn.frbb.tup.service.cuentaService.MostradorDeCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {
    private CuentaDto cuentaDto;
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();

    @Mock private CreadorDeCuenta creadorDeCuenta;
    @Mock private EliminadorDeCuenta eliminadorDeCuenta;
    @Mock private MostradorDeCuenta mostradorDeCuenta;
    @Mock private AdministradorDeAltaYBaja administradorDeAltaYBaja;

    @BeforeEach
    public void setUp() {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "c", "d");
    }

    @Test
    public void testCrearCuentaSuccess() throws BadRequestException, ConflictException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(creadorDeCuenta.crearCuenta(cuentaDto)).thenReturn(cuenta);
        Cuenta cuentaCreada = creadorDeCuenta.crearCuenta(cuentaDto);
        assertNotNull(cuentaCreada);
        assertEquals(cuenta, cuentaCreada);
        verify(creadorDeCuenta, times(1)).crearCuenta(cuentaDto);
    }

    @Test
    public void testCrearCuentaFailClienteNoEncontrado() throws ConflictException, NotFoundException, BadRequestException {
        when(creadorDeCuenta.crearCuenta(cuentaDto)).thenThrow(new ClienteNoEncontradoException(cuentaDto.getDniTitular()));
        assertThrows(ClienteNoEncontradoException.class, () -> creadorDeCuenta.crearCuenta(cuentaDto));
        verify(creadorDeCuenta, times(1)).crearCuenta(cuentaDto);
    }

    @Test
    public void testCrearCuentaFailCuentaAlreadyExists() throws ConflictException, NotFoundException, BadRequestException {
        when(creadorDeCuenta.crearCuenta(cuentaDto)).thenThrow(new CuentaAlreadyExistsException());
        assertThrows(CuentaAlreadyExistsException.class, () -> creadorDeCuenta.crearCuenta(cuentaDto));
        verify(creadorDeCuenta, times(1)).crearCuenta(cuentaDto);
    }

    @Test
    public void testDarAltaBajaSuccess() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(administradorDeAltaYBaja.gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false)).thenReturn(cuenta);
        administradorDeAltaYBaja.gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false);
        //no verifico que se haya guardado la cuenta en false porque
        //deberia involucrarme en la logica interna del metodo, el cual ya esta chequeado
        verify(administradorDeAltaYBaja, times(1)).gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false);
    }

    @Test
    public void testDarAltaBajaFailClienteNoEncontrado() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(administradorDeAltaYBaja.gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false)).thenThrow(new ClienteNoEncontradoException(cuenta.getDniTitular()));
        assertThrows(ClienteNoEncontradoException.class, () -> administradorDeAltaYBaja.gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false));
        verify(administradorDeAltaYBaja, times(1)).gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false);
    }

    @Test
    public void testDarAltaBajaFailCuentaNoEncontrada() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(administradorDeAltaYBaja.gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false)).thenThrow(new CuentaNoEncontradaException(cuenta.getDniTitular()));
        assertThrows(CuentaNoEncontradaException.class, () -> administradorDeAltaYBaja.gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false));
        verify(administradorDeAltaYBaja, times(1)).gestionarEstado(cuenta.getDniTitular(), cuenta.getCBU(), false);
    }

    @Test
    public void testEliminarCuentaSuccess() throws NotFoundException, BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(eliminadorDeCuenta.eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU())).thenReturn(cuenta);
        Cuenta cuentaEliminada = eliminadorDeCuenta.eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU());
        assertNotNull(cuentaEliminada);
        assertEquals(cuenta, cuentaEliminada);
        verify(eliminadorDeCuenta, times(1)).eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU());
    }

    @Test
    public void testEliminarCuentaFailClienteNoEncontrado() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(eliminadorDeCuenta.eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU())).thenThrow(new ClienteNoEncontradoException(cuenta.getDniTitular()));
        assertThrows(ClienteNoEncontradoException.class, () -> eliminadorDeCuenta.eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU()));
        verify(eliminadorDeCuenta, times(1)).eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU());
    }

    @Test
    public void testEliminarCuentaFailCuentaNoEncontrada() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(eliminadorDeCuenta.eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU())).thenThrow(new CuentaNoEncontradaException(cuenta.getCBU()));
        assertThrows(CuentaNoEncontradaException.class, () -> eliminadorDeCuenta.eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU()));
        verify(eliminadorDeCuenta, times(1)).eliminarCuenta(cuenta.getDniTitular(), cuenta.getCBU());
    }

    @Test
    public void testMostrarCuentaSuccess() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(mostradorDeCuenta.mostrarCuenta(cuenta.getDniTitular())).thenReturn(Set.of(cuenta));
        Set<Cuenta> cuentas = mostradorDeCuenta.mostrarCuenta(cuenta.getDniTitular());
        assertNotNull(cuentas);
        assertEquals(1, cuentas.size());
        assertTrue(cuentas.contains(cuenta));
        verify(mostradorDeCuenta, times(1)).mostrarCuenta(cuenta.getDniTitular());
    }

    @Test
    public void testMostrarCuentaFailClienteNoEncontrado() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(mostradorDeCuenta.mostrarCuenta(cuenta.getDniTitular())).thenThrow(new ClienteNoEncontradoException(cuenta.getDniTitular()));
        assertThrows(ClienteNoEncontradoException.class, () -> mostradorDeCuenta.mostrarCuenta(cuenta.getDniTitular()));
        verify(mostradorDeCuenta, times(1)).mostrarCuenta(cuenta.getDniTitular());
    }

    @Test
    public void testMostrarCuentaFailCuentaNoEncontrada() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(mostradorDeCuenta.mostrarCuenta(cuenta.getDniTitular())).thenThrow(new CuentaNoEncontradaException(cuenta.getCBU()));
        assertThrows(CuentaNoEncontradaException.class, () -> mostradorDeCuenta.mostrarCuenta(cuenta.getDniTitular()));
        verify(mostradorDeCuenta, times(1)).mostrarCuenta(cuenta.getDniTitular());
    }
}
package ar.edu.utn.frbb.tup.service.cuentaServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoHayCuentasException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.cuentaService.MostradorDeCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MostradorDeCuentaTest {
    private CuentaDto cuentaDto;
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();

    @Mock private CuentaDao cuentaDao;
    @Mock private ClienteDao clienteDao;

    @InjectMocks
    private MostradorDeCuenta mostradorDeCuenta;

    @BeforeEach
    public void setUp() {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(123456789L, "c", "p");
    }

    @Test
    public void testMostrarCuentaSuccess() throws BadRequestException, NotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findAllCuentasDelCliente(cuentaDto.getDniTitular())).thenReturn(generadorDeObjetosParaTests.getListaDeCuentas(cuenta));
        Set<Cuenta> ListaDeCuentasParaMostrar = mostradorDeCuenta.mostrarCuenta(cuentaDto.getDniTitular());
        assertNotNull(ListaDeCuentasParaMostrar);
        assertNotEquals(0, ListaDeCuentasParaMostrar.size());
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
    }

    @Test
    public void testMostrarCuentaClienteNoEncontradoException() {
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(null);
        assertThrows(ClienteNoEncontradoException.class, () -> mostradorDeCuenta.mostrarCuenta(cuentaDto.getDniTitular()));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
    }

    @Test
    public void testMostrarCuentaNoHayCuentasException() {
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findAllCuentasDelCliente(cuentaDto.getDniTitular())).thenReturn(anySet());
        assertThrows(NoHayCuentasException.class, () -> mostradorDeCuenta.mostrarCuenta(cuentaDto.getDniTitular()));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
    }
}
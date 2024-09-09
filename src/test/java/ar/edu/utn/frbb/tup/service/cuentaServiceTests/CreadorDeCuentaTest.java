package ar.edu.utn.frbb.tup.service.cuentaServiceTests;

import ar.edu.utn.frbb.tup.GeneradorDeObjetosParaTests;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.service.cuentaService.CreadorDeCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreadorDeCuentaTest {
    private final GeneradorDeObjetosParaTests generadorDeObjetosParaTests = new GeneradorDeObjetosParaTests();
    private CuentaDto cuentaDto;

    @Mock private ClienteDao clienteDao;
    @Mock private CuentaDao cuentaDao;

    @InjectMocks private CreadorDeCuenta creadorDeCuenta;

    @BeforeEach
    public void setUp() {
        cuentaDto = generadorDeObjetosParaTests.getCuentaDto(85876925L, "A", "d");
    }

    @Test
    public void testCrearCuentaSuccess() throws ConflictException, NotFoundException, BadRequestException {
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findCuenta(anyLong())).thenReturn(null);
        when(cuentaDao.findAllCuentasDelCliente(cuentaDto.getDniTitular())).thenReturn(null);
        Cuenta cuentaCreada = creadorDeCuenta.crearCuenta(cuentaDto);
        assertNotNull(cuentaCreada);
        assertEquals(cuentaDto.getDniTitular(), cuentaCreada.getDniTitular());
        verify(clienteDao, times (1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findCuenta(anyLong());
        verify(cuentaDao, times(1)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).saveCuenta(cuentaCreada);
    }

    @Test
    public void testCrearCuentaClienteNoEncontradoException() {
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(null);
        assertThrows(ClienteNoEncontradoException.class, () -> creadorDeCuenta.crearCuenta(cuentaDto));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).findCuenta(anyLong());
        verify(cuentaDao, times(0)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).saveCuenta(any(Cuenta.class));
    }

    @Test
    public void testCrearCuentaAlreadyExistsException() {
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findCuenta(anyLong())).thenReturn(new Cuenta());
        assertThrows(CuentaAlreadyExistsException.class, () -> creadorDeCuenta.crearCuenta(cuentaDto));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findCuenta(anyLong());
        verify(cuentaDao, times(0)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).saveCuenta(any(Cuenta.class));
    }

    @Test
    public void testCrearCuentaTipoCuentaAlreadyExistsException() throws BadRequestException {
        when(clienteDao.findCliente(cuentaDto.getDniTitular())).thenReturn(new Cliente());
        when(cuentaDao.findCuenta(anyLong())).thenReturn(null);
        when(cuentaDao.findAllCuentasDelCliente(cuentaDto.getDniTitular())).thenReturn(generadorDeObjetosParaTests.getListaDeCuentas(new Cuenta(cuentaDto)));
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> creadorDeCuenta.crearCuenta(cuentaDto));
        verify(clienteDao, times(1)).findCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(1)).findCuenta(anyLong());
        verify(cuentaDao, times(1)).findAllCuentasDelCliente(cuentaDto.getDniTitular());
        verify(cuentaDao, times(0)).saveCuenta(any(Cuenta.class));
    }
}
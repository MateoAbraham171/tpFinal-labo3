package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.ControllerExceptions.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.DniInvalidoException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.InputInvalidoException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaControllerValidatorTest {
    private CuentaControllerValidator cuentaControllerValidator;

    @Mock private CuentaDto cuentaDto;

    @BeforeEach
    public void setUp() {
        cuentaControllerValidator = new CuentaControllerValidator();
    }

    @Test
    public void testValidateCuentaSuccess() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn("p");
        when(cuentaDto.getDniTitular()).thenReturn(12345678L);
        assertDoesNotThrow(() -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoCuenta();
        verify(cuentaDto, times(1)).getTipoMoneda();
        verify(cuentaDto, times(1)).getDniTitular();
    }

    @Test
    public void testValidateTipoCuentaNull() {
        when(cuentaDto.getTipoCuenta()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoCuenta();
    }

    @Test
    public void testValidateTipoCuentaVacio() {
        when(cuentaDto.getTipoCuenta()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoCuenta();
    }

    @Test
    public void testValidateTipoCuentaInvalido() {
        when(cuentaDto.getTipoCuenta()).thenReturn("Z");
        assertThrows(InputInvalidoException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoCuenta();
    }

    @Test
    public void testValidateTipoMonedaNull() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoMoneda();
    }

    @Test
    public void testValidateTipoMonedaVacio() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoMoneda();
    }

    @Test
    public void testValidateTipoMonedaInvalido() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn("Z");
        assertThrows(InputInvalidoException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getTipoMoneda();
    }

    @Test
    public void testValidateDniTitularVacio() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn("d");
        when(cuentaDto.getDniTitular()).thenReturn(0L);
        assertThrows(CampoVacioException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getDniTitular();
    }

    @Test
    public void testValidateDniTitularMenosDe8Digitos() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn("d");
        when(cuentaDto.getDniTitular()).thenReturn(1234567L);
        assertThrows(DniInvalidoException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getDniTitular();
    }

    @Test
    public void testValidateDniTitularMasDe8Digitos() {
        when(cuentaDto.getTipoCuenta()).thenReturn("C");
        when(cuentaDto.getTipoMoneda()).thenReturn("d");
        when(cuentaDto.getDniTitular()).thenReturn(123456789L);
        assertThrows(DniInvalidoException.class, () -> cuentaControllerValidator.validate(cuentaDto));
        verify(cuentaDto, times(1)).getDniTitular();
    }
}
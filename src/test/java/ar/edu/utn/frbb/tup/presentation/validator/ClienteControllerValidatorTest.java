package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteMenorException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.FechaNacimientoInvalidaException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.DniInvalidoException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.InputInvalidoException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerValidatorTest {
    private ClienteControllerValidator clienteControllerValidator;

    @Mock private ClienteDto clienteDto;

    @BeforeEach
    public void setUp() {
        clienteControllerValidator = new ClienteControllerValidator();
    }

    @Test
    public void testValidateClienteSuccess() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn("f");
        when(clienteDto.getDni()).thenReturn(85876925L);
        assertDoesNotThrow(() -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
        verify(clienteDto, times(1)).getDni();
    }

    @Test
    public void testValidateNombreNull() {
        when(clienteDto.getNombre()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
    }

    @Test
    public void testValidateNombreVacio() {
        when(clienteDto.getNombre()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
    }

    @Test
    public void testValidateNombreSoloLetrasFail() {
        when(clienteDto.getNombre()).thenReturn("Mateo123");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
    }

    @Test
    public void testValidateApellidoNull() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
    }

    @Test
    public void testValidateApellidoVacio() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
    }

    @Test
    public void testValidateApellidoSoloLetrasFail() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka123");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
    }

    @Test
    public void testValidateDireccionNull() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
    }

    @Test
    public void testValidateDireccionVacia() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
    }

    @Test
    public void testValidateDireccionDosOMasPartesFail() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Avenida");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
    }

    @Test
    public void testValidateDireccionNumeroAlFInalFail() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Avenida Siempre Viva");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
    }

    @Test
    public void testValidateFechaNacimientoFutura() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2100-10-10");
        assertThrows(FechaNacimientoInvalidaException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
    }

    @Test
    public void testValidateFechaNacimientoMenorA1900() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("1899-10-10");
        assertThrows(FechaNacimientoInvalidaException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
    }

    @Test
    public void testValidateNoEsMayor() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2008-10-10");
        assertThrows(ClienteMenorException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
    }

    @Test
    public void testValidateMailNull() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
    }

    @Test
    public void testValidateMailVacio() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
    }

    @Test
    public void testValidateMailNoContieneArroba() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171yahoo.com");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
    }

    @Test
    public void testValidateMailNoContienePunto() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoocom");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
    }

    @Test
    public void testValidateTipoPersonaNull() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn(null);
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
    }

    @Test
    public void testValidateTipoPersonaVacio() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn("");
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
    }

    @Test
    public void testTipoPersonaInvalido() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn("p");
        assertThrows(InputInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
    }

    @Test
    public void testValidateDniVacio() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn("f");
        when(clienteDto.getDni()).thenReturn(0L);
        assertThrows(CampoVacioException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
        verify(clienteDto, times(1)).getDni();
    }

    @Test
    public void testValidateDniMenosDe8Digitos() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn("f");
        when(clienteDto.getDni()).thenReturn(1L);
        assertThrows(DniInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
        verify(clienteDto, times(1)).getDni();
    }

    @Test
    public void testValidateDniMasDe8Digitos() {
        when(clienteDto.getNombre()).thenReturn("Mateo");
        when(clienteDto.getApellido()).thenReturn("Takanaka");
        when(clienteDto.getDireccion()).thenReturn("Av. Siempre Viva 123");
        when(clienteDto.getFechaNacimiento()).thenReturn("2001-10-10");
        when(clienteDto.getMail()).thenReturn("takanaka171@yahoo.com");
        when(clienteDto.getTipoPersona()).thenReturn("f");
        when(clienteDto.getDni()).thenReturn(11111111111111L);
        assertThrows(DniInvalidoException.class, () -> clienteControllerValidator.validateCliente(clienteDto));
        verify(clienteDto, times(1)).getNombre();
        verify(clienteDto, times(1)).getApellido();
        verify(clienteDto, times(1)).getDireccion();
        verify(clienteDto, times(1)).getFechaNacimiento();
        verify(clienteDto, times(1)).getMail();
        verify(clienteDto, times(1)).getTipoPersona();
        verify(clienteDto, times(1)).getDni();
    }
}
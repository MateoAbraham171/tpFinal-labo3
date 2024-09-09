package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.ControllerExceptions.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.OperacionesExceptions.CBUsIgualesException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferValidatorTest {
    private TransferValidator transferValidator;

    @Mock private TransferDto transferDto;

    @BeforeEach
    public void setUp() {
        transferValidator = new TransferValidator();
    }

    @Test
    public void testValidateTransferSuccess() {
        when(transferDto.cbuOrigen()).thenReturn(123456L);
        when(transferDto.cbuDestino()).thenReturn(654321L);
        when(transferDto.monto()).thenReturn(100D);
        assertDoesNotThrow(() -> transferValidator.validate(transferDto));
        verify(transferDto, times(2)).cbuOrigen();
        verify(transferDto, times(2)).cbuDestino();
        verify(transferDto, times(1)).monto();
    }

    @Test
    public void testValidateTransferFailNotCbuOrigen() {
        when(transferDto.cbuOrigen()).thenReturn(0L);
        assertThrows((CampoVacioException.class), () -> transferValidator.validate(transferDto));
        verify(transferDto, times(1)).cbuOrigen();
    }

    @Test
    public void testValidateTransferFailNotCbuDestino() {
        when(transferDto.cbuOrigen()).thenReturn(123456L);
        when(transferDto.cbuDestino()).thenReturn(0L);
        assertThrows((CampoVacioException.class), () -> transferValidator.validate(transferDto));
        verify(transferDto, times(1)).cbuOrigen();
        verify(transferDto, times(1)).cbuDestino();
    }

    @Test
    public void testValidateTransferFailNotMonto() {
        when(transferDto.cbuOrigen()).thenReturn(123456L);
        when(transferDto.cbuDestino()).thenReturn(654321L);
        when(transferDto.monto()).thenReturn(0D);
        assertThrows((CampoVacioException.class), () -> transferValidator.validate(transferDto));
        verify(transferDto, times(1)).cbuOrigen();
        verify(transferDto, times(1)).cbuDestino();
        verify(transferDto, times(1)).monto();
    }

    @Test
    public void testValidateTransferFailCbusIguales() {
        when(transferDto.cbuOrigen()).thenReturn(123456L);
        when(transferDto.cbuDestino()).thenReturn(123456L);
        when(transferDto.monto()).thenReturn(100D);
        assertThrows((CBUsIgualesException.class), () -> transferValidator.validate(transferDto));
        verify(transferDto, times(2)).cbuOrigen();
        verify(transferDto, times(2)).cbuDestino();
        verify(transferDto, times(1)).monto();
    }
}
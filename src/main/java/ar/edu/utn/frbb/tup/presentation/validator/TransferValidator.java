package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.ControllerException.InputInvalidoException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TransferValidator {

    public static void validate(TransferDto transferDto) throws InputInvalidoException {
        validateDatosCompletos(transferDto);
        validateCBU(transferDto.getCuentaOrigen(), "Origen");
        validateCBU(transferDto.getCuentaDestino(), "Destino");
        validateDistintosCBUs(transferDto.getCuentaOrigen(), transferDto.getCuentaDestino());
    }

    private static void validateCBU(long cuenta, String tipo) throws InputInvalidoException {
        if (cuenta < 100000 || cuenta > 999999) {
            throw new InputInvalidoException("Error: El CBU " + tipo + " tiene que ser de 6 dígitos");
        }
    }

    private static void validateDistintosCBUs(long cuentaOrigen, long cuentaDestino) throws InputInvalidoException {
        if (cuentaOrigen == cuentaDestino) {
            throw new InputInvalidoException("Error: El CBU de Origen y Destino no pueden ser iguales");
        }
    }

    private static void validateDatosCompletos(TransferDto transferDto) throws InputInvalidoException {
        validateNonZero(transferDto.getCuentaOrigen(), "CBU de Origen");
        validateNonZero(transferDto.getCuentaDestino(), "CBU de Destino");
        validateNonZero(transferDto.getMonto(), "Monto a transferir");
        validateNotNull(transferDto.getMoneda(), "Tipo de moneda");
        validateNotNull(transferDto.getTipoTransaccion(), "Tipo de transacción");
    }

    private static void validateNonZero(double value, String valor) throws InputInvalidoException {
        if (value == 0) {
            throw new InputInvalidoException("Error: Ingrese un " + valor);
        }
    }

    private static void validateNotNull(Object value, String valor) throws InputInvalidoException {
        if (value == null) {
            throw new InputInvalidoException("Error: Ingrese " + valor);
        }
    }
}
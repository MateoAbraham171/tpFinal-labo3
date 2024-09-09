package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.InputInvalidoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDistintaMonedaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.OperacionesExceptions.CBUsIgualesException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.springframework.stereotype.Component;


@Component
public class TransferValidator {

    public void validate(TransferDto transferDto) throws BadRequestException, ConflictException {
        validateDatosCompletos(transferDto);
        validateDistintosCBUs(transferDto.cbuOrigen(), transferDto.cbuDestino());
    }

    private void validateDatosCompletos(TransferDto transferDto) throws BadRequestException {
        validateNonZero(transferDto.cbuOrigen(), "CBU de Origen");
        validateNonZero(transferDto.cbuDestino(), "CBU de Destino");
        validateNonZero(transferDto.monto(), "Monto a transferir");
    }

    private void validateNonZero(double value, String valor) throws BadRequestException {
        if (value == 0)
            throw new CampoVacioException(valor);
    }

    private void validateDistintosCBUs(long cuentaOrigen, long cuentaDestino) throws ConflictException {
        if (cuentaOrigen == cuentaDestino)
            throw new CBUsIgualesException();
    }
}
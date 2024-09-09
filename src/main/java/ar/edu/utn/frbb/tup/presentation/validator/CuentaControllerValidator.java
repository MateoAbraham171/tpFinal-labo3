package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.*;
import org.springframework.stereotype.Component;

@Component
public class CuentaControllerValidator {

    public void validate(CuentaDto cuentaDto) throws BadRequestException {
        validateTipoCuenta(cuentaDto.getTipoCuenta());
        validateTipoMoneda(cuentaDto.getTipoMoneda());
        new ClienteControllerValidator().validateDni(cuentaDto.getDniTitular());
    }

    private void validateTipoCuenta(String tipoCuenta) throws BadRequestException {
        if (tipoCuenta == null || tipoCuenta.isEmpty())
            throw new CampoVacioException("tipo de cuenta");

        TipoCuenta.fromString(tipoCuenta);
    }

    private void validateTipoMoneda(String tipoMoneda) throws BadRequestException {
        if (tipoMoneda == null || tipoMoneda.isEmpty())
            throw new CampoVacioException("tipo de moneda");

        TipoMoneda.fromString(tipoMoneda);
    }
}
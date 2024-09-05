package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.*;
import org.springframework.stereotype.Component;

@Component
public class CuentaControllerValidator {

    private final int MIN_DNI = 10000000;
    private final int MAX_DNI = 99999999;

    public void validate(CuentaDto cuentaDto) throws BadRequestException {

        // Tipo de cuenta
        if (cuentaDto.getTipoCuenta() == null || cuentaDto.getTipoCuenta().isEmpty()) {
            throw new CampoVacioException("tipo de cuenta");
        }

        // Tipo de moneda
        if (cuentaDto.getTipoMoneda() == null || cuentaDto.getTipoMoneda().isEmpty()) {
            throw new CampoVacioException("tipo de moneda");
        }

        new ClienteControllerValidator().validateDni(cuentaDto.getDniTitular());
    }
}

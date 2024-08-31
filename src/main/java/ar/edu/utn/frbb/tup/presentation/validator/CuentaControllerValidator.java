package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.exception.ControllerException.*;
import org.springframework.stereotype.Component;

@Component
public class CuentaControllerValidator {

    private static final int MIN_DNI = 10000000;
    private static final int MAX_DNI = 99999999;

    public void validate(CuentaDto cuentaDto) {

        // Tipo de cuenta
        if (cuentaDto.getTipoCuenta() == null || cuentaDto.getTipoCuenta().isEmpty()) {
            throw new CampoVacioException("tipo de cuenta");
        }

        // Tipo de moneda
        if (cuentaDto.getTipoMoneda() == null || cuentaDto.getTipoMoneda().isEmpty()) {
            throw new CampoVacioException("tipo de moneda");
        }

        // DNI Titular
        if (cuentaDto.getDniTitular() == 0) {
            throw new DniInvalidoException("DNI");
        }

        if (cuentaDto.getDniTitular() < MIN_DNI || cuentaDto.getDniTitular() > MAX_DNI) {
            throw new DniInvalidoException("Error: El DNI debe tener 8 dígitos");
        }
    }
}

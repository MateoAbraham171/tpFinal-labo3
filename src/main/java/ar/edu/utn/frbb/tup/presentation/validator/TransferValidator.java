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
    private final CuentaDao cuentaDao;

    public TransferValidator(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }

    public void validate(TransferDto transferDto) throws BadRequestException, NotFoundException, ConflictException {
        //valido que los datos lleguen completos
        validateDatosCompletos(transferDto);
        //valido que los cbus sean distintos porque es facil
        validateDistintosCBUs(transferDto.cbuOrigen(), transferDto.cbuDestino());
        //valido que el cbu este asociado a una cuenta y guardo la cuenta
        Cuenta cuentaOrigen = validateCBU(transferDto.cbuOrigen(), true);
        //valido que la cuenta este activa
        validateCuentaAlta(cuentaOrigen);
        //valido que el cbu este asociado a una cuenta, puede ser una cuenta de otro banco, por ende puedo guardar null ya que no esta en mi sistema
        Cuenta cuentaDestino = validateCBU(transferDto.cbuDestino(), false);
        //si la cuenta de destino no es null, o sea es de mi banco, valido que este activa y que sea de la misma moneda que la cuenta de origen
        if (cuentaDestino != null) {
            validateCuentaAlta(cuentaDestino);
            validateIdemTipoMoneda(cuentaOrigen.getMoneda(), cuentaDestino.getMoneda());
        }
    }

    private void validateDatosCompletos(TransferDto transferDto) throws BadRequestException {
        validateNonZero(transferDto.cbuOrigen(), "CBU de Origen");
        validateNonZero(transferDto.cbuDestino(), "CBU de Destino");
        validateNonZero(transferDto.monto(), "Monto a transferir");
    }

    private void validateNonZero(double value, String valor) throws BadRequestException {
        if (value == 0) {
            throw new CampoVacioException(valor);
        }
    }

    private void validateDistintosCBUs(long cuentaOrigen, long cuentaDestino) throws ConflictException {
        if (cuentaOrigen == cuentaDestino) {
            throw new CBUsIgualesException();
        }
    }

    private Cuenta validateCBU(long CBUcuenta, boolean esCuentaOrigen) throws BadRequestException, NotFoundException {
        if (esCuentaOrigen && (CBUcuenta < 100000 || CBUcuenta > 999999))
            throw new InputInvalidoException("Error: El CBU de origen tiene que ser de 6 dígitos");

        Cuenta cuentaEncontrada = cuentaDao.findCuenta(CBUcuenta);
        if (esCuentaOrigen && cuentaEncontrada == null)
            throw new CuentaNoEncontradaException(CBUcuenta);

        return cuentaEncontrada;
    }

    private void validateCuentaAlta(Cuenta cuenta) throws ConflictException {
        if (!cuenta.getEstado())
            throw new CuentaDeBajaException(cuenta.getCBU());
    }

    private void validateIdemTipoMoneda (TipoMoneda monedaOrigen, TipoMoneda monedaDestino) throws ConflictException {
        if (!monedaOrigen.equals(monedaDestino)) {
            throw new CuentaDistintaMonedaException();
        }
    }
}
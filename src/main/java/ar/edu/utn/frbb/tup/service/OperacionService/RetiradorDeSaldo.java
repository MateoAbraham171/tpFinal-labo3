package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import org.springframework.stereotype.Service;

@Service
public class RetiradorDeSaldo {
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientoDao;

    public RetiradorDeSaldo(CuentaDao cuentaDao, MovimientoDao movimientoDao) {
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
    }

    public Operacion retiro(long cbu, MontoDeOperacionDto montoDeOperacion, String tipoOperacion) throws NotFoundException, ConflictException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);
        if (!cuenta.getEstado())
            throw new CuentaDeBajaException(cbu);
        if (cuenta.getBalance() < montoDeOperacion.monto())
            throw new NoAlcanzaException(cuenta.getBalance());

        cuenta.setBalance(cuenta.getBalance() - montoDeOperacion.monto());
        cuentaDao.updateCuenta(cuenta);
        movimientoDao.saveMovimiento(tipoOperacion, montoDeOperacion.monto(), cbu);

        return new Operacion().setCbu(cbu)
                .setSaldoActual(cuenta.getBalance())
                .setMonto(montoDeOperacion.monto())
                .setTipoOperacion(tipoOperacion);
    }
}

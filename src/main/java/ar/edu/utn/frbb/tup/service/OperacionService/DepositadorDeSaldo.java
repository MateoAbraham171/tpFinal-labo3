package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import org.springframework.stereotype.Service;

@Service
public class DepositadorDeSaldo {
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientoDao;

    public DepositadorDeSaldo(CuentaDao cuentaDao, MovimientoDao movimientoDao) {
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
    }

    public Operacion deposito(long cbu, MontoDeOperacionDto montoDeOperacion, String tipoOperacion) throws NotFoundException, ConflictException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);
        if(!cuenta.getEstado())
            throw new CuentaDeBajaException(cbu);

        cuenta.setBalance(cuenta.getBalance() + montoDeOperacion.monto());
        cuentaDao.updateCuenta(cuenta);
        movimientoDao.saveMovimiento(tipoOperacion, montoDeOperacion.monto(), cuenta.getCBU());

        return new Operacion().setCbu(cbu)
                .setSaldoActual(cuenta.getBalance())
                .setMonto(montoDeOperacion.monto())
                .setTipoOperacion(tipoOperacion);
    }
}

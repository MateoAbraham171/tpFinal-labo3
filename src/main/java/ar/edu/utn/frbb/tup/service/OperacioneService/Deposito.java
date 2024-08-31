package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class Deposito {
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientoDao;
    private final String tipoOperacion = "Deposito";

    public Deposito(CuentaDao cuentaDao, MovimientoDao movimientoDao) {
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
    }

    public Operacion deposito(long cbu, double monto) throws CuentaNoEncontradaException, CuentaDeBajaException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException(cbu);
        }

        if(!cuenta.getEstado()) {
            throw new CuentaDeBajaException(cbu);
        }

        cuenta.setBalance(cuenta.getBalance() + monto);
        movimientoDao.saveMovimiento(tipoOperacion, monto, cuenta.getCBU());
        cuentaDao.updateCuenta(cuenta);

        return new Operacion().setCbu(cbu)
                .setSaldoActual(cuenta.getBalance())
                .setMonto(monto)
                .setTipoOperacion(tipoOperacion);
    }
}

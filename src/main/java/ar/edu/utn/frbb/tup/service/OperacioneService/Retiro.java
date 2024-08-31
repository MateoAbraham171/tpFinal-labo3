package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class Retiro {
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientoDao;

    private final String tipoOperacion = "Retiro";

    public Retiro(CuentaDao cuentaDao, MovimientoDao movimientoDao) {
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
    }

    public Operacion retiro(long cbu, double monto) throws CuentaNoEncontradaException, CuentaDeBajaException, NoAlcanzaException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);
        if (!cuenta.getEstado())
            throw new CuentaDeBajaException(cbu);
        if (cuenta.getBalance() < monto)
            throw NoAlcanzaException.ErrorComun(cuenta.getBalance());

        cuenta.setBalance(cuenta.getBalance() - monto);
        movimientoDao.saveMovimiento(tipoOperacion, monto, cbu);
        cuentaDao.updateCuenta(cuenta);

        return new Operacion().setCbu(cbu)
                .setSaldoActual(cuenta.getBalance())
                .setMonto(monto)
                .setTipoOperacion(tipoOperacion);
    }
}

package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class Consulta {
    private final MovimientoDao movimientoDao;
    private final CuentaDao cuentaDao;
    private final String tipoOperacion = "Consulta";

    public Consulta(MovimientoDao movimientoDao, CuentaDao cuentaDao) {
        this.movimientoDao = movimientoDao;
        this.cuentaDao = cuentaDao;
    }

    public Operacion consulta(long cbu) throws CuentaNoEncontradaException, CuentaDeBajaException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException(cbu);
        }

        if (!cuenta.getEstado()) {
            throw new CuentaDeBajaException(cbu);
        }

        movimientoDao.saveMovimiento(tipoOperacion, 0, cuenta.getCBU());

        return new Operacion().setCbu(cbu).setSaldoActual(cuenta.getBalance()).setTipoOperacion(tipoOperacion);
    }
}

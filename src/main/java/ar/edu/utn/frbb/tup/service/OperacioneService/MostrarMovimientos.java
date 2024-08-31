package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.OperacionesException.NoHayMovimientosException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MostrarMovimientos {
    private final MovimientoDao movimientoDao;
    private final CuentaDao cuentaDao;

    public MostrarMovimientos(MovimientoDao movimientoDao, CuentaDao cuentaDao) {
        this.movimientoDao = movimientoDao;
        this.cuentaDao = cuentaDao;
    }

    public List<Movimiento> mostrarMovimientos(long cbu) throws CuentaNoEncontradaException, CuentaDeBajaException, NoHayMovimientosException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);
        if (!cuenta.getEstado())
            throw new CuentaDeBajaException(cbu);

        List<Movimiento> movimientos = movimientoDao.findMovimientos(cbu);

        if (movimientos.isEmpty())
            throw new NoHayMovimientosException();

        return movimientos;
    }
}

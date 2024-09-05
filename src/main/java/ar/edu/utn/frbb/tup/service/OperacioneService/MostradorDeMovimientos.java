package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.OperacionesExceptions.NoHayMovimientosException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MostradorDeMovimientos {
    private final MovimientoDao movimientoDao;
    private final CuentaDao cuentaDao;

    public MostradorDeMovimientos(MovimientoDao movimientoDao, CuentaDao cuentaDao) {
        this.movimientoDao = movimientoDao;
        this.cuentaDao = cuentaDao;
    }

    public List<Movimiento> mostrarMovimientosDeCuenta(long cbu) throws NotFoundException, ConflictException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);
        if (!cuenta.getEstado())
            throw new CuentaDeBajaException(cbu);

        List<Movimiento> movimientosDelCBU = movimientoDao.findMovimientos(cbu);

        if (movimientosDelCBU.isEmpty())
            throw new NoHayMovimientosException();

        return movimientosDelCBU;
    }
}

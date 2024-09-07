package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class ConsultorDeSaldo {
    private final MovimientoDao movimientoDao;
    private final CuentaDao cuentaDao;
    private final String TIPO_DE_OPERACION = "Consulta";

    public ConsultorDeSaldo(MovimientoDao movimientoDao, CuentaDao cuentaDao) {
        this.movimientoDao = movimientoDao;
        this.cuentaDao = cuentaDao;
    }

    public Operacion consultarSaldo(long cbu) throws NotFoundException, ConflictException {
        Cuenta cuenta = cuentaDao.findCuenta(cbu);

        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);
        if (!cuenta.getEstado())
            throw new CuentaDeBajaException(cbu);

        return new Operacion().setCbu(cbu).setSaldoActual(cuenta.getBalance()).setTipoOperacion(TIPO_DE_OPERACION);
    }
}

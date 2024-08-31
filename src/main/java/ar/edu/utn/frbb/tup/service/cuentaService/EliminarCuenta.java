package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentasVaciasException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EliminarCuenta {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientosDao;

    public EliminarCuenta(ClienteDao clienteDao, CuentaDao cuentaDao, MovimientoDao movimientoDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
        this.movimientosDao = movimientoDao;
    }

    public Cuenta eliminarCuenta(long dni, long cbu) throws ClienteNoEncontradoException, CuentasVaciasException, IOException, CuentaNoEncontradaException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        List<Long> cuentasCBU = cuentaDao.getRelacionesDni(dni);
        if (cuentasCBU.isEmpty()) {
            throw new CuentasVaciasException(dni);
        }

        Cuenta cuenta = cuentaDao.findCuentaDelCliente(cbu, dni);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException(dni);
        }

        cuentaDao.deleteCuenta(cbu);
        movimientosDao.deleteMovimiento(cbu);

        return cuenta;
    }
}

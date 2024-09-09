package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class EliminadorDeCuenta {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public EliminadorDeCuenta(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cuenta eliminarCuenta(long dni, long cbu) throws NotFoundException {
        if (clienteDao.findCliente(dni) == null)
            throw new ClienteNoEncontradoException(dni);

        Cuenta cuenta = cuentaDao.findCuentaDelCliente(cbu, dni);
        if (cuenta == null)
            throw new CuentaNoEncontradaException(cbu);

        cuentaDao.deleteCuenta(cbu);
        new MovimientoDao().deleteMovimiento(cbu);

        return cuenta;
    }
}

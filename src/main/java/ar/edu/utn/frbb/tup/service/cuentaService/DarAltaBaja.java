package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DarAltaBaja {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public DarAltaBaja(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cuenta gestionarEstado(long dni, long cbu, boolean opcion) throws ClienteNoEncontradoException, IOException, CuentaNoEncontradaException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        Cuenta cuenta = cuentaDao.findCuentaDelCliente(cbu, dni);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException(dni);
        }

        cuentaDao.deleteCuenta(cbu);
        cuenta.setEstado(opcion);
        cuentaDao.saveCuenta(cuenta);

        return cuenta;
    }
}

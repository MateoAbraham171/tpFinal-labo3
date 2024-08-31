package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MostrarCuenta {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public MostrarCuenta(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Set<Cuenta> mostrarCuenta(long dni) throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        Set<Cuenta> cuentas = cuentaDao.findAllCuentasDelCliente(dni);

        if (cuentas.isEmpty()) {
            throw new CuentaNoEncontradaException(dni);
        }

        return cuentas;
    }
}

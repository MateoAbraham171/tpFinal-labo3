package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoHayCuentasException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MostradorDeCuenta {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public MostradorDeCuenta(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Set<Cuenta> mostrarCuenta(long dni) throws NotFoundException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        Set<Cuenta> cuentas = cuentaDao.findAllCuentasDelCliente(dni);

        if (cuentas.isEmpty()) {
            throw new NoHayCuentasException(dni);
        }

        return cuentas;
    }
}

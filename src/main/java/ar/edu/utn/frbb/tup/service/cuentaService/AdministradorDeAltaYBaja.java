package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import org.springframework.stereotype.Service;

@Service
public class AdministradorDeAltaYBaja {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public AdministradorDeAltaYBaja(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cuenta gestionarEstado(long dni, long cbu, boolean estado) throws NotFoundException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        Cuenta cuenta = cuentaDao.findCuentaDelCliente(cbu, dni);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException(dni);
        }

        cuentaDao.deleteCuenta(cbu);
        cuenta.setEstado(estado);
        cuentaDao.saveCuenta(cuenta);

        return cuenta;
    }
}

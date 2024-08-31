package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.CuentasException.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CrearCuenta {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public CrearCuenta(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cuenta crearCuenta(CuentaDto cuentaDto) throws ClienteNoEncontradoException, CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        Cuenta cuenta = new Cuenta(cuentaDto);

        //valido que exista el cliente, sino lanza exception
        Cliente cliente = clienteDao.findCliente(cuenta.getDniTitular());

        if (cliente == null) {
            throw new ClienteNoEncontradoException(cuenta.getDniTitular());
        }

        Cuenta cuentaExiste = cuentaDao.findCuenta(cuenta.getCBU());

        if (cuentaExiste != null) {
            throw new CuentaAlreadyExistsException();
        }

        tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda(), cuenta.getDniTitular());

        cuentaDao.saveCuenta(cuenta);

        return cuenta;
    }

    private void tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda tipoMoneda, long dniTitular) throws TipoCuentaAlreadyExistsException {
        Set<Cuenta> cuentasCliente = cuentaDao.findAllCuentasDelCliente(dniTitular);

        for (Cuenta cuenta : cuentasCliente) {
            if (tipoCuenta.equals(cuenta.getTipoCuenta()) && tipoMoneda.equals(cuenta.getMoneda())) {
                throw new TipoCuentaAlreadyExistsException();
            }
        }
    }
}

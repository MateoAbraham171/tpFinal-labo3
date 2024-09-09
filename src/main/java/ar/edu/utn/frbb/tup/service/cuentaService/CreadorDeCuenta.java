package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreadorDeCuenta {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public CreadorDeCuenta(ClienteDao clienteDao, CuentaDao cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cuenta crearCuenta(CuentaDto cuentaDto) throws NotFoundException, ConflictException, BadRequestException {
        Cuenta cuenta = new Cuenta(cuentaDto);

        if (clienteDao.findCliente(cuenta.getDniTitular()) == null)
            throw new ClienteNoEncontradoException(cuenta.getDniTitular());
        if (cuentaDao.findCuenta(cuenta.getCBU()) != null)
            throw new CuentaAlreadyExistsException();

        verificarCuentaNoRepetida(cuenta.getTipoCuenta(), cuenta.getMoneda(), cuenta.getDniTitular());

        cuentaDao.saveCuenta(cuenta);

        return cuenta;
    }

    private void verificarCuentaNoRepetida(TipoCuenta tipoCuenta, TipoMoneda tipoMoneda, long dniTitular) throws ConflictException {
        Set<Cuenta> cuentasCliente = cuentaDao.findAllCuentasDelCliente(dniTitular);

        if (cuentasCliente != null) {
            for (Cuenta cuenta : cuentasCliente) {
                if (tipoCuenta.equals(cuenta.getTipoCuenta()) && tipoMoneda.equals(cuenta.getMoneda())) {
                    throw new TipoCuentaAlreadyExistsException();
                }
            }
        }
    }
}
package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasException.*;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CuentaService {
    private final CrearCuenta crearCuenta;
    private final DarAltaBaja darAltaBaja;
    private final EliminarCuenta eliminarCuenta;
    private final MostrarCuenta mostrarCuenta;
    private final CuentaDao cuentaDao;

    public CuentaService(CrearCuenta crearCuenta, DarAltaBaja darAltaBaja, EliminarCuenta eliminarCuenta, MostrarCuenta mostrarCuenta, CuentaDao cuentaDao) {
        this.crearCuenta = crearCuenta;
        this.darAltaBaja = darAltaBaja;
        this.eliminarCuenta = eliminarCuenta;
        this.mostrarCuenta = mostrarCuenta;
        this.cuentaDao = cuentaDao;
    }

    public void inicializarCuentas() {
        cuentaDao.inicializarCuentas();
    }

    public Cuenta crearCuenta(CuentaDto cuentaDto) throws ClienteNoEncontradoException, CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {
        return crearCuenta.crearCuenta(cuentaDto);
    }

    public Cuenta darAltaBaja(long dni, long cvu, boolean opcion) throws CuentaNoEncontradaException, ClienteNoEncontradoException, IOException {
        return darAltaBaja.gestionarEstado(dni, cvu, opcion);
    }

    public Cuenta eliminarCuenta(long dni, long cvu) throws CuentasVaciasException, CuentaNoEncontradaException, ClienteNoEncontradoException, IOException {
        return eliminarCuenta.eliminarCuenta(dni, cvu);
    }

    public Set<Cuenta> mostrarCuenta(long dni) throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        return mostrarCuenta.mostrarCuenta(dni);
    }
}

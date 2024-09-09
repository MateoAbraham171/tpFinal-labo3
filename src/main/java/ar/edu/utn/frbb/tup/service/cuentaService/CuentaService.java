package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class CuentaService {
    private final CreadorDeCuenta creadorDeCuenta;
    private final AdministradorDeAltaYBaja administradorDeAltaYBaja;
    private final EliminadorDeCuenta eliminadorDeCuenta;
    private final MostradorDeCuenta mostradorDeCuenta;
    private final CuentaDao cuentaDao;

    public CuentaService(CreadorDeCuenta creadorDeCuenta, AdministradorDeAltaYBaja administradorDeAltaYBaja, EliminadorDeCuenta eliminadorDeCuenta, MostradorDeCuenta mostradorDeCuenta, CuentaDao cuentaDao) {
        this.creadorDeCuenta = creadorDeCuenta;
        this.administradorDeAltaYBaja = administradorDeAltaYBaja;
        this.eliminadorDeCuenta = eliminadorDeCuenta;
        this.mostradorDeCuenta = mostradorDeCuenta;
        this.cuentaDao = cuentaDao;
    }

    public void inicializarCuentas() {
        cuentaDao.inicializarCuentas();
    }

    public Cuenta crearCuenta(CuentaDto cuentaDto) throws NotFoundException, ConflictException, BadRequestException {
        return creadorDeCuenta.crearCuenta(cuentaDto);
    }

    public Cuenta darAltaBaja(long dni, long cvu, boolean estado) throws NotFoundException {
        return administradorDeAltaYBaja.gestionarEstado(dni, cvu, estado);
    }

    public Cuenta eliminarCuenta(long dni, long cvu) throws NotFoundException {
        return eliminadorDeCuenta.eliminarCuenta(dni, cvu);
    }

    public Set<Cuenta> mostrarCuenta(long dni) throws NotFoundException {
        return mostradorDeCuenta.mostrarCuenta(dni);
    }
}

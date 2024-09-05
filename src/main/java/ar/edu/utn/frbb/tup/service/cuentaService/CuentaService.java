package ar.edu.utn.frbb.tup.service.cuentaService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class CuentaService {
    private final CreadorDeCuentas creadorDeCuentas;
    private final AdministradorDeAltasYBajas administradorDeAltasYBajas;
    private final EliminadorDeCuentas eliminadorDeCuentas;
    private final MostradorDeCuentas mostradorDeCuentas;
    private final CuentaDao cuentaDao;

    public CuentaService(CreadorDeCuentas creadorDeCuentas, AdministradorDeAltasYBajas administradorDeAltasYBajas, EliminadorDeCuentas eliminadorDeCuentas, MostradorDeCuentas mostradorDeCuentas, CuentaDao cuentaDao) {
        this.creadorDeCuentas = creadorDeCuentas;
        this.administradorDeAltasYBajas = administradorDeAltasYBajas;
        this.eliminadorDeCuentas = eliminadorDeCuentas;
        this.mostradorDeCuentas = mostradorDeCuentas;
        this.cuentaDao = cuentaDao;
    }

    public void inicializarCuentas() {
        cuentaDao.inicializarCuentas();
    }

    public Cuenta crearCuenta(CuentaDto cuentaDto) throws NotFoundException, ConflictException {
        return creadorDeCuentas.crearCuenta(cuentaDto);
    }

    public Cuenta darAltaBaja(long dni, long cvu, boolean estado) throws NotFoundException {
        return administradorDeAltasYBajas.gestionarEstado(dni, cvu, estado);
    }

    public Cuenta eliminarCuenta(long dni, long cvu) throws NotFoundException {
        return eliminadorDeCuentas.eliminarCuenta(dni, cvu);
    }

    public Set<Cuenta> mostrarCuenta(long dni) throws NotFoundException {
        return mostradorDeCuentas.mostrarCuenta(dni);
    }
}

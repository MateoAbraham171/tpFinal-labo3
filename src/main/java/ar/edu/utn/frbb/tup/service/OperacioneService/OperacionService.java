package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDistintaMonedaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.OperacionesException.NoHayMovimientosException;
import ar.edu.utn.frbb.tup.exception.OperacionesException.TransferenciaFailException;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperacionService {
    private final Consulta consulta;
    private final Deposito deposito;
    private final MostrarMovimientos mostrarMovimientos;
    private final Retiro retiro;
    private final Transferencia transferencia;
    private final MovimientoDao movimientoDao;

    public OperacionService(Consulta consulta, Deposito deposito, MostrarMovimientos mostrarMovimientos, Retiro retiro, Transferencia transferencia, MovimientoDao movimientoDao) {
        this.consulta = consulta;
        this.deposito = deposito;
        this.mostrarMovimientos = mostrarMovimientos;
        this.retiro = retiro;
        this.transferencia = transferencia;
        this.movimientoDao = movimientoDao;
    }

    public void inicializarMovimientos() {
        movimientoDao.inicializarMovimientos();
    }

    public Operacion consulta(long cvu) throws CuentaNoEncontradaException, CuentaDeBajaException {
        return consulta.consulta(cvu);
    }

    public Operacion deposito(long cvu, double monto) throws CuentaNoEncontradaException, CuentaDeBajaException {
        return deposito.deposito(cvu, monto);
    }

    public List<Movimiento> mostrarMovimientos(long cvu) throws CuentaNoEncontradaException, NoHayMovimientosException, CuentaDeBajaException {
        return mostrarMovimientos.mostrarMovimientos(cvu);
    }

    public Operacion retiro(long cvu, double monto) throws CuentaNoEncontradaException, NoAlcanzaException, CuentaDeBajaException {
        return retiro.retiro(cvu, monto);
    }

    public Operacion transferencia(TransferDto transferDto) throws CuentaNoEncontradaException, CuentaDeBajaException, NoAlcanzaException, CuentaDistintaMonedaException, TransferenciaFailException {
        return transferencia.transferencia(transferDto);
    }
}

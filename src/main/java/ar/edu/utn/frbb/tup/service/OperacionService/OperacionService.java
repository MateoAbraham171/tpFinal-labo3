package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperacionService {

    private final ConsultorDeSaldo consultorDeSaldo;
    private final DepositadorDeSaldo depositadorDeSaldo;
    private final MostradorDeMovimientos mostradorDeMovimientos;
    private final RetiradorDeSaldo retiradorDeSaldo;
    private final TransferService transferService;
    private final MovimientoDao movimientoDao;

    public OperacionService(ConsultorDeSaldo consultorDeSaldo, DepositadorDeSaldo depositadorDeSaldo, MostradorDeMovimientos mostradorDeMovimientos, RetiradorDeSaldo retiradorDeSaldo, TransferService transferService, MovimientoDao movimientoDao) {
        this.consultorDeSaldo = consultorDeSaldo;
        this.depositadorDeSaldo = depositadorDeSaldo;
        this.mostradorDeMovimientos = mostradorDeMovimientos;
        this.retiradorDeSaldo = retiradorDeSaldo;
        this.transferService = transferService;
        this.movimientoDao = movimientoDao;
    }

    public void inicializarMovimientos() {
        movimientoDao.inicializarMovimientos();
    }

    public List<Movimiento> mostrarMovimientosDeCuenta(long cbu) throws NotFoundException, ConflictException {
        return mostradorDeMovimientos.mostrarMovimientosDeCuenta(cbu);
    }

    public Operacion consulta(long cbu) throws NotFoundException, ConflictException {
        return consultorDeSaldo.consultarSaldo(cbu);
    }

    public Operacion deposito(long cbu, MontoDeOperacionDto monto) throws NotFoundException, ConflictException {
        String DEPOSITO = "Deposito";
        return depositadorDeSaldo.deposito(cbu, monto, DEPOSITO);
    }

    public Operacion retiro(long cbu, MontoDeOperacionDto monto) throws NotFoundException, ConflictException {
        String RETIRO = "Retiro";
        return retiradorDeSaldo.retiro(cbu, monto, RETIRO);
    }

    public Operacion transferencia(TransferDto transferDto) throws NotFoundException, ConflictException {
        return transferService.transferencia(transferDto);
    }
}

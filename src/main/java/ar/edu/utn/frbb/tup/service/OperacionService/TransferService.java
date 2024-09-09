package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaDistintaMonedaException;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.*;
import ar.edu.utn.frbb.tup.exception.CuentasExceptions.NoAlcanzaException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.springframework.stereotype.Service;

@Service
public class TransferService {
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientoDao;

    private static final String TRANSFERENCIA_SALIENTE = "Transferencia saliente";
    private static final String TRANSFERENCIA_ENTRANTE = "Transferencia entrante";

    public TransferService(CuentaDao cuentaDao, MovimientoDao movimientoDao) {
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
    }

    public Operacion transferencia(TransferDto transferDto) throws NotFoundException, ConflictException {
        Transfer datosTransfer = new Transfer(transferDto);
        Cuenta cuentaOrigen = cuentaDao.findCuenta(datosTransfer.getCbuOrigen());

        if (cuentaOrigen == null)
            throw new CuentaNoEncontradaException(datosTransfer.getCbuOrigen());
        if(!cuentaOrigen.getEstado())
            throw new CuentaDeBajaException(datosTransfer.getCbuOrigen());

        double montoConCargo = calcularMontoConCargoTransferencia(datosTransfer.getMonto(), cuentaOrigen.getMoneda());

        if (montoConCargo > cuentaOrigen.getBalance())
            throw new NoAlcanzaException(cuentaOrigen.getCBU(), montoConCargo);

        return realizarTransferencia(cuentaOrigen, datosTransfer, montoConCargo);
}

    private double calcularMontoConCargoTransferencia(double monto, TipoMoneda moneda) {
        double cargo = 0;

        if (moneda == TipoMoneda.PESOS && monto > 1000000)
            cargo = 0.02;
        else if (moneda == TipoMoneda.DOLARES && monto > 5000)
            cargo = 0.005;

        return monto + (monto * cargo);
    }

    private Operacion realizarTransferencia(Cuenta cuentaOrigen, Transfer datosTransfer, double montoConCargo) throws NotFoundException, ConflictException {
        BanelcoService banelcoService = new BanelcoService();

        Cuenta cuentaDestino = cuentaDao.findCuenta(datosTransfer.getCbuDestino());
        validateCuentaDestino(cuentaDestino, banelcoService, datosTransfer, cuentaOrigen.getMoneda());

        Operacion transferenciaSaliente = new RetiradorDeSaldo(cuentaDao, movimientoDao).retiro(cuentaOrigen.getCBU(), new MontoDeOperacionDto(montoConCargo), TRANSFERENCIA_SALIENTE);
        transferenciaSaliente.setTipoOperacion(TRANSFERENCIA_SALIENTE + " exitosa!! :)");

        if (cuentaDestino != null)
            new DepositadorDeSaldo(cuentaDao, movimientoDao).deposito(cuentaDestino.getCBU(), new MontoDeOperacionDto(datosTransfer.getMonto()), TRANSFERENCIA_ENTRANTE);
        else
            banelcoService.transfer();

        return transferenciaSaliente;
    }

    private void validateCuentaDestino(Cuenta cuentaDestino, BanelcoService banelcoService, Transfer datosTransfer, TipoMoneda monedaCuentaOrigen) throws NotFoundException, ConflictException {
        if (cuentaDestino == null)
            banelcoService.validateCuentaExiste(datosTransfer.getCbuDestino());
        else if (!cuentaDestino.getEstado())
            throw new CuentaDeBajaException(datosTransfer.getCbuDestino());
        else if (cuentaDestino.getMoneda() != monedaCuentaOrigen)
            throw new CuentaDistintaMonedaException();
    }
}
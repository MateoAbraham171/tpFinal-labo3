package ar.edu.utn.frbb.tup.service.OperacioneService;

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

        // Calcular el monto con cargo
        double montoConCargo = calcularMontoConCargoTransferencia(datosTransfer.getMonto(), cuentaOrigen.getMoneda());


        // Verificar si hay saldo suficiente en la cuenta origen
        if (montoConCargo > cuentaOrigen.getBalance())
            throw new NoAlcanzaException(cuentaOrigen.getCBU(), montoConCargo);


        // Realizar la transferencia según el banco del cliente
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
        if (cuentaDestino == null)
            banelcoService.validateCuentaExiste(datosTransfer.getCbuDestino());

        // Actualizar saldo de cuenta de origen y registrar movimiento
        Operacion transferenciaSaliente = new RetiradorDeSaldo(cuentaDao, movimientoDao).retiro(cuentaOrigen.getCBU(), new MontoDeOperacionDto(montoConCargo), TRANSFERENCIA_SALIENTE);
        transferenciaSaliente.setTipoOperacion(TRANSFERENCIA_SALIENTE + " exitosa!! :)");

        if (cuentaDestino != null)
            new DepositadorDeSaldo(cuentaDao, movimientoDao).deposito(cuentaDestino.getCBU(), new MontoDeOperacionDto(datosTransfer.getMonto()), TRANSFERENCIA_ENTRANTE);
        else
            banelcoService.transfer();

        return transferenciaSaliente;
    }
}

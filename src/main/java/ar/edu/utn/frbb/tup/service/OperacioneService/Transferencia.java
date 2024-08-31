package ar.edu.utn.frbb.tup.service.OperacioneService;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDistintaMonedaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.OperacionesException.TransferenciaFailException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.DAO.*;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import org.springframework.stereotype.Service;

@Service
public class Transferencia {
    private final CuentaDao cuentaDao;
    private final ClienteDao clienteDao;
    private final MovimientoDao movimientoDao;

    private static final String TIPO_OPERACION = "Transferencia a la cuenta ";
    private static final String TIPO_OPERACION_DESTINO = "Deposito recibido de la cuenta ";

    public Transferencia(CuentaDao cuentaDao, ClienteDao clienteDao, MovimientoDao movimientoDao) {
        this.cuentaDao = cuentaDao;
        this.clienteDao = clienteDao;
        this.movimientoDao = movimientoDao;
    }

    public Operacion transferencia(TransferDto transferDto) throws CuentaDeBajaException, NoAlcanzaException, CuentaNoEncontradaException, CuentaDistintaMonedaException, TransferenciaFailException {
        Transfer datosTransfer = new Transfer(transferDto);

        // Calcular el monto con cargo
        double montoConCargo = calcularMontoCargoTransferencia(datosTransfer.getMonto(), datosTransfer.getMoneda());

        // Buscar y validar las cuentas
        Cuenta cuentaOrigen = cuentaDao.findCuenta(datosTransfer.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaDao.findCuenta(datosTransfer.getCuentaDestino());
        validateTransferencia(cuentaOrigen, cuentaDestino, datosTransfer, montoConCargo);

        // Realizar la transferencia según el banco del cliente
        Cliente clienteOrigen = clienteDao.findCliente(cuentaOrigen.getDniTitular());
        Cliente clienteDestino = clienteDao.findCliente(cuentaDestino.getDniTitular());
        return realizarTransferenciaSegunBanco(clienteOrigen, clienteDestino, cuentaOrigen, cuentaDestino, datosTransfer, montoConCargo);
    }

    private void validateTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Transfer datosTransfer, double montoConCargo) throws CuentaDeBajaException, CuentaDistintaMonedaException, CuentaNoEncontradaException, NoAlcanzaException {
        // Verificar existencia de cuentas
        if (cuentaOrigen == null)
            throw new CuentaNoEncontradaException(datosTransfer.getCuentaOrigen());
        if (cuentaDestino == null)
            throw new CuentaNoEncontradaException(datosTransfer.getCuentaDestino());

        // Verificar estado de cuentas
        if (!cuentaOrigen.getEstado())
            throw new CuentaDeBajaException(cuentaOrigen.getCBU());
        if (!cuentaDestino.getEstado())
            throw new CuentaDeBajaException(cuentaDestino.getCBU());

        // Verificar monedas de las cuentas
        if (cuentaOrigen.getMoneda() != datosTransfer.getMoneda())
            throw new CuentaDistintaMonedaException("La cuenta de origen con el CBU " + datosTransfer.getCuentaOrigen() + " es de diferente tipo de moneda.");
        if (cuentaDestino.getMoneda() != datosTransfer.getMoneda())
            throw new CuentaDistintaMonedaException("La cuenta de destino con el CBU " + datosTransfer.getCuentaDestino() + " es de diferente tipo de moneda.");

        // Verificar saldo suficiente en la cuenta origen
        if (montoConCargo > cuentaOrigen.getBalance())
            throw NoAlcanzaException.ErrorTransfer(cuentaOrigen.getCBU(), cuentaOrigen.getBalance());
    }

    private double calcularMontoCargoTransferencia(double monto, TipoMoneda moneda) {
        double cargo = 0;

        if (moneda == TipoMoneda.PESOS && monto >= 1000000) {
            cargo = 0.02;
        } else if (moneda == TipoMoneda.DOLARES && monto >= 5000) {
            cargo = 0.005;
        }

        return monto + (monto * cargo);
    }

    private Operacion realizarTransferenciaSegunBanco(Cliente clienteOrigen, Cliente clienteDestino, Cuenta cuentaOrigen, Cuenta cuentaDestino, Transfer datosTransfer, double montoConCargo) throws TransferenciaFailException {
        if (clienteOrigen.getBanco().equalsIgnoreCase(clienteDestino.getBanco())) {
            return realizarTransferencia(cuentaOrigen, cuentaDestino, datosTransfer, montoConCargo);
        } else {
            return transferenciaBancoExterno(cuentaOrigen, cuentaDestino, datosTransfer, montoConCargo);
        }
    }

    private Operacion realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Transfer datosTransfer, double montoConCargo) {
        // Actualizar saldos de cuentas
        cuentaOrigen.setBalance(cuentaOrigen.getBalance() - montoConCargo);
        cuentaDestino.setBalance(cuentaDestino.getBalance() + datosTransfer.getMonto());

        // Registrar movimientos
        movimientoDao.saveMovimiento(TIPO_OPERACION + cuentaDestino.getCBU() + " - " + datosTransfer.getTipoTransaccion(), montoConCargo, cuentaOrigen.getCBU());
        movimientoDao.saveMovimiento(TIPO_OPERACION_DESTINO + cuentaOrigen.getCBU() + " - " + datosTransfer.getTipoTransaccion(), datosTransfer.getMonto(), cuentaDestino.getCBU());

        // Actualizar cuentas en la base de datos
        cuentaDao.updateCuenta(cuentaOrigen);
        cuentaDao.updateCuenta(cuentaDestino);

        return new Operacion()
                .setCbu(cuentaOrigen.getCBU())
                .setSaldoActual(cuentaOrigen.getBalance())
                .setMonto(montoConCargo)
                .setTipoOperacion(TIPO_OPERACION + cuentaDestino.getCBU() + " - " + datosTransfer.getTipoTransaccion());
    }

    private Operacion transferenciaBancoExterno(Cuenta cuentaOrigen, Cuenta cuentaDestino, Transfer datosTransfer, double montoConCargo) throws TransferenciaFailException {
        if (cuentaDestino.getDniTitular() % 2 == 0) {
            return realizarTransferencia(cuentaOrigen, cuentaDestino, datosTransfer, montoConCargo);
        } else {
            throw new TransferenciaFailException("El banco externo no puede realizar esta transferencia.");
        }
    }
}

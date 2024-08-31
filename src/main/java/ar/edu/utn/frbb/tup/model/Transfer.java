package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoTransaccion;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;

public class Transfer {

    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private TipoMoneda moneda;
    private TipoTransaccion tipoTransaccion;

    public Transfer() {
    }

    public Transfer(TransferDto transferDto) {
        this.cuentaOrigen = transferDto.getCuentaOrigen();
        this.cuentaDestino = transferDto.getCuentaDestino();
        this.monto = transferDto.getMonto();
        this.moneda = TipoMoneda.fromString(transferDto.getMoneda());
        this.tipoTransaccion = TipoTransaccion.fromString(transferDto.getTipoTransaccion());
    }

    public long getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public long getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }
}
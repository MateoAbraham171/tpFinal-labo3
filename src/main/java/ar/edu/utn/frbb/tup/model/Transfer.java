package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;

public class Transfer {

    private long cbuOrigen;
    private long cbuDestino;
    private double monto;

    public Transfer(TransferDto transferDto) {
        this.cbuOrigen = transferDto.cbuOrigen();
        this.cbuDestino = transferDto.cbuDestino();
        this.monto = transferDto.monto();
    }

    public long getCbuOrigen() {
        return cbuOrigen;
    }

    public void setCbuOrigen(long cbuOrigen) {
        this.cbuOrigen = cbuOrigen;
    }

    public long getCbuDestino() {
        return cbuDestino;
    }

    public void setCbuDestino(long cbuDestino) {
        this.cbuDestino = cbuDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
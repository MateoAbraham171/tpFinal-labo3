package ar.edu.utn.frbb.tup.model;

public class Operacion {
    private String tipoOperacion;
    private long cbu;
    private double saldoActual;
    private double monto;

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public Operacion setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
        return this;
    }

    public long getCbu() {
        return cbu;
    }

    public Operacion setCbu(long cbu) {
        this.cbu = cbu;
        return this;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public Operacion setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
        return this;
    }

    public double getMonto() {
        return monto;
    }

    public Operacion setMonto(double monto) {
        this.monto = monto;
        return this;
    }
}

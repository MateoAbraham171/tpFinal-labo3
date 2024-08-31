package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  Cuenta {
    private boolean estado;
    private double balance;
    private long CBU;
    private long dniTitular;
    private LocalDate fechaCreacion;
    private TipoCuenta tipoCuenta;
    private TipoMoneda tipoMoneda;

    public Cuenta(){
        Random r = new Random();
        this.balance = 0;
        this.CBU = r.nextInt(900000) + 100000;
        this.fechaCreacion = LocalDate.now();
        this.estado = true;
    }

    public Cuenta(CuentaDto cuentaDto){
        Random r = new Random();
        this.balance = 0;
        this.CBU = r.nextInt(900000) + 100000;
        this.fechaCreacion = LocalDate.now();
        this.estado = true;
        this.dniTitular = cuentaDto.getDniTitular();
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.tipoMoneda = TipoMoneda.fromString(cuentaDto.getTipoMoneda());
    }

    public boolean getEstado() {
        return estado;
    }
    public Cuenta setEstado(boolean estado) {
        this.estado = estado;
        return this;
    }

    public double getBalance() {
        return balance;
    }
    public Cuenta setBalance(double saldo) {
        this.balance = saldo;
        return this;
    }

    public long getCBU() {
        return CBU;
    }
    public Cuenta setCBU(long CBU) {
        this.CBU = CBU;
        return this;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    public Cuenta setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }


    public TipoMoneda getMoneda() {
        return tipoMoneda;
    }
    public void setMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public long getDniTitular() {
        return dniTitular;
    }
    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
    }
}

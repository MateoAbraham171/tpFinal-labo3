package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class CuentaDistintaMonedaException extends ConflictException {
    public CuentaDistintaMonedaException() {
        super("Error: Las cuentas deben ser de igual moneda");
    }
}

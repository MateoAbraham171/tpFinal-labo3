package ar.edu.utn.frbb.tup.exception.CuentasException;

public class CuentaDeBajaException extends Throwable {
    public CuentaDeBajaException(long cbu) {
        super("Error: La cuenta con cbu " + cbu + " no está dada de baja");
    }
}

package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class CuentaDeBajaException extends ConflictException {
    public CuentaDeBajaException(long cbu) {
        super("Error: La cuenta con cbu " + cbu + " no está dada de baja");
    }
}

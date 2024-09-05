package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

public class CuentaNoEncontradaException extends NotFoundException {
    public CuentaNoEncontradaException(long cbu) {
        super("Error: No existe una cuenta asociada al cbu: " + cbu);
    }
}

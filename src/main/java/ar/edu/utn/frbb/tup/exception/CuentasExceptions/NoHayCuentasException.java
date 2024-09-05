package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

public class NoHayCuentasException extends NotFoundException {
    public NoHayCuentasException(long dni) {
        super("Error: No existen cuentas asociadas al dni: " + dni);
    }
}
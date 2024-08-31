package ar.edu.utn.frbb.tup.exception.CuentasException;

public class CuentasVaciasException extends Throwable {
    public CuentasVaciasException(long dni) {
        super("Error: No existen cuentas asociadas al dni: " + dni);
    }
}
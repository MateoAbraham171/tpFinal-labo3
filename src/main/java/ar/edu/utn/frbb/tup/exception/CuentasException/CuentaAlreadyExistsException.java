package ar.edu.utn.frbb.tup.exception.CuentasException;

public class CuentaAlreadyExistsException extends Throwable {
    public CuentaAlreadyExistsException() {
        super("Error: Ya existe una cuenta con ese CBU");
    }
}

package ar.edu.utn.frbb.tup.exception.CuentasException;

public class TipoCuentaAlreadyExistsException extends Throwable {
    public TipoCuentaAlreadyExistsException() {
        super("Error: Ya posee una cuenta con este tipo de cuenta y tipo de moneda");
    }
}
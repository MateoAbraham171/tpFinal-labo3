package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class TipoCuentaAlreadyExistsException extends ConflictException {
    public TipoCuentaAlreadyExistsException() {
        super("Error: Ya posee una cuenta con este tipo de cuenta y tipo de moneda");
    }
}
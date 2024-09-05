package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class CuentaAlreadyExistsException extends ConflictException {
    public CuentaAlreadyExistsException() {
        super("Error: Ya existe una cuenta con ese CBU");
    }
}

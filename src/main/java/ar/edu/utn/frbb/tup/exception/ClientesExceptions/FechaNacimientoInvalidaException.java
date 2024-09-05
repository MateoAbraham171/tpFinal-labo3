package ar.edu.utn.frbb.tup.exception.ClientesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public class FechaNacimientoInvalidaException extends BadRequestException {
    public FechaNacimientoInvalidaException(String mensaje) { super(mensaje); }
}

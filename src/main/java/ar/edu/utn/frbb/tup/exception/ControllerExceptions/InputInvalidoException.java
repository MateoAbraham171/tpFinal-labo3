package ar.edu.utn.frbb.tup.exception.ControllerExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public class InputInvalidoException extends BadRequestException {
    public InputInvalidoException(String mensaje) { super(mensaje); }
}

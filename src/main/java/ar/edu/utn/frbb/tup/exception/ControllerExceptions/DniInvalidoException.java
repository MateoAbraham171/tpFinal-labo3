package ar.edu.utn.frbb.tup.exception.ControllerExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public class DniInvalidoException extends BadRequestException {
    public DniInvalidoException() {
        super("Error: El DNI debe tener 8 dígitos");
    }
}

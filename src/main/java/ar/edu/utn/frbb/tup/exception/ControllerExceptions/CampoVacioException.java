package ar.edu.utn.frbb.tup.exception.ControllerExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public class CampoVacioException extends BadRequestException {
    public CampoVacioException(String campo) {
        super("Error: Ingrese un " + campo);
    }
}

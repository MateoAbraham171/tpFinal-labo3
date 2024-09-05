package ar.edu.utn.frbb.tup.exception.OperacionesExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class CBUsIgualesException extends ConflictException {
    public CBUsIgualesException() {
        super("Error: los CBU de origen y destino no pueden ser iguales");
    }
}

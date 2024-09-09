package ar.edu.utn.frbb.tup.model.enums;

import ar.edu.utn.frbb.tup.exception.ControllerExceptions.InputInvalidoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public enum TipoMoneda {
    PESOS("P"),
    DOLARES("D");

    private final String descripcion;

    TipoMoneda(String descripcion){
        this.descripcion = descripcion;
    }

    public static TipoMoneda fromString(String text) throws BadRequestException {
        for (TipoMoneda tipo : TipoMoneda.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new InputInvalidoException("No se pudo encontrar un TipoMoneda con la descripción: " + text + ", debe ser 'P' o 'D'");
    }
}
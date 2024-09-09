package ar.edu.utn.frbb.tup.model.enums;

import ar.edu.utn.frbb.tup.exception.ControllerExceptions.InputInvalidoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public enum TipoPersona {

    PERSONA_FISICA("F"),
    PERSONA_JURIDICA("J");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public static TipoPersona fromString(String text) throws BadRequestException {
        for (TipoPersona tipo : TipoPersona.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new InputInvalidoException("No se pudo encontrar un TipoPersona con la descripción: " + text);
    }
}

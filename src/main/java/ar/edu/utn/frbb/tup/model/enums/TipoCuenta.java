package ar.edu.utn.frbb.tup.model.enums;

import ar.edu.utn.frbb.tup.exception.ControllerExceptions.InputInvalidoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;

public enum TipoCuenta {
    CUENTA_CORRIENTE("C"),
    CAJA_AHORRO("A");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public static TipoCuenta fromString(String text) throws BadRequestException {
        for (TipoCuenta tipo : TipoCuenta.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new InputInvalidoException("No se pudo encontrar un TipoCuenta con la descripcion: " + text + ", debe ser 'C' o 'A'");
    }
}
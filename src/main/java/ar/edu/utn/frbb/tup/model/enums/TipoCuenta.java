package ar.edu.utn.frbb.tup.model.enums;

public enum TipoCuenta {
    CUENTA_CORRIENTE("C"),
    CAJA_AHORRO("A");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public static TipoCuenta fromString(String text) throws IllegalArgumentException {
        for (TipoCuenta tipo : TipoCuenta.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un TipoCuenta con la descripcion: " + text + ", debe ser 'C' o 'A'");
    }
}

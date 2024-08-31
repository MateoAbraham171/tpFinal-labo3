package ar.edu.utn.frbb.tup.model.enums;

public enum TipoTransaccion {
    CREDITO("C"),
    DEBITO("D");

    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoTransaccion fromString(String text) {
        for (TipoTransaccion tipo : TipoTransaccion.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un Tipo Transaccion con la descripcion: " + text + ", debe ser 'C' o 'D'");
    }
}
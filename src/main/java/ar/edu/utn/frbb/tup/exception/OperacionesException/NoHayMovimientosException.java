package ar.edu.utn.frbb.tup.exception.OperacionesException;

public class NoHayMovimientosException extends Throwable {
    public NoHayMovimientosException() {
        super("La cuenta no posee movimientos");
    }
}

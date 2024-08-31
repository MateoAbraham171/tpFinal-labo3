package ar.edu.utn.frbb.tup.exception.CuentasException;

public class CuentaNoEncontradaException extends Throwable {
    public CuentaNoEncontradaException(long cbu) {
        super("Error: No existe una cuenta asociada al cbu: " + cbu);
    }
}

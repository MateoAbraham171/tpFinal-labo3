package ar.edu.utn.frbb.tup.exception.CuentasException;

public class NoAlcanzaException extends Throwable {
    public NoAlcanzaException(String message) {
        super(message);
    }

    public static NoAlcanzaException ErrorComun(double balance) {
        return new NoAlcanzaException("Error: No hay suficiente dinero. Su saldo actual es: " + balance);
    }

    public static NoAlcanzaException ErrorTransfer(long cbu, double saldo) {
        return new NoAlcanzaException("No hay suficiente dinero en la cuenta " + cbu + ", su saldo es de $" + saldo);
    }
}

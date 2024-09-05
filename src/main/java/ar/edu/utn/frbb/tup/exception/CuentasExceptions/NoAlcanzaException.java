package ar.edu.utn.frbb.tup.exception.CuentasExceptions;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;

public class NoAlcanzaException extends ConflictException {
    public NoAlcanzaException(double balance) {
        super("Error: No hay suficiente dinero. Su saldo actual es: " + balance);
    }
    public NoAlcanzaException(long cbu, double monto) {
        super("No hay suficiente dinero en la cuenta " + cbu + " para transferir $" + monto);
    }
}

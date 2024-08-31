package ar.edu.utn.frbb.tup.exception.ClientesException;

public class ClienteNoEncontradoException extends Throwable {
    public ClienteNoEncontradoException(long dni) { super("Error: No se ha encontrado un cliente con dni: " + dni); }
}

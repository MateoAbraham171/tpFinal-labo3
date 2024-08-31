package ar.edu.utn.frbb.tup.exception.ControllerException;

//Excepcion que representa un error relacionado con el DNI
public class DniInvalidoException extends RuntimeException{

    public DniInvalidoException(String mensaje) {
        super(mensaje);
    }
}

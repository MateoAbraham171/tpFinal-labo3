package ar.edu.utn.frbb.tup.exception.ControllerException;

//excpecion que representa un error cuando falta un campo requerido
public class CampoVacioException extends RuntimeException{

    public CampoVacioException(String campo) {
        super("Error: Ingrese un " + campo);
    }
}

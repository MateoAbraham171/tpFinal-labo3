package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

import java.util.Random;

public class BanelcoService {

    public void validateCuentaExiste(long cbu) throws NotFoundException {
        //simulamos que banelco busca el cbu en su base y valida que la cuenta existe
        int probabilidad = new Random().nextInt(100);
        if (probabilidad < 30)
            throw new CuentaNoEncontradaException(cbu);
    }

    public void transfer() {
        //simulamos que banelco realiza la transferencia
    }
}

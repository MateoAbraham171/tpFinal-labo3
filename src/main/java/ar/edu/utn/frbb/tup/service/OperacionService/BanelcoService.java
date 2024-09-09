package ar.edu.utn.frbb.tup.service.OperacionService;

import ar.edu.utn.frbb.tup.exception.CuentasExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;

import java.util.Random;

public class BanelcoService {

    public void validateCuentaExiste(long cbu) throws NotFoundException {
        //a fines de testo, establezco dos cuentas con resultado previsible
        //esto es para que el testeo sea determinístico
        if (cbu == 111111)
            return;
        else if(cbu == 222222)
            throw new CuentaNoEncontradaException(cbu);

        //simulamos que banelco busca el cbu en su base y valida que la cuenta existe
        int probabilidad = new Random().nextInt(100);
        if (probabilidad < 5)
            throw new CuentaNoEncontradaException(cbu);
    }

    public void transfer() {
        //simulamos que banelco realiza la transferencia
    }
}
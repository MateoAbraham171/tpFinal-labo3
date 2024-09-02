package ar.edu.utn.frbb.tup.presentation.controller;

import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDeBajaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaDistintaMonedaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.OperacionesException.NoHayMovimientosException;
import ar.edu.utn.frbb.tup.exception.OperacionesException.TransferenciaFailException;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import ar.edu.utn.frbb.tup.service.OperacioneService.OperacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operaciones")

public class OperacionesController {
    private final OperacionService operacionService;

    public OperacionesController(OperacionService operacionService){
        this.operacionService = operacionService;
        operacionService.inicializarMovimientos();
    }

    @GetMapping("/consulta/{cbu}")
    public ResponseEntity<Operacion> getConsultaSaldo(@PathVariable long cbu) throws CuentaNoEncontradaException, CuentaDeBajaException {
        return new ResponseEntity<>(operacionService.consulta(cbu), HttpStatus.OK);
    }

    @GetMapping("/movimientos/{cbu}")
    public ResponseEntity<List<Movimiento>> getMovimientos(@PathVariable long cbu) throws NoHayMovimientosException, CuentaNoEncontradaException, CuentaDeBajaException {
        return new ResponseEntity<>(operacionService.mostrarMovimientos(cbu), HttpStatus.OK);
    }

    @PutMapping("/deposito/{cbu}/{monto}")
    public ResponseEntity<Operacion> putDeposito(@PathVariable long cbu, @PathVariable double monto) throws CuentaNoEncontradaException, CuentaDeBajaException {
        return new ResponseEntity<>(operacionService.deposito(cbu, monto), HttpStatus.OK);
    }

    @PutMapping("/retiro/{cbu}/{monto}")
    public ResponseEntity<Operacion> putRetiro(@PathVariable long cbu, @PathVariable double monto) throws CuentaNoEncontradaException, CuentaDeBajaException, NoAlcanzaException {
        return new ResponseEntity<>(operacionService.retiro(cbu, monto), HttpStatus.OK);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Operacion> postTransferencia(@RequestBody TransferDto transferDto) throws CuentaNoEncontradaException, CuentaDeBajaException, NoAlcanzaException, CuentaDistintaMonedaException, TransferenciaFailException {
        return new ResponseEntity<>(operacionService.transferencia(transferDto), HttpStatus.OK);
    }
}

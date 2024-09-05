package ar.edu.utn.frbb.tup.presentation.controller;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Operacion;
import ar.edu.utn.frbb.tup.presentation.modelDTO.MontoDeOperacionDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;
import ar.edu.utn.frbb.tup.presentation.validator.TransferValidator;
import ar.edu.utn.frbb.tup.service.OperacioneService.OperacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operaciones")

public class OperacionesController {
    private final OperacionService operacionService;
    private final TransferValidator transferValidator;

    public OperacionesController(OperacionService operacionService, TransferValidator transferValidator){
        this.operacionService = operacionService;
        operacionService.inicializarMovimientos();
        this.transferValidator = transferValidator;
    }

    @GetMapping("/movimientos/{cbu}")
    public ResponseEntity<List<Movimiento>> getMovimientos(@PathVariable long cbu) throws NotFoundException, ConflictException {
        return new ResponseEntity<>(operacionService.mostrarMovimientosDeCuenta(cbu), HttpStatus.OK);
    }

    @GetMapping("/consulta/{cbu}")
    public ResponseEntity<Operacion> getConsultaSaldo(@PathVariable long cbu) throws NotFoundException, ConflictException {
        return new ResponseEntity<>(operacionService.consulta(cbu), HttpStatus.OK);
    }

    @PutMapping("/deposito/{cbu}")
    public ResponseEntity<Operacion> putDeposito(@PathVariable long cbu, @RequestBody MontoDeOperacionDto monto) throws NotFoundException, ConflictException {
        return new ResponseEntity<>(operacionService.deposito(cbu, monto), HttpStatus.OK);
    }

        @PutMapping("/retiro/{cbu}")
        public ResponseEntity<Operacion> putRetiro(@PathVariable long cbu, @RequestBody MontoDeOperacionDto monto) throws NotFoundException, ConflictException {
            return new ResponseEntity<>(operacionService.retiro(cbu, monto), HttpStatus.OK);
        }

    @PostMapping("/transferencia")
    public ResponseEntity<Operacion> postTransferencia(@RequestBody TransferDto transferDto) throws BadRequestException, NotFoundException, ConflictException {
        transferValidator.validate(transferDto);
        return new ResponseEntity<>(operacionService.transferencia(transferDto), HttpStatus.OK);
    }
}

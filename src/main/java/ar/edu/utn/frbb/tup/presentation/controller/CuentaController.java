package ar.edu.utn.frbb.tup.presentation.controller;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.service.cuentaService.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    private final CuentaControllerValidator cuentaControllerValidator;
    private final CuentaService cuentaService;

    public CuentaController(CuentaControllerValidator cuentaControllerValidator, CuentaService cuentaService) {
        this.cuentaControllerValidator = cuentaControllerValidator;
        this.cuentaService = cuentaService;
        cuentaService.inicializarCuentas();
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Set<Cuenta>> getAllCuentasByDni(@PathVariable long dni) throws NotFoundException {
        return new ResponseEntity<>(cuentaService.mostrarCuenta(dni), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody CuentaDto cuentaDto) throws BadRequestException, NotFoundException, ConflictException {
        cuentaControllerValidator.validate(cuentaDto);
        return new ResponseEntity<>(cuentaService.crearCuenta(cuentaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{dni}/{cbu}")
    public ResponseEntity<Cuenta> darAltaBajaCuenta(@PathVariable long dni, @PathVariable long cbu, @RequestParam boolean estado) throws NotFoundException {
        return new ResponseEntity<>(cuentaService.darAltaBaja(dni, cbu, estado), HttpStatus.OK);
    }

    @DeleteMapping("/{dni}/{cbu}")
    public ResponseEntity<Cuenta> deleteCuenta(@PathVariable long dni, @PathVariable long cbu) throws NotFoundException {
        return new ResponseEntity<>(cuentaService.eliminarCuenta(dni, cbu), HttpStatus.OK);
    }
}
package ar.edu.utn.frbb.tup.presentation.controller;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentasException.CuentasVaciasException;
import ar.edu.utn.frbb.tup.exception.CuentasException.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.service.cuentaService.CuentaService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("api//cuentas")
public class CuentaController {
    private CuentaControllerValidator cuentaControllerValidator;
    private CuentaService cuentaService;

    public CuentaController(CuentaControllerValidator cuentaControllerValidator, CuentaService cuentaService) {
        this.cuentaControllerValidator = cuentaControllerValidator;
        this.cuentaService = cuentaService;
    }

    // Métodos
    @GetMapping("/{dni}")
    public ResponseEntity<Set<Cuenta>> getAllCuentasByDni(@PathVariable long dni) throws CuentaNoEncontradaException, ClienteNoEncontradoException {
        return new ResponseEntity<>(cuentaService.mostrarCuenta(dni), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody CuentaDto cuentaDto) throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, ClienteNoEncontradoException {
        cuentaControllerValidator.validate(cuentaDto);
        return new ResponseEntity<>(cuentaService.crearCuenta(cuentaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{dni}/{cbu}")
    public ResponseEntity<Cuenta> darAltaBajaCuenta(@PathVariable long dni, @PathVariable long cbu, @RequestParam boolean opcion) throws CuentaNoEncontradaException, ClienteNoEncontradoException, IOException {
        return new ResponseEntity<>(cuentaService.darAltaBaja(dni, cbu, opcion), HttpStatus.OK);
    }

    @DeleteMapping("/{dni}/{cbu}")
    public ResponseEntity<Cuenta> deleteCuenta(@PathVariable long dni, @PathVariable long cbu) throws CuentaNoEncontradaException, ClienteNoEncontradoException, CuentasVaciasException, IOException {
        return new ResponseEntity<>(cuentaService.eliminarCuenta(dni, cbu), HttpStatus.OK);
    }
}
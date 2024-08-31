package ar.edu.utn.frbb.tup.presentation.controller;

import ar.edu.utn.frbb.tup.presentation.validator.CuentaControllerValidator;
import ar.edu.utn.frbb.tup.service.cuentaService.CuentaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    private CuentaControllerValidator cuentaControllerValidator;
    private CuentaService cuentaService;
}

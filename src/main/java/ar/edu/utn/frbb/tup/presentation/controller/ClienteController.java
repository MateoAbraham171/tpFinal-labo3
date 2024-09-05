package ar.edu.utn.frbb.tup.presentation.controller;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.presentation.validator.ClienteControllerValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.service.clienteService.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteControllerValidator clienteControllerValidator;
    private final ClienteService clienteService;

    public ClienteController(ClienteControllerValidator clienteControllerValidator, ClienteService clienteService) {
        this.clienteService = clienteService;
        this.clienteControllerValidator = clienteControllerValidator;
        clienteService.inicializarClientes();
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody ClienteDto clienteDto) throws BadRequestException, ConflictException {
        clienteControllerValidator.validateCliente(clienteDto);
        Cliente cliente = clienteService.crearCliente(clienteDto);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() throws NotFoundException {
        return new ResponseEntity<>(clienteService.mostrarTodosClientes(), HttpStatus.OK);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> getClienteByID(@PathVariable long dni) throws NotFoundException {
        return new ResponseEntity<>(clienteService.mostrarCliente(dni), HttpStatus.OK);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Cliente> deleteCliente(@PathVariable long dni) throws NotFoundException {
        return new ResponseEntity<>(clienteService.eliminarCliente(dni), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Cliente> updateCliente(@RequestBody ClienteDto clienteDto) throws BadRequestException, NotFoundException, ConflictException {
        clienteControllerValidator.validateCliente(clienteDto);
        return new ResponseEntity<>(clienteService.modificarCliente(clienteDto), HttpStatus.OK);
    }
}
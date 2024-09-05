package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;

import java.time.LocalDate;

//clase creada para simular la creacion de los objetos necesarios
//para la ejecucion de los test
public class AdminTest {

    public Cliente getCliente(String nombre, long dni) {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido("Takanaka");
        cliente.setMail("takanaka171@yahoo.com");
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setFechaNacimiento(LocalDate.of(2001, 10, 10));
        cliente.setDireccion("Av. Siempre Viva 123");
        cliente.setDni(dni);

        return cliente;
    }

    public ClienteDto getClienteDto(String nombre, long dni) {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre(nombre);
        clienteDto.setApellido("Takanaka");
        clienteDto.setDni(dni);
        clienteDto.setDireccion("Av. Siempre Viva 123");
        clienteDto.setFechaNacimiento("2001-10-10");
        clienteDto.setTipoPersona("f");
        clienteDto.setMail("takanaka171@yahoo.com");

        return clienteDto;
    }

    public Cuenta getCuenta(long dniTitular, TipoCuenta tipoCuenta, TipoMoneda tipoMoneda, double balance) {
        Cuenta cuenta = new Cuenta();
        cuenta.setDniTitular(dniTitular);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setMoneda(tipoMoneda);
        cuenta.setBalance(balance);

        return cuenta;
    }

    public CuentaDto getCuentaDto(long dniTitular, TipoCuenta tipoCuenta, TipoMoneda tipoMoneda) {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(dniTitular);
        cuentaDto.setTipoCuenta(tipoCuenta.toString());
        cuentaDto.setTipoMoneda(tipoMoneda.toString());

        return cuentaDto;
    }
}

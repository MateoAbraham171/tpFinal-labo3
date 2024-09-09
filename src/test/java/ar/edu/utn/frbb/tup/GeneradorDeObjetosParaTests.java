package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.modelDTO.TransferDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//clase creada para simular la creacion de los objetos necesarios
//para la ejecucion de los test
public class GeneradorDeObjetosParaTests {

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

    public Cuenta getCuenta(long dniTitular, TipoCuenta tipoCuenta, TipoMoneda tipoMoneda) {
        Cuenta cuenta = new Cuenta();
        cuenta.setDniTitular(dniTitular);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setMoneda(tipoMoneda);
        cuenta.setCBU(123456);
        cuenta.setBalance(100000);

        return cuenta;
    }

    public CuentaDto getCuentaDto(long dniTitular, String tipoCuenta, String tipoMoneda) {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(dniTitular);
        cuentaDto.setTipoCuenta(tipoCuenta);
        cuentaDto.setTipoMoneda(tipoMoneda);

        return cuentaDto;
    }

    public List<Cliente> getListaDeClientes() {
        List<Cliente> lista = new ArrayList<>();
        lista.add(getCliente("Mateo", 85876925L));
        lista.add(getCliente("Juan", 12345678L));

        return lista;
    }

    public Set<Cuenta> getListaDeCuentas(Cuenta cuenta){
        Set<Cuenta> lista = new java.util.HashSet<>(Set.of(cuenta));
        lista.add(cuenta);

        return lista;
    }

    public List<Movimiento> getListaDeMovimientos() {
        List<Movimiento> lista = new ArrayList<>();
        lista.add(getMovimiento(123456, "Deposito", 1000));
        lista.add(getMovimiento(123456, "Extraccion", 500));

        return lista;
    }

    private Movimiento getMovimiento(int i, String deposito, int i1) {
        Movimiento movimiento = new Movimiento();
        movimiento.setCBU(i);
        movimiento.setTipoOperacion(deposito);
        movimiento.setMonto(i1);

        return movimiento;
    }

    public TransferDto getTransferDto(long CBUOrigen, long CBUDestino, double monto){
        TransferDto transferDto = new TransferDto(CBUOrigen, CBUDestino, monto);
        return transferDto;
    }
}

package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.NotFoundException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import ar.edu.utn.frbb.tup.service.cuentaService.EliminadorDeCuentas;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EliminadorDeCliente {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;

    public EliminadorDeCliente(ClienteDao clienteDao, CuentaDao cuentaDao){
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }

    public Cliente eliminarCliente(long dni) throws NotFoundException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        List<Long> cbusDeCuentasParaEliminar = cuentaDao.getCBUsVinculadosPorDni(dni);
        EliminadorDeCuentas eliminadorDeCuentas = new EliminadorDeCuentas(clienteDao, cuentaDao);
        for (long cuenta : cbusDeCuentasParaEliminar)
            eliminadorDeCuentas.eliminarCuenta(dni, cuenta);

        clienteDao.deleteCliente(dni);

        return cliente;
    }
}

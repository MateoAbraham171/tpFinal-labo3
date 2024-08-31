package ar.edu.utn.frbb.tup.service.clienteService;

import ar.edu.utn.frbb.tup.exception.ClientesException.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.DAO.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.DAO.MovimientoDao;
import org.springframework.stereotype.Service;

@Service
public class EliminarCliente {
    private final ClienteDao clienteDao;
    private final CuentaDao cuentaDao;
    private final MovimientoDao movimientoDao;

    public EliminarCliente(ClienteDao clienteDao, CuentaDao cuentaDao, MovimientoDao movimientoDao){
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
    }

    public Cliente eliminarCliente(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.findCliente(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException(dni);
        }

        clienteDao.deleteCliente(dni);

//        aca deberia ir una funcion para adquirir todos los numeros de cuenta de la persona
//        List<Long> cbuParaEliminar = cuentaDao.getCuentasByCliente(dni);
//
//        for (Long cbu : cbuParaEliminar) {
//            cuentaDao.deleteCuente(cbuParaEliminar);
//            movimientosDao.deleteMovimientos(cbuParaEliminar)
//        }

        return cliente;
    }
}

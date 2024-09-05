package ar.edu.utn.frbb.tup.service.clienteServiceTests;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.DAO.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.service.clienteService.ClienteService;
import ar.edu.utn.frbb.tup.service.clienteService.CreadorDeCliente;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreadorDeClienteTest {
    private ClienteDto clienteDto;

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private CreadorDeCliente creadorDeCliente;

    @BeforeAll
    public void setUp() {
        clienteDto = new ClienteDto();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearClienteSuccess() throws ConflictException {
        clienteDto.setDni(51754556);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("1990-03-18");
        clienteDto.setTipoPersona("PERSONA_FISICA");
        clienteDto.setMail("juanperez@gmail.com");
        clienteDto.setDireccion("Av. Siempre Viva 123");

        Cliente clienteResultado = creadorDeCliente.crearCliente(clienteDto);

        verify(clienteDao, times(1)).saveCliente(clienteResultado);
    }
}

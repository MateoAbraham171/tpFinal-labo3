package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteMenorException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.FechaNacimientoInvalidaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import java.time.LocalDate;
import java.time.Period;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.*;
import org.springframework.stereotype.Component;

@Component
public class ClienteControllerValidator {
    private static final LocalDate FECHA_NACIMIENTO_MINIMA = LocalDate.of(1900, 1, 1);

    public void validateCliente(ClienteDto clienteDto) throws BadRequestException, ConflictException {
        validateNombre(clienteDto.getNombre());
        validateApellido(clienteDto.getApellido());
        validateDireccion(clienteDto.getDireccion());
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
        validateMail(clienteDto.getMail());
        validateTipoPersona(clienteDto.getTipoPersona());
        validateDni(clienteDto.getDni());
    }

    private void validateNombre(String nombre) throws BadRequestException {
        if (nombre == null || nombre.isEmpty())
            throw new CampoVacioException("nombre");

        validateSoloLetras(nombre, "nombre");
    }

    private void validateApellido(String apellido) throws BadRequestException {
        if (apellido == null || apellido.isEmpty())
            throw new CampoVacioException("apellido");

        validateSoloLetras(apellido, "apellido");
    }

    private void validateSoloLetras(String str, String mensaje) throws BadRequestException {
        if (!str.matches("[a-zA-Z \\-']+"))
            throw new InputInvalidoException("Error: El formato del " + mensaje + " es invalido");
    }

    private void validateDireccion(String direccion) throws BadRequestException {
        if (direccion == null || direccion.isEmpty())
            throw new CampoVacioException("direccion");

        String[] partes = direccion.trim().split("\\s+");

        if (partes.length < 2)
            throw new InputInvalidoException("Error: La dirección debe contener un nombre de calle y un número");

        String numeroCalle = partes[partes.length - 1];
        if (!numeroCalle.matches("\\d+"))
            throw new InputInvalidoException("Error: La dirección debe terminar con un número válido");
    }

    private void validateFechaNacimiento(String fechaNacimiento) throws BadRequestException, ConflictException {
            LocalDate fechaNacimientoParseada = LocalDate.parse(fechaNacimiento);
            validarSiFechaDeNacimientoEsPosible(fechaNacimientoParseada);
            esMayor(fechaNacimientoParseada);
    }

    private void validarSiFechaDeNacimientoEsPosible(LocalDate fechaNacimiento) throws BadRequestException {
        if (fechaNacimiento.isAfter(LocalDate.now()))
            throw new FechaNacimientoInvalidaException("La fecha de nacimiento no puede ser futura.");
        if (fechaNacimiento.isBefore(FECHA_NACIMIENTO_MINIMA))
            throw new FechaNacimientoInvalidaException("La fecha de nacimiento debe ser a partir del " + FECHA_NACIMIENTO_MINIMA + ".");
    }

    private void esMayor(LocalDate fechaNacimiento) throws ConflictException {
        Period periodo = Period.between(fechaNacimiento, LocalDate.now());

        if (periodo.getYears() < 18)
            throw new ClienteMenorException("Solo se permiten clientes mayores de 18 años");
    }

    private void validateMail(String mail) throws BadRequestException {
        if (mail == null || mail.isEmpty())
            throw new CampoVacioException("mail");
        if (!mail.contains("@") || !mail.contains("."))
            throw new InputInvalidoException("Error: Ingrese un mail válido");
    }

    private void validateTipoPersona(String tipoPersona) throws BadRequestException {
        if (tipoPersona == null || tipoPersona.isEmpty())
            throw new CampoVacioException("tipoPersona");

        TipoPersona.fromString(tipoPersona);
    }

    public void validateDni(long dni) throws BadRequestException {
        if (dni == 0) throw new CampoVacioException("DNI");
        if (dni < 10000000 || dni > 99999999) throw new DniInvalidoException();
    }
}
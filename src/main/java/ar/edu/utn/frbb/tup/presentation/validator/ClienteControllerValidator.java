package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.exception.HttpExceptions.BadRequestException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.ClienteMenorException;
import ar.edu.utn.frbb.tup.exception.ClientesExceptions.FechaNacimientoInvalidaException;
import ar.edu.utn.frbb.tup.exception.HttpExceptions.ConflictException;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.exception.ControllerExceptions.*;
import org.springframework.stereotype.Component;


@Component
public class ClienteControllerValidator {
    private static final LocalDate FECHA_NACIMIENTO_MINIMA = LocalDate.of(1900, 1, 1);

    public void validateCliente(ClienteDto clienteDto) throws BadRequestException, ConflictException {
        validateAllData(clienteDto);
    }

    private void validateAllData(ClienteDto clienteDto) throws BadRequestException, ConflictException {
        validateNombre(clienteDto.getNombre());
        validateApellido(clienteDto.getApellido());
        validateDireccion(clienteDto.getDireccion());
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
        validateMail(clienteDto.getMail());
        validateTipoPersona(clienteDto.getTipoPersona());
        validateDni(clienteDto.getDni());
    }

    // Valida que el nombre no sea nulo ni esté vacío.
    private void validateNombre(String nombre) throws BadRequestException {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoVacioException("nombre");
        }
        validateSoloLetras(nombre, "nombre");
    }

    // Valida que el apellido no sea nulo ni esté vacío.
    private void validateApellido(String apellido) throws BadRequestException {
        if (apellido == null || apellido.isEmpty()) {
            throw new CampoVacioException("apellido");
        }
        validateSoloLetras(apellido, "apellido");
    }

    //Valida que la direccion no sea nula, no este vacio y que contenga nombre de calle y altura.
    private void validateDireccion(String direccion) throws BadRequestException {
        if (direccion == null || direccion.isEmpty()) {
            throw new CampoVacioException("direccion");
        }

        // Dividimos la dirección en partes separadas por espacios
        String[] partes = direccion.trim().split("\\s+");

        // Verificamos que haya al menos dos partes
        if (partes.length < 2) {
            throw new IllegalArgumentException("Error: La dirección debe contener un nombre de calle y un número");
        }

        // Verificamos que la última parte sea numérica (número de la calle)
        String numeroCalle = partes[partes.length - 1];
        if (!numeroCalle.matches("\\d+")) {
            throw new IllegalArgumentException("Error: La dirección debe terminar con un número válido");
        }
    }

    //Valida que el mail no este vacio, no sea nulo y contenga @ y .
    private void validateMail(String mail) throws BadRequestException {
        if (mail == null || mail.isEmpty()) {
            throw new CampoVacioException("mail");
        }
        if (!mail.contains("@") || !mail.contains(".")) {
            throw new IllegalArgumentException("Error: Ingrese un mail válido");
        }
    }

    //Valida que tipo de persona no este vacio y sea valido
    public void validateTipoPersona(String tipoPersona) throws BadRequestException {
        try {
            TipoPersona.fromString(tipoPersona);
        } catch (IllegalArgumentException ex) {
            throw new InputInvalidoException("El tipo de persona no es correcto.");
        }
    }

    public void validateDni(long dni) throws BadRequestException {
        if (dni == 0) throw new CampoVacioException("DNI");
        if (dni < 10000000 || dni > 99999999) throw new DniInvalidoException();
    }

    private void validateFechaNacimiento(String fechaNacimiento) throws BadRequestException, ConflictException {
        try {
            LocalDate fechaNacimientoParseada = LocalDate.parse(fechaNacimiento);
            validarSiFechaDeNacimientoEsPosible(fechaNacimientoParseada);
            esMayor(fechaNacimientoParseada);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Error: La fecha de nacimiento no es válida");
        }
    }

    private void validarSiFechaDeNacimientoEsPosible(LocalDate fechaNacimiento) throws BadRequestException {
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new FechaNacimientoInvalidaException("La fecha de nacimiento no puede ser futura.");
        }
        if (fechaNacimiento.isBefore(FECHA_NACIMIENTO_MINIMA)) {
            throw new FechaNacimientoInvalidaException("La fecha de nacimiento debe ser a partir del " + FECHA_NACIMIENTO_MINIMA + ".");
        }
    }

    private void esMayor(LocalDate fechaNacimiento) throws ConflictException {
        Period periodo = Period.between(fechaNacimiento, LocalDate.now());

        if (periodo.getYears() < 18) {
            throw new ClienteMenorException("Solo se permiten clientes mayores de 18 años");
        }
    }

    public void validateSoloLetras(String str, String mensaje) throws BadRequestException {
        if (!str.matches("[a-zA-Z \\-']+")) {
            throw new InputInvalidoException("Error: El formato del " + mensaje + " es invalido");
        }
    }
}


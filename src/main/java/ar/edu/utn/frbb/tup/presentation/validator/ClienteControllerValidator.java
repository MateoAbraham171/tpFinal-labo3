package ar.edu.utn.frbb.tup.presentation.validator;

import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import ar.edu.utn.frbb.tup.presentation.modelDTO.ClienteDto;
import ar.edu.utn.frbb.tup.exception.ControllerException.*;
import org.springframework.stereotype.Component;


@Component
public class ClienteControllerValidator {

    public void validateCliente(ClienteDto clienteDto) throws InputInvalidoException {
        validateAllData(clienteDto);
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
    }

    public void validateClienteModificacion(ClienteDto clienteDto) {
        validateDni(clienteDto.getDni());

        if (clienteDto.getFechaNacimiento() != null) {
            validateFechaNacimiento(clienteDto.getFechaNacimiento());
        }
    }

    private void validateAllData(ClienteDto clienteDto) throws InputInvalidoException {
        validateNombre(clienteDto.getNombre());
        validateApellido(clienteDto.getApellido());
        validateDireccion(clienteDto.getDireccion());
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
        validateBanco(clienteDto.getBanco());
        validateMail(clienteDto.getMail());
        validateTipoPersona(clienteDto.getTipoPersona());
        validateDni(clienteDto.getDni());
    }

    // Valida que el nombre no sea nulo ni esté vacío.
    private void validateNombre(String nombre) throws InputInvalidoException {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoVacioException("nombre");
        }
        validateSoloLetras(nombre, "nombre");
    }

    // Valida que el apellido no sea nulo ni esté vacío.
    private void validateApellido(String apellido) throws InputInvalidoException {
        if (apellido == null || apellido.isEmpty()) {
            throw new CampoVacioException("apellido");
        }
        validateSoloLetras(apellido, "apellido");
    }

    //Valida que la direccion no sea nula, no este vacio y que contenga nombre de calle y altura.
    private void validateDireccion(String direccion) {
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

    //Valida que el banco no sea nulo ni este vacio
    private void validateBanco(String banco) throws InputInvalidoException {
        if (banco == null || banco.isEmpty()) {
            throw new CampoVacioException("banco");
        }
        validateSoloLetras(banco, "banco");
    }

    //Valida que el mail no este vacio, no sea nulo y contenga @ y .
    private void validateMail(String mail) {
        if (mail == null || mail.isEmpty()) {
            throw new CampoVacioException("mail");
        }
        if (!mail.contains("@") || !mail.contains(".")) {
            throw new IllegalArgumentException("Error: Ingrese un mail válido");
        }
    }

    //Valida que tipo de persona no este vacio y sea valido
    public void validateTipoPersona(String tipoPersona) throws InputInvalidoException {
        try {
            TipoPersona.fromString(tipoPersona);
        } catch (IllegalArgumentException ex) {
            throw new InputInvalidoException("El tipo de persona no es correcto.");
        }
    }

    private void validateDni(long dni) {
        if (dni == 0) throw new CampoVacioException("DNI");
        if (dni < 10000000 || dni > 99999999) throw new IllegalArgumentException("Error: El DNI debe tener 8 dígitos");
    }

    private void validateFechaNacimiento(String fechaNacimiento) {
        try {
            LocalDate.parse(fechaNacimiento);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error: La fecha de nacimiento no es correcta");
        }
    }

    public void validateSoloLetras(String str, String mensaje) throws InputInvalidoException {
        if (!str.matches("[a-zA-Z \\-']+")) {
            throw new InputInvalidoException("Error: El formato del " + mensaje + " es invalido");
        }
    }
}


# 🏦 Simulación de Sistema Bancario

Este proyecto es una simulación a pequeña escala de un sistema bancario, desarrollado como trabajo final para la materia "Laboratorio III" de la Tecnicatura Universitaria en Programación (TUP) en la Universidad Tecnológica Nacional, Facultad Regional de Bahía Blanca (UTN FRBB).

## 📋 Descripción

El programa simula las operaciones básicas de un banco, incluyendo:
- Gestión de clientes:
  - Creación de clientes.
  - Consulta de clientes.
  - Consulta de cliente por DNI.
  - Modificación de cliente.
  - Eliminación de cliente.
- Gestión de cuentas bancarias:
  - Creación de cuenta.
  - Consulta de cuentas por DNI del titular.
  - Gestión del estado de una cuenta (alta/baja).
  - Eliminación de cuenta.
- Operaciones bancarias:
    - Retiros.
    - Depósitos.
    - Transferencias entre cuentas.
    - Consulta de saldo.
    - Consulta de movimientos.

## 🛠 Tecnologías Utilizadas

- Java 21.0.3
- Maven
- Spring Boot
- JUnit
- Mockito
- IDE: IntelliJ Idea

## 🚀 Cómo ejecutar el programa

Para ejecutar el programa desde la consola, utiliza el siguiente comando:

```bash
mvn spring-boot:run
```

## 🎨 Decisiones de Diseño
A continuación, algunas de las decisiones de diseño tomadas durante el desarrollo del proyecto:

- ### Generales
  - Se utilizó el patrón de diseño MVC (Modelo-Vista-Controlador) para organizar el código.
  - Se utilizó el patrón de diseño DTO (Data Transfer Object) para transferir datos entre las capas de la aplicación.
  - Se utilizó manejo de archivo .txt para almacenar los datos de los clientes y cuentas (por decisión personal).
  - Existen tres excepciones "padre": "BadRequestException", "NotFoundException" y "ConflictException", mientras que el resto de las excepciones heredan de estas. Se realizó de esta manera para mantener el código más limpio.
  - Las clases "ClienteService", "CuentaService" y "OperacionService" simplemente invocan a cada uno de los métodos de los servicios de cada entidad, para que el controlador no tenga que hacerlo y las clases mencionadas no sean muy extensas.
- ### Cliente
  - Los DNI de los clientes son únicos y deben poseer ocho dígitos (por decisión personal).
  - Solo puede ser cliente del banco una persona mayor de edad (por decisión personal).
  - Se considera que una dirección debe tener al menos un nombre (String) y un número de vivienda (Integer).
- ### Cuenta
  - Los CBU de las cuentas son únicos, son numéricos y deben tener seis dígitos (por decisión personal).
  - Los CBU de otros bancos pueden ser cualquier número menos cero (por decisión personal).
  - Las cuentas no almacenan todo el contenido del titular, solo su DNI.
  - La fecha mínima de un cliente para poder abrir una cuenta es 01/01/1900 (por decisión personal).
  - Para mostrar una cuenta se necesita saber el DNI del titular.
  - Al eliminar una cuenta, también se eliminan los movimientos relacionados a la misma.
  - No se pueden mostrar todas las cuentas del banco, pero sí todas las cuentas de un cliente.
- ### Operaciones
  - El modelo "Operacion" se utiliza para mostrarse ante el usuario y que este no pueda ver el movimiento completo, ya que podría contener información no deseada.
  - La clase "BanelcoService" es simplemente una simulación de un servicio externo que se encarga de realizar o rechazar transferencias a bancos externos.
- ### Transferencia
  - El tipo de moneda no necesita ser declarado en las transferencias, pues es un atributo propio de las monedas.
  - El monto de la transferencia puede tener la cantidad de decimales que el usuario quiera, pero el balance de la cuenta siempre se manejará con dos decimales.

## 🔗 Endpoints y posibles errores

A continuación se enumeran los endpoints disponibles en la API, junto con ejemplos de uso y posibles errores:
### Cliente
1. **Crear Cliente**
    - Método: POST
    - URL: `/api/clientes`
    - Ejemplo de cuerpo de la solicitud:
      ```json
      {
       "nombre" : "Miyamoto",
       "apellido" : "Musashi",
       "dni" : 12345678,
       "fechaNacimiento" : "2001-08-12",
       "direccion" : "Av. Siempreviva 742",
       "tipoPersona" : "F",
       "mail" : "myamoto_musashi@gmail.com"
      }
      ```
    - Posibles errores:
      - ❌ 400 Bad Request:
        - Si hay algún campo vacío o nulo.
        - Si el nombre o apellido contienen números.
        - Si la dirección no posee dos o más partes.
        - Si la dirección no termina con un número.
        - Si la fecha de nacimiento es futura.
        - Si la fecha de nacimiento es menor a 1900.
        - Si el mail no posee un "@" y un punto.
        - Si el tipo de persona no es "F", "J", "f" o "j".
        - Si el DNI no posee 8 dígitos.
      - 🔄 409 Conflict:
        - Si la persona es menor de edad.
        - Si el DNI ya está registrado.
2. **Obtener Todos Los Clientes**
    - Método: GET
    - URL: `/api/clientes`
    - Posibles errores:
      - 🔍 404 Not Found:
          - Si no se encuentran clientes registrados.
3. **Obtener Cliente Por DNI**
    - Método: GET
    - URL: `/api/clientes/{dni}`
    - Posibles errores:
      - 🔍 404 Not Found:
          - Si no se encuentra el cliente con el DNI especificado.
4. **Modificar Cliente**
    - Método: PUT
    - URL: `/api/clientes/{dni}`
    - Ejemplo de cuerpo de la solicitud:
      ```json
      {
       "nombre" : "Kozuki",
       "apellido" : "Oden",
       "dni" : 66666666,
       "fechaNacimiento" : "2000-12-18",
       "direccion" : "Grove Street 123",
       "tipoPersona" : "F",
       "mail" : "odenNiSoro@gmail.com"
      }
      ```
    - Posibles errores:
      - ❌ 400 Bad Request:
        - Si hay algún campo vacío o nulo. 
        - Si el nombre o apellido contienen números.
        - Si la dirección no posee dos o más partes.
        - Si la dirección no termina con un número.
        - Si la fecha de nacimiento es futura.
        - Si la fecha de nacimiento es menor a 1900.
        - Si el mail no posee un "@" y un punto.
        - Si el tipo de persona no es "F", "J", "f" o "j".
        - Si el DNI no posee 8 dígitos.
      - 🔍 404 Not Found:
        - Si no se encuentra el cliente con el DNI especificado.
5. **Eliminar Cliente**
    - Método: DELETE
    - URL: `/api/clientes/{dni}`
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no se encuentra el cliente con el DNI especificado.
### Cuenta
1. **Crear Cuenta**
    - Método: POST
    - URL: `/api/cuentas`
    - Ejemplo de cuerpo de la solicitud:
      ```json
      {
        "dniTitular": 12345678,
        "tipoCuenta": "C",
        "tipoMoneda": "d"
      }
      ```
    - Posibles errores:
      - ❌ 400 Bad Request:
        - Si hay algún campo vacío o nulo.
        - Si el DNI no posee 8 dígitos.
        - Si el tipo de cuenta no es "C", "A", "c" o "a".
        - Si el tipo de moneda no es "P", "D", "p" o "d".
      - 🔍 404 Not Found:
        - Si no se encuentra el cliente con el DNI especificado.
      - 🔄 409 Conflict:
        - Si el cliente ya posee una cuenta del mismo tipo.
        - Si el CBU ya existe.
2. **Obtener Cuentas Por DNI**
    - Método: GET
    - URL: `/api/cuentas/{dni}`
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no se encuentra el cliente con el DNI especificado.
        - Si no se encuentra cuentas registradas.
3. **Cambiar Estado De Cuenta**
    - Método: PUT
    - URL: `/api/cuentas/{dni}/{cbu}?estado=boolean`
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no encuentra al cliente con el DNI especificado.
        - Si no se encuentra la cuenta con el CBU especificado.
4. **Eliminar Cuenta**
    - Método: DELETE
    - URL: `/api/cuentas/{dni}/{cbu}`
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no encuentra al cliente con el DNI especificado.
        - Si no se encuentra la cuenta con el CBU especificado.
### Operaciones
1. **Consultar Saldo**
    - Método: GET
    - URL: `/api/operaciones/consulta/{cbu}`
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no se encuentra la cuenta con el CBU especificado.
      - 🔄 409 Conflict:
        - Si la cuenta está dada de baja.
2. **Mostrar Movimientos De Cuenta**
    - Método: GET
    - URL: `/api/operaciones/movimientos/{cbu}`
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no se encuentra la cuenta con el CBU especificado.
        - Si no hay movimientos registrados.
      - 🔄 409 Conflict:
        - Si la cuenta está dada de baja.
3. **Depositar**
    - Método: PUT
    - URL: `/api/operaciones/deposito/{cbu}`
    - Ejemplo de cuerpo de la solicitud:
      ```json
      {
        "monto": 1000
      }
      ```
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no se encuentra la cuenta con el CBU especificado.
      - 🔄 409 Conflict:
        - Si la cuenta está dada de baja.
4. **Retirar**
    - Método: PUT
    - URL: `/api/operaciones/retiro/{cbu}`
    - Ejemplo de cuerpo de la solicitud:
      ```json
      {
        "monto": 1000
      }
      ```
    - Posibles errores:
      - 🔍 404 Not Found:
        - Si no se encuentra la cuenta con el CBU especificado.
      - 🔄 409 Conflict:
        - Si la cuenta está dada de baja.
        - Si el monto a retirar es mayor al saldo de la cuenta.
5. **Transferir**
    - Método: POST
    - URL: `/api/operaciones/transferencia`
    - Ejemplo de cuerpo de la solicitud:
      ```json
      {
        "cbuOrigen": 123456,
        "cbuDestino": 654321,
        "monto": 1000
      }
      ```
    - Posibles errores:
      - ❌ 400 Bad Request:
        - Si algún campo está vacío.
        - Si la cuenta de origen o destino está dada de baja.
        - Si el monto a transferir es mayor al saldo de la cuenta de origen.
      - 🔍 404 Not Found:
        - Si no se encuentra la cuenta con el CBU de origen o destino especificado.
      - 🔄 409 Conflict:
        - Si los CBU de origen y destino son iguales.
        - Si la cuenta de origen no posee el monto suficiente.
        - Si la cuenta de origen y de destino son de distinta moneda.
# tup2024
ACLARACIONES:
- Decidi inyectar las dependencias mediante un constructor y no por @Autowired, ya que esto permite mejorar la testabilidad y facilitar la creación de pruebas unitarias.
- el tipo de moneda no necesita ser declarado en las transferencias pues es un atributo propio de las cuentas
- el monto de la transferencia puede tener la cantidad de decimales que el usuario quiera, pero el balance de la cuenta siempre se manejara con 2 decimales.
- la clase APIExceptionHandler maneja las excepciones que pueden devolverse a la API REST, divididas y organizadas por tipo de error.
- 

PENDIENTES:
- Test:
  - TransferenciaService faltan algunos test y OperacionService
  - Validator de cuenta y transferencia
  - Controllers todos
- Documentación
- todos los DTO pueden pasar a ser record
- limpiar getters y setters que no se usen
- 
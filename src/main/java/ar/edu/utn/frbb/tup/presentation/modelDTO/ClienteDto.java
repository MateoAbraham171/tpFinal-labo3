package ar.edu.utn.frbb.tup.presentation.modelDTO;

public class ClienteDto extends PersonaDto {
    private String tipoPersona;
    private String mail;

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public void setMail(String mail) { this.mail = mail; }

    public String getMail() { return mail; }
}
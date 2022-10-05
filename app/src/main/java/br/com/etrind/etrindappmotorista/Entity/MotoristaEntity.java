package br.com.etrind.etrindappmotorista.Entity;

public class MotoristaEntity {

    public MotoristaEntity(){
    }

    public MotoristaEntity(String cnh){
        this.Cnh = cnh;
    }

    public MotoristaEntity(String cnh, String telefone){
        this.Cnh = cnh;
        this.Telefone = telefone;
    }

    public String Cnh;
    public String Telefone;
    public String Token;
    public String Nome;
    public String IdMotorista;
    public String TokenApi;
    public Boolean MotoristaAutenticado;
}

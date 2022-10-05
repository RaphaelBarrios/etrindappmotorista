package br.com.etrind.etrindappmotorista.Entity;

public class LogEntity {
    public int LogId ;
    public String Tipo;
    public String DataHora;
    public String Descricao;

    public LogEntity(String tipo, String dataHora, String descricao){
        this.Tipo = tipo;
        this.DataHora = dataHora;
        this.Descricao = descricao;
    }

    public LogEntity(){ }
}

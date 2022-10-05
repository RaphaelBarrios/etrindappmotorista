package br.com.etrind.etrindappmotorista.Entity;

import java.util.ArrayList;

public class AtivacaoEntity {
    public int Numero;
    public String Data;
    public String Processo;
    public String Transportadora;
    public String OrigemTitulo;
    public Boolean ViagemEmAndamento;
    @SuppressWarnings("CanBeFinal")
    public ArrayList<AtivacaoItinerarioEntity> ListaItinerarios;
    public String ListaItinerariosStr;

    //utilizado para o calculo de estimatica de tempo do percurso api google distance matrix
    public double LatitudeDestino;
    public double LongitudeDestino;
    public int TempoAttEta;

    public AtivacaoEntity(){
        this.ListaItinerarios = new ArrayList<>();
        this.ListaItinerariosStr = "";
    }
}
